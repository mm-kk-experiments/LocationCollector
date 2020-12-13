package net.olewinski.locationcollector.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.olewinski.locationcollector.databinding.FragmentSorryBinding
import net.olewinski.locationcollector.viewmodels.OnboardingViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SorryFragment : Fragment() {

    private val onboardingViewModel: OnboardingViewModel by sharedViewModel()

    private lateinit var fragmentSorryBinding: FragmentSorryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSorryBinding = FragmentSorryBinding.inflate(inflater, container, false)
        fragmentSorryBinding.lifecycleOwner = viewLifecycleOwner

        return fragmentSorryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentSorryBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        fragmentSorryBinding.leaveTheAppButton.setOnClickListener {
            onboardingViewModel.onLeaveTheAppButtonClicked()
        }
    }
}
