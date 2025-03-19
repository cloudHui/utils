package utils.other;

public class HexUtils {

    private static final int[] DEC = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13,
            14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15};

    public static byte[] decodeHex(String input) {
        if(input == null) {
            return null;
        } else if((input.length() & 1) == 1) {
            // 肯定是偶数位，奇数位转换失败：一个byte对应两个字符
            throw new IllegalArgumentException("hexUtils.fromHex.oddDigits");
        } else {

            int l = input.length();
            byte[] data = new byte[l / 2];
            for (int i = 0; i < l; i += 2) {
                data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                        + Character.digit(input.charAt(i + 1), 16));
            }
            return data;

//            char[] inputChars = input.toCharArray();
//            byte[] result = new byte[input.length() >> 1];
//
//            for(int i = 0; i < result.length; ++i) {
//                int upperNibble = getDec(inputChars[2 * i]);
//                int lowerNibble = getDec(inputChars[2 * i + 1]);
//                if(upperNibble < 0 || lowerNibble < 0) {
//                    // 字符不存在
//                    throw new IllegalArgumentException("hexUtils.fromHex.nonHex");
//                }
//
//                result[i] = (byte)((upperNibble << 4) + lowerNibble);
//            }
//
//            return result;
        }
    }
    /**
     * 字符获取十进制
     * @param index
     * @return
     */
    public static int getDec(int index) {
        try {
            return DEC[index - 48];
        } catch (ArrayIndexOutOfBoundsException var2) {
            return -1;
        }
    }

    public static byte[] decodeHex(final char[] data) throws Exception {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    public static int toDigit(final char ch, final int index) throws Exception {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
