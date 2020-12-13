package net.olewinski.locationcollector.di.modules

import net.olewinski.locationcollector.viewmodels.MainFragmentViewModel
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val viewModelsModule = module {
    viewModel { OnboardingViewModel() }
    viewModel { MainFragmentViewModel(get(), get()) }
}
