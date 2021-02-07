package mwvdev.berightthere.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.trip_ongoing_fragment.*
import mwvdev.berightthere.android.service.DialogService
import mwvdev.berightthere.android.service.IntentFactory
import mwvdev.berightthere.android.service.LocationService
import mwvdev.berightthere.android.viewmodel.TripViewModel
import javax.inject.Inject

class TripOngoingFragment : Fragment() {

    private lateinit var locationService: LocationService
    private var locationServiceBound = false

    @Inject
    lateinit var dialogService: DialogService

    @Inject
    lateinit var intentFactory: IntentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TripViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(TripViewModel::class.java)
    }

    private val locationServiceConnection = createLocationServiceConnection()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trip_ongoing_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()

        Intent(requireActivity(), LocationService::class.java).also { intent ->
            requireActivity().bindService(
                intent,
                locationServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onStop() {
        super.onStop()

        requireActivity().unbindService(locationServiceConnection)
        locationServiceBound = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareTripButton.setOnClickListener {
            shareTrip()
        }

        tripBottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.stopTripMenuItem -> {
                    promptStopTrip()
                    true
                }
                R.id.pauseTripMenuItem -> {
                    pauseTrip()
                    true
                }
                R.id.resumeTripMenuItem -> {
                    resumeTrip()
                    true
                }
                else -> false
            }
        }

        viewModel.tripWithLocations.observe(viewLifecycleOwner, Observer { trip ->
            when {
                trip.locations.isEmpty() -> showLocatingFragment()
                trip.locations.isNotEmpty() -> showPreviewFragment()
            }
        })

        viewModel.locationServiceSubscription.observe(viewLifecycleOwner, Observer { isSubscribed ->
            val pauseTripMenuItem = tripBottomAppBar.menu.findItem(R.id.pauseTripMenuItem)
            val resumeTripMenuItem = tripBottomAppBar.menu.findItem(R.id.resumeTripMenuItem)

            pauseTripMenuItem.isVisible = isSubscribed
            resumeTripMenuItem.isVisible = !isSubscribed
        })
    }

    private fun showLocatingFragment() {
        replaceChildFragment(TripLocatingChildFragment())
    }

    private fun showPreviewFragment() {
        val currentFragment = childFragmentManager.fragments.find { it is TripPreviewChildFragment }
        if (currentFragment == null) {
            replaceChildFragment(TripPreviewChildFragment())
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)

        super.onAttach(context)
    }

    private fun shareTrip() {
        val text = "I'm on my way! Follow my location at: ${viewModel.getTripUrl()}"
        val sendIntent = intentFactory.createSendIntent(requireActivity(), text)

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun promptStopTrip() {
        dialogService.showConfirmExitDialog(requireActivity(),
            R.string.trip_exit_confirm_title,
            R.string.trip_exit_confirm_message,
            { _, _ -> stopTrip() })
    }

    private fun stopTrip() {
        val showMainOverviewIntent = intentFactory.createMainActivityIntent(requireActivity())
        startActivity(showMainOverviewIntent)
    }

    private fun pauseTrip() {
        if (locationServiceBound) {
            locationService.unsubscribe()
            viewModel.locationServiceUnsubscribed()
        }
    }

    private fun resumeTrip() {
        if (locationServiceBound) {
            locationService.subscribe()
            viewModel.locationServiceSubscribed()
        }
    }

    private fun replaceChildFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.trip_child_fragment_container, fragment)
        transaction.commit()
    }

    private fun createLocationServiceConnection(): ServiceConnection {
        return object : ServiceConnection {

            override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
                val binder = service as LocationService.LocalBinder
                locationService = binder.getService()
                locationServiceBound = true
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                locationServiceBound = false
            }

        }
    }

}