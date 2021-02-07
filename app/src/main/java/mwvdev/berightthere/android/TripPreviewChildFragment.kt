package mwvdev.berightthere.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.trip_ongoing_preview_child_fragment.*
import mwvdev.berightthere.android.persistence.entity.Location
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.viewmodel.TripViewModel
import javax.inject.Inject

class TripPreviewChildFragment : Fragment(), OnCameraMoveStartedListener, OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TripViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(TripViewModel::class.java)
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var travelPath: Polyline

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.trip_ongoing_preview_child_fragment, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)

        super.onAttach(context)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        with(this.googleMap) {
            setMaxZoomPreference(18f)
            setOnCameraMoveStartedListener(this@TripPreviewChildFragment)
        }

        autoMoveCameraButton.setOnClickListener {
            viewModel.enableAutoMoveCamera()

            val mappedLocations =
                viewModel.tripWithLocations.value!!.locations.map { mapLocation(it) }
            moveCamera(mappedLocations)
        }

        marker = this.googleMap.addMarker(
            MarkerOptions()
                .position(latestPosition(viewModel.tripWithLocations.value!!))
        )

        travelPath = this.googleMap.addPolyline(
            PolylineOptions()
                .color(resources.getColor(R.color.mainPathColor, null))
        )

        viewModel.tripWithLocations.observe(viewLifecycleOwner, Observer { trip ->
            val position = latestPosition(trip)

            marker.position = position

            val mappedLocations = trip.locations.map { mapLocation(it) }
            travelPath.points = mappedLocations

            if (viewModel.autoMoveCamera.value!!) {
                moveCamera(mappedLocations)
            }
        })

        viewModel.autoMoveCamera.observe(viewLifecycleOwner, Observer { autoMoveCamera ->
            autoMoveCameraButton.visibility = if (autoMoveCamera) View.GONE else View.VISIBLE
        })
    }

    override fun onCameraMoveStarted(reason: Int) {
        if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
            viewModel.disableAutoMoveCamera()
        }
    }

    private fun moveCamera(locations: List<LatLng>) {
        val latLngBounds = boundsForLocations(locations)
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 128))
    }

    private fun boundsForLocations(locations: List<LatLng>): LatLngBounds {
        val builder = LatLngBounds.Builder()

        locations.forEach { location -> builder.include(location) }

        return builder.build()
    }

    private fun latestPosition(trip: TripWithLocations): LatLng {
        return mapLocation(trip.locations.last())
    }

    private fun mapLocation(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

}