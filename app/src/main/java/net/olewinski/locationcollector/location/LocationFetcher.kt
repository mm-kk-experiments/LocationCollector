package net.olewinski.locationcollector.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException

private const val UPDATE_INTERVAL_MILLISECONDS = 10000L
private const val FASTEST_UPDATE_INTERVAL_MILLISECONDS = 5000L
private const val LOCATION_REQUEST_EXPIRATION_TIMEOUT_MILLISECONDS = 180000L

class PermissionsDeniedException : Exception()

class LocationFetcher(
    context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    companion object {
        private val LOG_TAG = LocationFetcher::class.java.simpleName
    }

    private val applicationContext = context.applicationContext

    // Suppressed because permission check is in fact done, but as it's performed by dedicated function, Lint can't see this.
    @SuppressLint("MissingPermission")
    suspend fun fetchLocation(): Location = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { cancellableContinuation ->
            Log.d(LOG_TAG, "Fetching location requested")

            if (applicationContext.checkAllLocationPermissionsGranted()) {
                Log.d(LOG_TAG, "All location permissions granted, proceeding with location fetch")

                val locationRequest = LocationRequest().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = UPDATE_INTERVAL_MILLISECONDS
                    fastestInterval = FASTEST_UPDATE_INTERVAL_MILLISECONDS
                    setExpirationDuration(LOCATION_REQUEST_EXPIRATION_TIMEOUT_MILLISECONDS)
                }

                val locationCallback = object : LocationCallback() {
                    private var locationUpdateNumber = 0

                    override fun onLocationResult(locationResult: LocationResult) {
                        if (locationUpdateNumber < 1) {
                            Log.d(
                                LOG_TAG,
                                "Ignoring first location update: ${locationResult.lastLocation}"
                            )

                            ++locationUpdateNumber

                            return
                        }

                        Log.d(LOG_TAG, "Accepting location update: ${locationResult.lastLocation}")

                        fusedLocationProviderClient.removeLocationUpdates(this)
                        cancellableContinuation.resumeWith(Result.success(locationResult.lastLocation))
                    }
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                ).addOnFailureListener { exception ->
                    Log.w(LOG_TAG, "Exception while fetching location", exception)

                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    cancellableContinuation.resumeWithException(exception)
                }

                cancellableContinuation.invokeOnCancellation { throwable ->
                    Log.d(
                        LOG_TAG,
                        "Removing location updates callback upon cancellation",
                        throwable
                    )

                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
            } else {
                val permissionsDeniedException = PermissionsDeniedException()

                Log.w(
                    LOG_TAG,
                    "Lack of required permissions, aborting location fetch",
                    permissionsDeniedException
                )

                cancellableContinuation.resumeWithException(permissionsDeniedException)
            }
        }
    }
}
