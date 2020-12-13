package net.olewinski.locationcollector.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.olewinski.locationcollector.databinding.FragmentMainBinding
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by sharedViewModel()

    private lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        fragmentMainBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentMainBinding.root
    }
}
