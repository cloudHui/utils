package utils.other;

public class GPSUtils {
    private static final double EARTH_RADIUS = 6378137.0D;

    public GPSUtils() {
    }

    private static double rad(double d) {
        return d * 3.141592653589793D / 180.0D;
    }

    public static double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        if (lat1 == lat2 && lng1 == lng2) {
            return 0.0D;
        } else {
            double radLat1 = rad(lat1);
            double radLat2 = rad(lat2);
            double a = radLat1 - radLat2;
            double b = rad(lng1) - rad(lng2);
            double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0D), 2.0D) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2.0D), 2.0D)));
            return (double)Math.abs(Math.round(s * 6378137.0D * 10000.0D) / 10000L);
        }
    }
}
