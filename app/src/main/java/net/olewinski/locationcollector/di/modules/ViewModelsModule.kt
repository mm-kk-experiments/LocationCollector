package net.olewinski.locationcollector.di.modules

import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { OnboardingViewModel() }
}
