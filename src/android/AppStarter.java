package com.fatlirmorina.cordova.plugin.autostart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import com.fatlirmorina.cordova.plugin.autostart.AutoStart;
import com.fatlirmorina.cordova.plugin.autostart.FireOSDetector;
// import android.util.Log;

public class AppStarter {

    public static final int BYPASS_USERPRESENT_MODIFICATION = -1;
    private static final String CORDOVA_AUTOSTART = "cordova_autostart";

    public void run(Context context, Intent intent, int componentState) {
	     this.run(context, intent, componentState, false);
    }

    public void run(Context context, Intent intent, int componentState, boolean onAutostart) {
        // Check if we're on Fire OS and add specific handling
        boolean isFireOS = FireOSDetector.isFireOS();
        boolean isFireTV = FireOSDetector.isFireTV(context);
        
        // Enable logging for debugging Fire OS issues
        // Log.d("Cordova AppStarter", "=== AUTOSTART DEBUG INFO ===");
        // Log.d("Cordova AppStarter", "Device: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL);
        // Log.d("Cordova AppStarter", "Fire OS Device: " + FireOSDetector.getFireOSDeviceType(context));
        // Log.d("Cordova AppStarter", "Is Fire OS: " + isFireOS + ", Is Fire TV: " + isFireTV);
        // Log.d("Cordova AppStarter", "Intent Action: " + (intent != null ? intent.getAction() : "null"));
        // Log.d("Cordova AppStarter", "Component State: " + componentState + ", On Autostart: " + onAutostart);
        
        // Enable or Disable UserPresentReceiver (or bypass the modification)
        if( componentState != BYPASS_USERPRESENT_MODIFICATION ) {
            ComponentName receiver = new ComponentName(context, UserPresentReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver, componentState, PackageManager.DONT_KILL_APP);
        }

        // Starting your app...
        SharedPreferences sp = context.getSharedPreferences(AutoStart.PREFS, Context.MODE_PRIVATE);
        String packageName = context.getPackageName();
        String activityClassName = sp.getString(AutoStart.ACTIVITY_CLASS_NAME, "");
        
        // Log.d("Cordova AppStarter", "Package: " + packageName + ", Activity: " + activityClassName);
        
        if( !activityClassName.equals("") ){
            // For Fire OS, try multiple launch strategies
            if (isFireOS) {
                boolean launched = false;
                
                // Strategy 1: Try package manager launch intent first (most reliable for Fire OS)
                try {
                    // Log.d("Cordova AppStarter", "Fire OS: Trying package manager launch intent");
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        
                        if (isFireTV) {
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            launchIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                        }
                        
                        if (onAutostart) {
                            launchIntent.putExtra(CORDOVA_AUTOSTART, true);
                        }
                        launchIntent.putExtra("FIRE_OS_AUTOSTART", true);
                        launchIntent.putExtra("FIRE_OS_LAUNCH_METHOD", "package_manager");
                        launchIntent.putExtra("FIRE_OS_DEVICE_MODEL", android.os.Build.MODEL);
                        
                        context.startActivity(launchIntent);
                        launched = true;
                        // Log.d("Cordova AppStarter", "Fire OS: Package manager launch successful");
                    }
                } catch (Exception e) {
                    // Log.e("Cordova AppStarter", "Fire OS: Package manager launch failed", e);
                }
                
                // Strategy 2: Try explicit activity name (fallback)
                if (!launched) {
                    try {
                        // Log.d("Cordova AppStarter", "Fire OS: Trying explicit activity launch");
                        Intent activityIntent = new Intent();
                        activityIntent.setClassName(context, String.format("%s.%s", packageName, activityClassName));
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        
                        if (isFireTV) {
                            activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            activityIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            activityIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                        }
                        
                        if (onAutostart) {
                            activityIntent.putExtra(CORDOVA_AUTOSTART, true);
                        }
                        activityIntent.putExtra("FIRE_OS_AUTOSTART", true);
                        activityIntent.putExtra("FIRE_OS_LAUNCH_METHOD", "explicit_activity");
                        activityIntent.putExtra("FIRE_OS_DEVICE_MODEL", android.os.Build.MODEL);
                        
                        context.startActivity(activityIntent);
                        launched = true;
                        // Log.d("Cordova AppStarter", "Fire OS: Explicit activity launch successful");
                        
                    } catch (Exception e) {
                        // Log.e("Cordova AppStarter", "Fire OS: Explicit activity launch failed", e);
                    }
                }
                
                // Strategy 3: Try with ACTION_MAIN intent (last resort)
                if (!launched) {
                    try {
                        // Log.d("Cordova AppStarter", "Fire OS: Trying ACTION_MAIN intent");
                        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
                        mainIntent.setClassName(context, String.format("%s.%s", packageName, activityClassName));
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        
                        if (isFireTV) {
                            mainIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                        }
                        
                        if (onAutostart) {
                            mainIntent.putExtra(CORDOVA_AUTOSTART, true);
                        }
                        mainIntent.putExtra("FIRE_OS_AUTOSTART", true);
                        mainIntent.putExtra("FIRE_OS_LAUNCH_METHOD", "action_main");
                        mainIntent.putExtra("FIRE_OS_DEVICE_MODEL", android.os.Build.MODEL);
                        
                        context.startActivity(mainIntent);
                        // Log.d("Cordova AppStarter", "Fire OS: ACTION_MAIN launch successful");
                        
                    } catch (Exception e) {
                        // Log.e("Cordova AppStarter", "Fire OS: All launch strategies failed", e);
                    }
                }
                
            } else {
                // Standard Android launch
                try {
                    Intent activityIntent = new Intent();
                    activityIntent.setClassName(context, String.format("%s.%s", packageName, activityClassName));
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    
                    if (onAutostart) {
                        activityIntent.putExtra(CORDOVA_AUTOSTART, true);
                    }
                    
                    context.startActivity(activityIntent);
                    // Log.d("Cordova AppStarter", "Standard Android: Activity started successfully");
                    
                } catch (Exception e) {
                    // Log.e("Cordova AppStarter", "Standard Android: Failed to start activity", e);
                }
            }
        }
        
        // Start a service in the background.
        String serviceClassName = sp.getString(AutoStart.SERVICE_CLASS_NAME, "");
        if ( !serviceClassName.equals("") ) {
            try {
                Intent serviceIntent = new Intent();
                serviceIntent.setClassName(context, serviceClassName);
                
                // Fire OS specific service handling
                if (isFireOS) {
                    serviceIntent.putExtra("FIRE_OS_AUTOSTART", true);
                    serviceIntent.putExtra("FIRE_OS_DEVICE_TYPE", FireOSDetector.getFireOSDeviceType(context));
                    serviceIntent.putExtra("FIRE_OS_DEVICE_MODEL", android.os.Build.MODEL);
                }
                
                if ( onAutostart ) {
                    serviceIntent.putExtra(CORDOVA_AUTOSTART, true);
                }
                
                context.startService(serviceIntent);
                // Log.d("Cordova AppStarter", "Service started successfully");
                
            } catch (Exception e) {
                // Log.e("Cordova AppStarter", "Failed to start service", e);
            }
        }

    }
}
