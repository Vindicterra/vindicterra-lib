package org.vindicterra.vindicterralib.utils;

@SuppressWarnings("unused")
public class MathUtils {
    /**
     * Clamps a value between a minimum and maximum value
     * <br>
     * <br>
     * Clamped values will not go below or above the specified min and max values
     * @param value Integer value to clamp
     * @param min Minimum value allowed
     * @param max Maximum value allowed
     * @return Clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamps a value between a minimum and maximum value
     * <br>
     * <br>
     * Clamped values will not go below or above the specified min and max values
     * @param value Double value to clamp
     * @param min Minimum value allowed
     * @param max Maximum value allowed
     * @return Clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
