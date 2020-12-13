package net.olewinski.locationcollector.ui.screens

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.olewinski.locationcollector.databinding.FragmentSplashScreenBinding
import net.olewinski.locationcollector.util.checkSinglePermissionGranted
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SplashScreenFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by sharedViewModel()

    private lateinit var splashScreenBinding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashScreenBinding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        splashScreenBinding.lifecycleOwner = viewLifecycleOwner

        return splashScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermissions()
    }

    private fun checkPermissions() {
        val context = requireContext()

        val accessCoarseLocationPermissionGranted =
            context.checkSinglePermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        val accessFineLocationPermissionGranted =
            context.checkSinglePermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
        val accessBackgroundLocationPermissionGranted =
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
                context.checkSinglePermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }

        onboardingViewModel.updatePermissionsState(
            accessCoarseLocationPermissionGranted,
            accessFineLocationPermissionGranted,
            accessBackgroundLocationPermissionGranted
        )
    }
}
