package net.olewinski.locationcollector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import net.olewinski.locationcollector.databinding.ActivityMainBinding
import net.olewinski.locationcollector.ui.screens.BackgroundLocationPermissionRequestFragmentDirections
import net.olewinski.locationcollector.ui.screens.ForegroundLocationPermissionRequestFragmentDirections
import net.olewinski.locationcollector.ui.screens.MainFragmentDirections
import net.olewinski.locationcollector.ui.screens.SorryFragmentDirections
import net.olewinski.locationcollector.ui.screens.SplashScreenFragmentDirections
import net.olewinski.locationcollector.viewmodels.NavigateOutOfApp
import net.olewinski.locationcollector.viewmodels.NavigateToBackgroundLocationPermissionRequestScreen
import net.olewinski.locationcollector.viewmodels.NavigateToForegroundLocationPermissionRequestScreen
import net.olewinski.locationcollector.viewmodels.NavigateToMainScreen
import net.olewinski.locationcollector.viewmodels.NavigateToSorryScreen
import net.olewinski.locationcollector.viewmodels.NavigateToSplashScreen
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val onboardingViewModel: OnboardingViewModel by viewModel()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        navController = findNavController(R.id.main_navigation_nav_host)
    }

    override fun onResume() {
        super.onResume()

        onboardingViewModel.navigationRequests.observe(this) { navigationRequestOneTimeEvent ->
            navigationRequestOneTimeEvent.getContent()?.let { navigationRequest ->
                when (navigationRequest) {
                    is NavigateToSplashScreen -> {
                        navController.navigate(SplashScreenFragmentDirections.actionGlobalSplashScreenFragment())
                    }

                    is NavigateToForegroundLocationPermissionRequestScreen -> {
                        navController.navigate(ForegroundLocationPermissionRequestFragmentDirections.actionGlobalForegroundLocationPermissionRequestFragment())
                    }

                    is NavigateToBackgroundLocationPermissionRequestScreen -> {
                        navController.navigate(BackgroundLocationPermissionRequestFragmentDirections.actionGlobalBackgroundLocationPermissionRequestFragment())
                    }

                    is NavigateToSorryScreen -> {
                        navController.navigate(SorryFragmentDirections.actionGlobalSorryFragment())
                    }

                    is NavigateToMainScreen -> {
                        navController.navigate(MainFragmentDirections.actionGlobalMainFragment())
                    }

                    is NavigateOutOfApp -> {
                        finish()
                    }
                }
            }
        }
    }
}
