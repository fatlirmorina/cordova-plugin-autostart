package com.fatlirmorina.cordova.plugin.autostart;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class FireOSDetector {
    
    private static final String AMAZON_FEATURE = "amazon.hardware.fire_tv";
    private static final String AMAZON_MANUFACTURER = "Amazon";
    
    /**
     * Detects if the device is running Amazon Fire OS
     */
    public static boolean isFireOS() {
        return AMAZON_MANUFACTURER.equals(Build.MANUFACTURER);
    }
    
    /**
     * Detects if the device is a Fire TV
     */
    public static boolean isFireTV(Context context) {
        if (!isFireOS()) {
            return false;
        }
        
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(AMAZON_FEATURE);
    }
    
    /**
     * Detects if the device is a Fire tablet
     */
    public static boolean isFireTablet(Context context) {
        return isFireOS() && !isFireTV(context);
    }
    
    /**
     * Gets a description of the Fire OS device type
     */
    public static String getFireOSDeviceType(Context context) {
        if (!isFireOS()) {
            return "Not Fire OS";
        }
        
        if (isFireTV(context)) {
            return "Fire TV";
        } else {
            return "Fire Tablet";
        }
    }
}