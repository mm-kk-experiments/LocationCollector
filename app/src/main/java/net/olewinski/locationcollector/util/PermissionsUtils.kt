package net.olewinski.locationcollector.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.checkSinglePermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.checkAllPermissionsGranted(permissions: Array<String>): Boolean {
    permissions.forEach { permission ->
        if (!checkSinglePermissionGranted(permission)) return false
    }

    return true
}
