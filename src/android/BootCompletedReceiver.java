package com.fatlirmorina.cordova.plugin.autostart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import com.fatlirmorina.cordova.plugin.autostart.AppStarter;
// import android.util.Log;
 
public class BootCompletedReceiver extends BroadcastReceiver {
    
    @Override
    public void onReceive(Context context, Intent intent) {
        
        if (intent == null || intent.getAction() == null) {
            return;
        }
        
        String action = intent.getAction();
        // Log.d("BootCompletedReceiver", "Received intent: " + action + " on device: " + android.os.Build.MODEL);
        
        // Handle boot completed events
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) || 
            Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action) ||
            "com.htc.intent.action.QUICKBOOT_POWERON".equals(action)) {
            
            // Log.d("BootCompletedReceiver", "Boot completed - checking for Fire OS");
            
            // For Fire OS devices, use a delayed handler approach instead of Thread.sleep
            // This is more reliable and doesn't block the receiver
            if (FireOSDetector.isFireOS()) {
                // Log.d("BootCompletedReceiver", "Fire OS detected (" + android.os.Build.MODEL + "), using delayed startup");
                
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Log.d("BootCompletedReceiver", "Starting Fire OS autostart after delay");
                        AppStarter appStarter = new AppStarter();
                        appStarter.run(context, intent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, true);
                    }
                }, 5000); // 5 second delay for Fire OS to ensure system is fully ready
                
                // Also try immediate start for backup (some Fire OS versions work better this way)
                try {
                    AppStarter appStarter = new AppStarter();
                    appStarter.run(context, intent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, true);
                    // Log.d("BootCompletedReceiver", "Immediate Fire OS autostart attempted");
                } catch (Exception e) {
                    // Log.e("BootCompletedReceiver", "Immediate Fire OS autostart failed", e);
                }
                
            } else {
                // Standard Android devices
                // Log.d("BootCompletedReceiver", "Standard Android device, starting immediately");
                AppStarter appStarter = new AppStarter();
                appStarter.run(context, intent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, true);
            }
        }
    }
}
