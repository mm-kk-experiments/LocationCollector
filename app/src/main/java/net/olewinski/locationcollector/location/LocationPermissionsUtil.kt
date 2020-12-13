package net.olewinski.locationcollector.location

import android.Manifest
import android.content.Context
import android.os.Build
import net.olewinski.locationcollector.util.checkAllPermissionsGranted
import net.olewinski.locationcollector.util.checkSinglePermissionGranted

private val standardPermissionsToCheck = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)

fun Context.checkAllLocationPermissionsGranted() = checkAllPermissionsGranted(standardPermissionsToCheck)
        && (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || checkSinglePermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
