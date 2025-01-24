package org.vindicterra.vindicterralib.entities.players;
/*
AUTHOR: Dev_Richard (https://www.spigotmc.org/members/dev_richard.38792/)
DESC: A simple and easy to use class that can get and set a player's total experience points.

Feel free to use this class in both public and private plugins, however if you release your
plugin please link to this gist publicly so that others can contribute and benefit from it.
*/

import java.math.BigDecimal;
import java.math.MathContext;

import lombok.Getter;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class ExperienceManager {

    /**
     * Gets the total experience points of the Player
     * @return the player's total experience
     */
    public static int getTotalExperience(Player player) {
        int level = player.getLevel();

        // Get the amount of experience in all the player's levels
        int experience = getExpOfLevels(level);

        // Get the amount of exp needed for the next level
        int neededExp = getExpOfLevel(level + 1);
        // getExp returns a range of 0-1 of player's progress to the next level
        var progress = BigDecimal.valueOf(player.getExp());
        experience +=
                // Multiply level progress by level total to get current exp
                progress.multiply( BigDecimal.valueOf(neededExp) )
                // Round out any decimals
                .round(MC_INT)
                // Convert to an int for summation
                .intValue();
        return experience;
    }

    /**
     * Sets the experience of the Player
     * @param xp experience points to set
     */
    public static void setTotalExperience(Player player, int xp) {
        PlayerExp playerExp = convertExp(xp);
        player.setLevel(playerExp.getLevel());
        player.setExp(playerExp.getProgress());
    }

    /**
     * A storage object that contains API-ready data about a player's levels and experience
     * Class has public fields and getters the public fields may go away in the future
     */
    @Getter
    public static class PlayerExp {
        /**
         * The player's level
         * -- GETTER --
         *  The player's level

         */
        private final int level;
        /**
         * The player's experience to the next level
         * -- GETTER --
         *  The player's experience to the next level

         */
        private final int exp;

        /**
         * A 0-1 range of the player's progress to the next level
         * -- GETTER --
         *  A 0-1 range of the player's progress to the next level

         */
        private final float progress;

        PlayerExp(int l, int e, float p){
            this.level = l;
            this.exp = e;
            this.progress = p;
        }
    }

    // TODO check what precision is actually needed of MathContext
    private static final MathContext MC = new MathContext(10);
    private static final MathContext MC_REDUCED = new MathContext(8);// Reduced precision for rounding
    private static final MathContext MC_INT = new MathContext(0);
    // Constants for the experience calculation
    private static final BigDecimal THREE = new BigDecimal(3);
    private static final BigDecimal NINE = new BigDecimal(9);
    private static final BigDecimal TWO_OVER_FIVE = new BigDecimal(2).divide(new BigDecimal(5), MC);
    private static final BigDecimal SEVENTY_EIGHT_THIRTY_NINE_OVER_FORTY = new BigDecimal(7839).divide(new BigDecimal(40), MC);
    private static final BigDecimal EIGHTY_ONE_OVER_TEN = new BigDecimal(81).divide(new BigDecimal(10), MC);
    private static final BigDecimal TWO_OVER_NINE = new BigDecimal(2).divide(NINE, MC);
    private static final BigDecimal THREE_HUNDRED_TWENTY_FIVE_OVER_EIGHTEEN = new BigDecimal(325).divide(new BigDecimal(18), MC);
    private static final BigDecimal FIFTY_FOUR_THOUSAND_TWO_HUNDRED_FIFTEEN_OVER_SEVENTY_TWO = new BigDecimal(54215).divide(new BigDecimal(72), MC);

    /**
     * Converts an exp value into a PlayerExp object containing level information
     * @see PlayerExp
     * @param xp the exp value to work off of
     * @return A PlayerExp object with API-ready data
     */
    public static PlayerExp convertExp(int xp) {
        int level = getLevelFromExp(xp);
        int currentLevelExp = getExpOfLevels(level);
        int nextLevelExp = getExpOfLevels(level + 1);
        int remainder = xp - currentLevelExp;
        BigDecimal ratio = new BigDecimal(remainder)
                .divide(new BigDecimal(nextLevelExp - currentLevelExp), MC).round(MC_REDUCED);

        return new PlayerExp(level, remainder, ratio.floatValue());
    }

    private static int getLevelFromExp(int xp) {
        // Convert to BigDecimal for precision
        BigDecimal xpBigDecimal = new BigDecimal(xp);
        if (xp < 353) { // Levels 0–16
            // (xp + 9)^0.5 - 3
            return xpBigDecimal.add(NINE) // (xp + 9)
                    .sqrt(MC) // ^0.5
                    .subtract(THREE) // - 3
                    .round(MC_INT)
                    .intValue();
        } else if (xp < 1508) { // Levels 17–31
            // ((xp - 7839/40) * 2/5)^0.5 + 81/10
            return xpBigDecimal.subtract(SEVENTY_EIGHT_THIRTY_NINE_OVER_FORTY) // (xp - 7839/40)
                    .multiply(TWO_OVER_FIVE) // * 2/5
                    .sqrt(MC) // ^0.5
                    .add(EIGHTY_ONE_OVER_TEN) // + 81/10
                    .round(MC_INT)
                    .intValue();

        } else { // Levels 32+
            // ((xp - 54215/72) * 2/9)^0.5 + 325/18
            return xpBigDecimal.subtract(FIFTY_FOUR_THOUSAND_TWO_HUNDRED_FIFTEEN_OVER_SEVENTY_TWO) // (xp - 54215/72)
                    .multiply(TWO_OVER_NINE) // * 2/9
                    .sqrt(MC) // ^0.5
                    .add(THREE_HUNDRED_TWENTY_FIVE_OVER_EIGHTEEN) // + 325/18
                    .round(MC_INT)
                    .intValue();
        }
    }

    /**
     * Gets the amount of experience in <code>level</code><br>
     * This differs from {@link #getExpOfLevels(int)} in that it only gives the exp of level <code>level</code>.<br>
     * Will return 0 when given an invalid (<=0) level
     * @param level the level you want the experience of
     * @return the amount of experience in that level
     */
    public static int getExpOfLevel(int level) {
        // Get the exp of the current level, not the one after it.
        level--;
        if (level >= 0 && level <= 15) {
            return (2 * level) + 7;
        } else if (level > 15 && level <= 30) {
            return (5 * level) - 38;
        } else if (level > 0){ // Catch negative values
            return (9 * level) - 158;
        } else return 0; // Value is invalid, return zero.
        /*TEST
         *  assert getExpOfLevel(1) == 7;
         *  assert getExpOfLevel(30) == 107;
         *  assert getExpOfLevel(0) == 0;
         *  assert getExpOfLevel(-1) == 0;
         */
    }

    /**
     * Gets the amount of experience needed to reach level <code>level</code><br>
     * This differs from {@link #getExpOfLevel(int)} in that it gives the amount of exp needed to reach level <code>level</code>.<br>
     * @param level the level you want the total exp of
     * @return the amount of experience needed to get to that level
     */
    // Yes, this uses some double math; BUT, I've manually tested it with levels 0-60 and it hasn't had any trouble
    public static int getExpOfLevels(int level) {
        if (level <= 0) { //if the level is 0 or negative, return 0
            return 0;
        }
        if (level <= 16) { // 0 < level <= 16
            // level^2 + 6*level
            return (level * level) + (6 * level);
        }
        if (level <= 31) { // 16 < level <= 31
            // 2.5 * level^2 - 40.5 * level + 360
            return (int) (Math.round(2.5 * (level * level) ) - (40.5 * level) + 360);
        } else { // 31 < level
            // 4.5 * level^2 - 162.5 * level + 2200
            return (int) (Math.round(4.5 * (level * level) ) - (162.5 * level) + 2220);
        }
        /*TEST
         *  assert getExpOfLevels(1) == 7;
         *  assert getExpOfLevels(30) == 1395;
         *  assert getExpOfLevels(0) == 0;
         *  assert getExpOfLevels(-1) == 0;
         */
    }

    /**
     * Gets the total experience of the player's top <code>levels</code> levels
     * <br>
     * This method has an O(n) time complexity.
     * <br>
     * If you want the total experience of the player, use {@link #getTotalExperience(Player)}}
     * <br>
     * <br>
     * I.e. Player has 30 levels and an additional 5 experience points.
     * <br>
     * This method is called with an argument of 2.
     * <br>
     * Returns the exp in level 30 + level 29 (107 + 102)
     * @param levels The number of levels to get the total experience of
     * @return The experience in the sum of <code>levels</code>
     */
    public static int getExpOfTopLevels(Player player, int levels) {
        return getExpOfLevels(player.getLevel()) - getExpOfLevels(player.getLevel() - levels);

        /*TEST
         * // Not sure how to test this, since a player is required to instantiate ExperienceManager
         * // Assume player has 30 levels for the sake of getting this onto paper
         * assert getExpOfTopLevels(1) == (107);
         * assert getExpOfTopLevels(3) == (107 + 102 + 97);
         * assert getExpOfTopLevels(39) == 1395; // Sum of levels 0-30, going over player exp is handled gracefully.
         * assert getExpOfTopLevels(-1) == 0;
         * assert getExpOfTopLevels(0) == 0;
         */
    }
}
