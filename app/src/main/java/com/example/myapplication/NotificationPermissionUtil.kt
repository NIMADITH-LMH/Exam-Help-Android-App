package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Utility class for handling notification permissions.
 */
object NotificationPermissionUtil {
    
    /**
     * Register for notification permission request result
     */
    fun registerForPermissionResult(activity: AppCompatActivity, onGranted: () -> Unit, onDenied: () -> Unit): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }
    }
    
    /**
     * Check if notification permission is granted
     */
    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == 
                    PackageManager.PERMISSION_GRANTED
        } else {
            true // For versions below Android 13, notification permission is granted by default
        }
    }
    
    /**
     * Request notification permission if needed
     */
    fun requestNotificationPermissionIfNeeded(
        context: Context,
        permissionLauncher: ActivityResultLauncher<String>
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isNotificationPermissionGranted(context)) {
                true
            } else {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                false
            }
        } else {
            true // Already granted for older versions
        }
    }
}
