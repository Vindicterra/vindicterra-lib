package org.vindicterra.vindicterralib.entities.players;
/*
AUTHOR: Dev_Richard (https://www.spigotmc.org/members/dev_richard.38792/)
DESC: A simple and easy to use class that can get and set a player's total experience points.

Feel free to use this class in both public and private plugins, however if you release your
plugin please link to this gist publicly so that others can contribute and benefit from it.
*/

import java.math.BigDecimal;
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

        /* TODO this can be done faster with (level^2 + 6 * level) ...
            getExperienceInLevels should be kept, but another helper getTotal method should be made public
        */
        // Get the amount of experience in all the player's levels
        int experience = getExpOfTopLevels(level);

        // Get the amount of exp needed for the next level
        int neededExp = getExpOfLevel(level + 1);
        // getExp returns a range of 0-1 of player's progress to the next level
        var progress = BigDecimal.valueOf(player.getExp());
        experience +=
                // Multiply level progress by level total to get current exp
                progress.multiply( BigDecimal.valueOf(neededExp) )
                // TODO check how we should be rounding
                // Round out any decimals
                .setScale(0, RoundingMode.HALF_DOWN)
                // Convert to an int for summation
                .intValue();
        return experience;
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

    public void setTotalExperience(int xp) {
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
     * Gets the amount of experience in <code>level</code>
     * <br>
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
        } else if (level > 0){ // Catch for negative values
            return (9 * level) - 158;
        } else { // Value is negative, return zero.
            return 0;
        }
        /**TEST
         *  assert getExpOfLevel(1) == 7;
         *  assert getExpOfLevel(30) == 107;
         *  assert getExpOfLevel(0) == 0;
         *  assert getExpOfLevel(-1) == 0;
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
