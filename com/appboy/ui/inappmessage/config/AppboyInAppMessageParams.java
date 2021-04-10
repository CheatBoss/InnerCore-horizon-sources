package com.appboy.ui.inappmessage.config;

public class AppboyInAppMessageParams
{
    public static final double GRAPHIC_MODAL_MAX_HEIGHT_DP = 290.0;
    public static final double GRAPHIC_MODAL_MAX_WIDTH_DP = 290.0;
    public static final double MODALIZED_IMAGE_RADIUS_DP = 9.0;
    private static double sGraphicModalMaxHeightDp = 290.0;
    private static double sGraphicModalMaxWidthDp = 290.0;
    private static double sModalizedImageRadiusDp = 9.0;
    
    public static double getGraphicModalMaxHeightDp() {
        return AppboyInAppMessageParams.sGraphicModalMaxHeightDp;
    }
    
    public static double getGraphicModalMaxWidthDp() {
        return AppboyInAppMessageParams.sGraphicModalMaxWidthDp;
    }
    
    public static double getModalizedImageRadiusDp() {
        return AppboyInAppMessageParams.sModalizedImageRadiusDp;
    }
    
    public static void setGraphicModalMaxHeightDp(final double sGraphicModalMaxHeightDp) {
        AppboyInAppMessageParams.sGraphicModalMaxHeightDp = sGraphicModalMaxHeightDp;
    }
    
    public static void setGraphicModalMaxWidthDp(final double sGraphicModalMaxWidthDp) {
        AppboyInAppMessageParams.sGraphicModalMaxWidthDp = sGraphicModalMaxWidthDp;
    }
    
    public static void setModalizedImageRadiusDp(final double sModalizedImageRadiusDp) {
        AppboyInAppMessageParams.sModalizedImageRadiusDp = sModalizedImageRadiusDp;
    }
}
