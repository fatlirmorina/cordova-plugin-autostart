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
            try {
                Intent activityIntent = new Intent();
                activityIntent.setClassName(
                    context, String.format("%s.%s", packageName, activityClassName));
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                // Fire OS specific handling
                if (isFireOS) {
                    // For Fire TV, add specific flags for better autostart behavior
                    if (isFireTV) {
                        // Fire TV specific flags
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        
                        // Additional Fire TV specific intent setup
                        activityIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        activityIntent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
                    }
                    
                    // Add Fire OS identifier to intent
                    activityIntent.putExtra("FIRE_OS_AUTOSTART", true);
                    activityIntent.putExtra("FIRE_OS_DEVICE_TYPE", FireOSDetector.getFireOSDeviceType(context));
                    activityIntent.putExtra("FIRE_OS_DEVICE_MODEL", android.os.Build.MODEL);
                }
                
                if (onAutostart) {
                  activityIntent.putExtra(CORDOVA_AUTOSTART, true);
                }
                
                // Log.d("Cordova AppStarter", "Starting activity with intent: " + activityIntent.toString());
                context.startActivity(activityIntent);
                // Log.d("Cordova AppStarter", "Activity started successfully");
                
            } catch (Exception e) {
                // Log.e("Cordova AppStarter", "Failed to start activity", e);
                
                // Fallback: Try starting with just the package name (Fire OS sometimes needs this)
                if (isFireOS) {
                    try {
                        // Log.d("Cordova AppStarter", "Trying Fire OS fallback method");
                        Intent fallbackIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                        if (fallbackIntent != null) {
                            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            
                            if (onAutostart) {
                                fallbackIntent.putExtra(CORDOVA_AUTOSTART, true);
                            }
                            fallbackIntent.putExtra("FIRE_OS_AUTOSTART", true);
                            fallbackIntent.putExtra("FIRE_OS_FALLBACK", true);
                            
                            context.startActivity(fallbackIntent);
                            // Log.d("Cordova AppStarter", "Fire OS fallback successful");
                        }
                    } catch (Exception fallbackException) {
                        // Log.e("Cordova AppStarter", "Fire OS fallback also failed", fallbackException);
                    }
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
