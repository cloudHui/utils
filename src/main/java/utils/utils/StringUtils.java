package utils.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringUtils {
    private static final String REGEX = ",";

    public StringUtils() {
    }

    public static boolean isNullOrEmpty(String data) {
        return null == data || data.isEmpty();
    }

    public static List<Long> splitLong(String data) {
        return splitLong(data, ",");
    }

    public static List<Long> splitLong(String data, String regex) {
        if (isNullOrEmpty(data)) {
            return new ArrayList<>();
        } else {
            String[] s = data.split(regex);
            List<Long> r = new ArrayList<>(s.length);
            int i = 0;

            for(int size = s.length; i < size; ++i) {
                try {
                    r.add(Long.parseLong(s[i]));
                } catch (Exception ignored) {
                }
            }

            return r;
        }
    }

    public static List<Integer> splitInt(String data) {
        return splitInt(data, ",");
    }

    public static List<Integer> splitInt(String data, String regex) {
        if (isNullOrEmpty(data)) {
            return new ArrayList<>();
        } else {
            String[] s = data.split(regex);
            List<Integer> r = new ArrayList<>(s.length);
            int i = 0;

            for(int size = s.length; i < size; ++i) {
                try {
                    r.add(Integer.parseInt(s[i]));
                } catch (Exception ignored) {
                }
            }

            return r;
        }
    }

    public static int[] splitArrayInt(String data) {
        return splitArrayInt(data, ",");
    }

    public static int[] splitArrayInt(String data, String regex) {
        if (isNullOrEmpty(data)) {
            return null;
        } else {
            String[] s = data.split(regex);
            int[] r = new int[s.length];
            int i = 0;

            for(int size = s.length; i < size; ++i) {
                try {
                    r[i] = Integer.parseInt(s[i]);
                } catch (Exception ignored) {
                }
            }

            return r;
        }
    }

    public static List<Float> splitFloat(String data) {
        return splitFloat(data, ",");
    }

    public static List<Float> splitFloat(String data, String regex) {
        if (isNullOrEmpty(data)) {
            return new ArrayList<>();
        } else {
            String[] s = data.split(regex);
            List<Float> r = new ArrayList<>(s.length);
            int i = 0;

            for(int size = s.length; i < size; ++i) {
                try {
                    r.add(Float.parseFloat(s[i]));
                } catch (Exception ignored) {
                }
            }

            return r;
        }
    }

    public static List<Double> splitDouble(String data) {
        return splitDouble(data, ",");
    }

    public static List<Double> splitDouble(String data, String regex) {
        if (isNullOrEmpty(data)) {
            return new ArrayList<>();
        } else {
            String[] s = data.split(regex);
            List<Double> r = new ArrayList<>(s.length);
            int i = 0;

            for(int size = s.length; i < size; ++i) {
                try {
                    r.add(Double.parseDouble(s[i]));
                } catch (Exception ignored) {
                }
            }

            return r;
        }
    }

    public static List<String> splitString(String data) {
        return splitString(data, ",");
    }

    public static List<String> splitString(String data, String regex) {
        if (isNullOrEmpty(data)) {
            return new ArrayList<>();
        } else {
            String[] s = data.split(regex);
            List<String> r = new ArrayList<>(s.length);
            int i = 0;

            for(int size = s.length; i < size; ++i) {
                try {
                    r.add(s[i]);
                } catch (Exception ignored) {
                }
            }

            return r;
        }
    }

    public static <T> String listToString(List<T> data) {
        if (null != data && !data.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (T o : data) {
                sb.append(String.format("%s,", o.toString()));
            }

            return sb.substring(0, sb.length() - 1);
        } else {
            return null;
        }
    }
}
