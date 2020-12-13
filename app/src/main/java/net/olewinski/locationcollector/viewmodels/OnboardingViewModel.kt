package net.olewinski.locationcollector.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.olewinski.locationcollector.util.OneTimeEvent

sealed class NavigationRequest

object NavigateToSplashScreen : NavigationRequest()
object NavigateToForegroundLocationPermissionRequestScreen : NavigationRequest()
object NavigateToBackgroundLocationPermissionRequestScreen : NavigationRequest()
object NavigateToSorryScreen : NavigationRequest()
object NavigateToMainScreen : NavigationRequest()
object NavigateOutOfApp: NavigationRequest()

class OnboardingViewModel : ViewModel() {

    private val mutableNavigationRequests = MutableLiveData<OneTimeEvent<NavigationRequest>>(
        OneTimeEvent(NavigateToSplashScreen)
    )
    val navigationRequests: LiveData<OneTimeEvent<NavigationRequest>> = mutableNavigationRequests

    private var accessCoarseLocationPermissionGranted = false
    private var accessFineLocationPermissionGranted = false
    private var accessBackgroundLocationPermissionGranted = false

    fun updatePermissionsState(
        accessCoarseLocationPermissionGranted: Boolean,
        accessFineLocationPermissionGranted: Boolean,
        accessBackgroundLocationPermissionGranted: Boolean
    ) {
        this.accessCoarseLocationPermissionGranted = accessCoarseLocationPermissionGranted
        this.accessFineLocationPermissionGranted = accessFineLocationPermissionGranted
        this.accessBackgroundLocationPermissionGranted = accessBackgroundLocationPermissionGranted

        updateOnboardingFlowState()
    }

    fun onForegroundLocationPermissionsGranted() {
        accessCoarseLocationPermissionGranted = true
        accessFineLocationPermissionGranted = true

        updateOnboardingFlowState()
    }

    fun onForegroundLocationPermissionsDenied() {
        accessCoarseLocationPermissionGranted = false
        accessFineLocationPermissionGranted = false

        mutableNavigationRequests.value = OneTimeEvent(NavigateToSorryScreen)
    }

    fun onBackgroundLocationPermissionGranted() {
        accessBackgroundLocationPermissionGranted = true

        updateOnboardingFlowState()
    }

    fun onBackgroundLocationPermissionsDenied() {
        accessBackgroundLocationPermissionGranted = false

        mutableNavigationRequests.value = OneTimeEvent(NavigateToSorryScreen)
    }

    fun onLeaveTheAppButtonClicked() {
        mutableNavigationRequests.value = OneTimeEvent(NavigateOutOfApp)
    }

    private fun updateOnboardingFlowState() {
        if (!accessCoarseLocationPermissionGranted || !accessFineLocationPermissionGranted) {
            mutableNavigationRequests.value =
                OneTimeEvent(NavigateToForegroundLocationPermissionRequestScreen)
        } else if (!accessBackgroundLocationPermissionGranted) {
            mutableNavigationRequests.value =
                OneTimeEvent(NavigateToBackgroundLocationPermissionRequestScreen)
        } else {
            mutableNavigationRequests.value = OneTimeEvent(NavigateToMainScreen)
        }
    }
}
