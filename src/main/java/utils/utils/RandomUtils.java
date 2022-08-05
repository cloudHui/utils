package utils.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random(System.nanoTime());

    public RandomUtils() {
    }

    public static int Random(int min, int max) {
        return Random(min, max, random);
    }

    public static int Random(int min, int max, Random r) {
        return (int)(r.nextDouble() * (double)(max - min + 1)) + min;
    }

    public static long Random(long min, long max) {
        return Random(min, max, random);
    }

    public static long Random(long min, long max, Random r) {
        return (long)(r.nextDouble() * (double)(max - min + 1L)) + min;
    }
}
