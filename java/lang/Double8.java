package java.lang;

public final class Double8
{
    private Double8() {
    }
    
    public static int hashCode(final double n) {
        final long doubleToLongBits = Double.doubleToLongBits(n);
        return (int)(doubleToLongBits ^ doubleToLongBits >>> 32);
    }
    
    public static boolean isFinite(final double n) {
        return Math.abs(n) <= Double.MAX_VALUE;
    }
    
    public static double max(final double n, final double n2) {
        return Math.max(n, n2);
    }
    
    public static double min(final double n, final double n2) {
        return Math.min(n, n2);
    }
    
    public static double sum(final double n, final double n2) {
        return n + n2;
    }
}
