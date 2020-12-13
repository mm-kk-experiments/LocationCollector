package net.olewinski.locationcollector.ui.screens

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import net.olewinski.locationcollector.databinding.FragmentBackgroundLocationPermissionRequestBinding
import net.olewinski.locationcollector.util.checkSinglePermissionGranted
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class BackgroundLocationPermissionRequestFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by sharedViewModel()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                onboardingViewModel.onBackgroundLocationPermissionGranted()
            } else {
                onboardingViewModel.onBackgroundLocationPermissionsDenied()
            }
        }

    private lateinit var backgroundLocationPermissionRequestBinding: FragmentBackgroundLocationPermissionRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        backgroundLocationPermissionRequestBinding =
            FragmentBackgroundLocationPermissionRequestBinding.inflate(inflater, container, false)
        backgroundLocationPermissionRequestBinding.lifecycleOwner = viewLifecycleOwner

        return backgroundLocationPermissionRequestBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backgroundLocationPermissionRequestBinding.continueButton.setOnClickListener {
            requestPermissionIfNeeded()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissionIfNeeded() {
        val permissionToCheck = Manifest.permission.ACCESS_BACKGROUND_LOCATION

        if (requireContext().checkSinglePermissionGranted(permissionToCheck)) {
            onboardingViewModel.onForegroundLocationPermissionsGranted()
        } else {
            requestPermissionLauncher.launch(permissionToCheck)
        }
    }
}