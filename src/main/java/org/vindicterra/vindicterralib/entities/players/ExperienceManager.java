package org.vindicterra.vindicterralib.entities.players;
/*
AUTHOR: Dev_Richard (https://www.spigotmc.org/members/dev_richard.38792/)
DESC: A simple and easy to use class that can get and set a player's total experience points.

Feel free to use this class in both public and private plugins, however if you release your
plugin please link to this gist publicly so that others can contribute and benefit from it.
*/

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.bukkit.entity.Player;
@SuppressWarnings("unused")
public class ExperienceManager {

    private final Player player;

    public ExperienceManager(Player player) {
        this.player = player;
    }

    /**
     * Gets the total experience points of the Player
     * @return the player's total experience
     */
    public int getTotalExperience() {
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
                .setScale(0, RoundingMode.HALF_UP)
                // Convert to an int for summation
                .intValue();
        return experience;
    }

    /**
     * Sets the experience of the Player
     * @param xp experience points to set
     */
    public void setTotalExperience(int xp) {
        var playerExp = convertExp(xp);
        player.setLevel(playerExp.level);
        player.setExp(playerExp.progress);
    }

    @Deprecated // changed signature of old method so dependencies default to new method
    public int getTotalExperience(Player player) {
        int experience;
        int level = player.getLevel();
        if(level >= 0 && level <= 15) {
            experience = (int) Math.ceil(Math.pow(level, 2) + (6 * level));
            int requiredExperience = 2 * level + 7;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += (int) Math.ceil(currentExp * requiredExperience);
            return experience;
        } else if(level > 15 && level <= 30) {
            experience = (int) Math.ceil((2.5 * Math.pow(level, 2) - (40.5 * level) + 360));
            int requiredExperience = 5 * level - 38;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += (int) Math.ceil(currentExp * requiredExperience);
            return experience;
        } else {
            experience = (int) Math.ceil(((4.5 * Math.pow(level, 2) - (162.5 * level) + 2220)));
            int requiredExperience = 9 * level - 158;
            double currentExp = Double.parseDouble(Float.toString(player.getExp()));
            experience += (int) Math.ceil(currentExp * requiredExperience);
            return experience;
        }
    }

    @Deprecated
    public void setTotalExperience(Player player,int xp) {
        //Levels 0 through 15
        if(xp >= 0 && xp < 351) {
            //Calculate Everything
            int a = 1; int b = 6; int c = -xp;
            int level = (int) (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
            int xpForLevel = (int) (Math.pow(level, 2) + (6 * level));
            int remainder = xp - xpForLevel;
            int experienceNeeded = (2 * level) + 7;
            float experience = (float) remainder / (float) experienceNeeded;
            experience = round(experience, 2);

            //Set Everything
            player.setLevel(level);
            player.setExp(experience);
            //Levels 16 through 30
        } else if(xp >= 352 && xp < 1507) {
            //Calculate Everything
            double a = 2.5; double b = -40.5; int c = -xp + 360;
            double dLevel = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
            int level = (int) Math.floor(dLevel);
            int xpForLevel = (int) (2.5 * Math.pow(level, 2) - (40.5 * level) + 360);
            int remainder = xp - xpForLevel;
            int experienceNeeded = (5 * level) - 38;
            float experience = (float) remainder / (float) experienceNeeded;
            experience = round(experience, 2);

            //Set Everything
            player.setLevel(level);
            player.setExp(experience);
            //Level 31 and greater
        } else {
            //Calculate Everything
            double a = 4.5; double b = -162.5; int c = -xp + 2220;
            double dLevel = (-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a);
            int level = (int) Math.floor(dLevel);
            int xpForLevel = (int) (4.5 * Math.pow(level, 2) - (162.5 * level) + 2220);
            int remainder = xp - xpForLevel;
            int experienceNeeded = (9 * level) - 158;
            float experience = (float) remainder / (float) experienceNeeded;
            experience = round(experience, 2);

            //Set Everything
            player.setLevel(level);
            player.setExp(experience);
        }
    }
    @SuppressWarnings("SameParameterValue")
    private float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_DOWN);
        return bd.floatValue();
    }

    /**
     * A storage object that contains API-ready data about a player's levels and experience
     */
    // TODO Fuck you I'm not making getters and setters (right now) this is *so* much better (i'll enjoy it while I can TwT)
    public static class PlayerExp {
        /**
         * The player's level
         */
        final int level;
        /**
         * The player's experience to the next level
         */
        final int exp;
        /**
         * A 0-1 range of the player's progress to the next level
         */
        final float progress;
        PlayerExp(int l, int e, float p){
            level = l;
            exp = e;
            progress = p;
        }
    }

    // TODO check what precision is actually needed of MathContext
    private static final MathContext mc = new MathContext(8);
    /**
     * Converts an exp value into a PlayerExp object containing level information
     * @see PlayerExp
     * @param xp the exp value to work off of
     * @return A PlayerExp object with API-ready data
     */
    public static PlayerExp convertExp(int xp) {
        int levels;
        if(xp >= 0 && xp <= 352) {
            // sqrt(xp+9) - 3
            levels = BigDecimal.valueOf(xp + 9)
                    .sqrt(mc)
                    .subtract( BigDecimal.valueOf(3))
                    .setScale(0, RoundingMode.HALF_DOWN)
                    .intValue();
        // Oh my god this is a shitshow
        } else if(xp >= 353 && xp <= 1507) {
            // 81/10 + sqrt(2/5*(xp - 7839/40))
            levels = BigDecimal.valueOf(xp)
                    .subtract(
                            BigDecimal.valueOf(7839)
                            .divide(BigDecimal.valueOf(40), mc) )
                    .multiply(
                            BigDecimal.valueOf(2)
                            .divide(BigDecimal.valueOf(5), mc) )
                    .sqrt(mc)
                    .add(
                            BigDecimal.valueOf(81)
                            .divide(BigDecimal.valueOf(10), mc) )
                    .setScale(0, RoundingMode.HALF_DOWN)
                    .intValue();
        // I hate doing complex math in this godforsaken language
        } else {
            // 325/18 + sqrt(2/9*(xp - 54215/72)
            levels = BigDecimal.valueOf(xp)
                    .subtract(
                            BigDecimal.valueOf(54215)
                                    .divide(BigDecimal.valueOf(72), mc) )
                    .multiply(
                            BigDecimal.valueOf(2)
                                    .divide(BigDecimal.valueOf(9), mc) )
                    .sqrt(mc)
                    .add(
                            BigDecimal.valueOf(325)
                                    .divide(BigDecimal.valueOf(18), mc) )
                    .setScale(0, RoundingMode.HALF_DOWN)
                    .intValue();
        }

        // Get leftover exp points
        int remainder = xp - getExpOfLevels(levels);
        // Get exp needed for the next level
        int neededExp = getExpOfLevel(levels + 1);
        // Calculate 0-1 range to next level
        float progress = BigDecimal.valueOf(remainder)
                .divide( BigDecimal.valueOf(neededExp), mc)
                .floatValue();
        return new PlayerExp(levels, remainder, progress);
    }

    /**
     * Deprecated compatibility method.  Use {@link #getExpOfLevel(int)} instead
     */
    @Deprecated
    public static int getExperienceInLevel(int level){
        return getExpOfLevel(level);
    }

    /**
     * Deprecated compatibility method.  Use {@link #getExpOfTopLevels(int)} instead
     */
    @Deprecated
    public int getExperienceInLevels(int levels){
        return getExpOfTopLevels(levels);
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
        level -= 1;
        if (level >= 0 && level <= 15) {
            return (2 * level) + 7;
        } else if (level > 15 && level <= 30) {
            return (5 * level) - 38;
        } else if (level > 0){ // Catch negative values
            return (9 * level) - 158;
        } else return 0; // Value is invalid, return zero.
        /**TEST
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
        if (level >= 0 && level <= 16) {
            // level^2 + 6*level
            return (level * level) + (6 * level);
        } else if (level >= 17 && level <= 31) {
            // 2.5 * level^2 - 40.5 * level + 360
            return (int) (Math.ceil(2.5 * (level * level) ) - (40.5 * level) + 360);
        } else if (level > 0) { // Catch negative values
            // 4.5 * level^2 - 162.5 * level + 2200
            return (int) (Math.ceil(4.5 * (level * level) ) - (162.5 * level) + 2220);
        } else return 0; // Value is invalid, return zero.
        /**TEST
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
     * If you want the total experience of the player, use {@link #getTotalExperience()}
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
    public int getExpOfTopLevels(int levels) {
        // Kinda scuffed that this all works as normal
        int sum = 0;
        for (int i = this.player.getLevel(); i > this.player.getLevel() - levels; i--){
            sum += getExpOfLevel(i);
        }
        return sum;
        /**TEST
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
