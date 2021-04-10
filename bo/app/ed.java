package bo.app;

public final class ed
{
    public static double a(double radians, double radians2, double radians3, final double n) {
        final double radians4 = Math.toRadians(radians3 - radians);
        radians2 = Math.toRadians(n - radians2);
        radians = Math.toRadians(radians);
        radians3 = Math.toRadians(radians3);
        return Math.asin(Math.sqrt(Math.pow(Math.sin(radians4 / 2.0), 2.0) + Math.pow(Math.sin(radians2 / 2.0), 2.0) * Math.cos(radians) * Math.cos(radians3))) * 2.0 * 6371000.0;
    }
}
