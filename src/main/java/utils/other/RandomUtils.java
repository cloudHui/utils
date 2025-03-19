package utils.other;

import java.util.Random;

public class RandomUtils {
    private final static Random random = new Random(System.currentTimeMillis());

    public static int randomRangeObtain(int min, int max) {
        return random(min, max, random);
    }

    public static int randomRange(int bound) {
        return random.nextInt(bound);
    }

    public static int random(int min, int max, Random r) {
        return (int) (r.nextDouble() * (max - min + 1)) + min;
    }

    /**
     * 符合随机概率
     *
     * @param rate 概率的整数
     * @return true 符合 false 不符合
     */
    public static boolean fitRandomRate(int rate) {
        //随机产生[0,100)的整数，每个数字出现的概率为1%
        int num = random.nextInt(100);
        //前20个数字的区间，代表20%的几率
        return num <= rate;
    }


    /**
     * 乱序byte
     */
    public static void randomByte(byte[] bytes) {
        random.nextBytes(bytes);
    }
}
