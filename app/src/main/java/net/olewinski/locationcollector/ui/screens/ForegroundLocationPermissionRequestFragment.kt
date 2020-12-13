package net.olewinski.locationcollector.ui.screens

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import net.olewinski.locationcollector.databinding.FragmentForegroundLocationPermissionRequestBinding
import net.olewinski.locationcollector.util.checkAllPermissionsGranted
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ForegroundLocationPermissionRequestFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by sharedViewModel()
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allGranted = true

            result.values.forEach { value ->
                if (!value) {
                    allGranted = false

                    return@forEach
                }
            }

            if (allGranted) {
                onboardingViewModel.onForegroundLocationPermissionsGranted()
            } else {
                onboardingViewModel.onForegroundLocationPermissionsDenied()
            }
        }

    private lateinit var foregroundLocationPermissionRequestBinding: FragmentForegroundLocationPermissionRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        foregroundLocationPermissionRequestBinding =
            FragmentForegroundLocationPermissionRequestBinding.inflate(inflater, container, false)
        foregroundLocationPermissionRequestBinding.lifecycleOwner = viewLifecycleOwner

        return foregroundLocationPermissionRequestBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foregroundLocationPermissionRequestBinding.continueButton.setOnClickListener {
            requestPermissionsIfNeeded()
        }
    }

    private fun requestPermissionsIfNeeded() {
        val permissionsToCheck = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (requireContext().checkAllPermissionsGranted(permissionsToCheck)) {
            onboardingViewModel.onForegroundLocationPermissionsGranted()
        } else {
            requestPermissionsLauncher.launch(permissionsToCheck)
        }
    }
}
