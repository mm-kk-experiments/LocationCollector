package net.olewinski.locationcollector.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.olewinski.locationcollector.databinding.FragmentMainBinding
import net.olewinski.locationcollector.viewmodels.MainFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val mainFragmentViewModel: MainFragmentViewModel by viewModel()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainFragmentViewModel.allLocationData.observe(viewLifecycleOwner) { result ->
            result.forEach { locationData ->
                Log.d("MainFragment", locationData.toString())
            }
        }
    }
}
