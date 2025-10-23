package com.fatlirmorina.cordova.plugin.autostart;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class FireOSDetector {
    
    private static final String AMAZON_FEATURE = "amazon.hardware.fire_tv";
    private static final String AMAZON_MANUFACTURER = "Amazon";
    
    // Fire TV device models
    private static final String[] FIRE_TV_MODELS = {
        "AFTKM",     // Fire TV Stick 4K Max
        "AFTKA",     // Fire TV Stick 4K
        "AFTKB",     // Fire TV Stick 4K (newer version) 
        "AFTKMM",    // Fire TV Stick 4K Max (2023)
        "AFTMM",     // Fire TV Stick (3rd Gen)
        "AFTRS",     // Fire TV Stick (2nd Gen)
        "AFTT",      // Fire TV Stick (1st Gen)
        "AFTM",      // Fire TV (2nd Gen)
        "AFTB",      // Fire TV (1st Gen)
        "AFTS",      // Fire TV Stick Basic Edition
        "AFTN",      // Fire TV Stick Lite
        "AFTSS",     // Fire TV Stick (International)
        "AFTEU",     // Fire TV Cube (1st Gen)
        "AFTR",      // Fire TV Cube (2nd Gen)
        "AFTKA007",  // Fire TV Stick 4K (variant)
        "AFTBAMR311" // Fire TV Edition Smart TV
    };
    
    /**
     * Detects if the device is running Amazon Fire OS
     */
    public static boolean isFireOS() {
        return AMAZON_MANUFACTURER.equals(Build.MANUFACTURER) || 
               isFireTVByModel() || 
               hasFireOSProperties();
    }
    
    /**
     * Check for Fire OS specific system properties
     */
    private static boolean hasFireOSProperties() {
        try {
            String osName = System.getProperty("os.name", "").toLowerCase();
            String javaVendor = System.getProperty("java.vendor", "").toLowerCase();
            return osName.contains("fire") || javaVendor.contains("amazon");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if device model matches known Fire TV models
     */
    private static boolean isFireTVByModel() {
        String model = Build.MODEL;
        if (model != null) {
            for (String fireModel : FIRE_TV_MODELS) {
                if (model.equals(fireModel) || model.startsWith(fireModel)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Detects if the device is a Fire TV
     */
    public static boolean isFireTV(Context context) {
        if (!isFireOS()) {
            return false;
        }
        
        // Check by model first (more reliable for some devices)
        if (isFireTVByModel()) {
            return true;
        }
        
        // Check system feature
        try {
            PackageManager pm = context.getPackageManager();
            return pm.hasSystemFeature(AMAZON_FEATURE) ||
                   pm.hasSystemFeature("android.hardware.type.television") ||
                   pm.hasSystemFeature("android.software.leanback");
        } catch (Exception e) {
            return false;
        }
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
            return "Fire TV (" + Build.MODEL + ")";
        } else {
            return "Fire Tablet (" + Build.MODEL + ")";
        }
    }
}