package net.olewinski.locationcollector.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import net.olewinski.locationcollector.R
import net.olewinski.locationcollector.databinding.FragmentMainBinding
import net.olewinski.locationcollector.viewmodels.MainFragmentViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val mainFragmentViewModel: MainFragmentViewModel by viewModel()

    private lateinit var fragmentMainBinding: FragmentMainBinding

    private val callback = OnMapReadyCallback { googleMap ->
        mainFragmentViewModel.allLocationData.observe(viewLifecycleOwner) { allLocationData ->
            if (allLocationData.isNotEmpty()) {
                val builder = LatLngBounds.Builder()

                // TODO remove old markers
                allLocationData.forEach { locationData ->
                    val marker = googleMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                locationData.latitude,
                                locationData.longitude
                            )
                        )
                    )

                    builder.include(marker.position)
                }

                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10))
            }
        }
    }

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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
