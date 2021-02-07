package mwvdev.berightthere.android

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.service.DialogService
import mwvdev.berightthere.android.service.IntentFactory
import mwvdev.berightthere.android.service.LocationService
import mwvdev.berightthere.android.viewmodel.TripViewModel
import javax.inject.Inject

class TripActivity : FragmentActivity() {

    @Inject
    lateinit var dialogService: DialogService

    @Inject
    lateinit var intentFactory: IntentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TripViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        val tripIdentifier = intent.extras!!.getString("tripIdentifier")!!
        val transportMode = intent.extras!!.get("transportMode") as TransportMode
        viewModel.initializeView(tripIdentifier)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        replaceFragment(TripOngoingFragment())

        startLocationService(tripIdentifier, transportMode)
    }

    override fun onDestroy() {
        super.onDestroy()

        stopLocationService()
    }

    override fun onBackPressed() {
        dialogService.showConfirmExitDialog(this,
            R.string.trip_exit_confirm_title,
            R.string.trip_exit_confirm_message,
            { _, _ -> stopTrip() })
    }

    private fun startLocationService(tripIdentifier: String, transportMode: TransportMode) {
        val requiredPermissions = arrayOf(
            Manifest.permission.FOREGROUND_SERVICE
        )
        ActivityCompat.requestPermissions(this, requiredPermissions, 42)

        val serviceIntent = Intent(this, LocationService::class.java).apply {
            putExtra("tripIdentifier", tripIdentifier)
            putExtra("transportMode", transportMode)
        }
        startForegroundService(serviceIntent)
    }

    private fun stopLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        stopService(serviceIntent)
    }

    private fun stopTrip() {
        val showMainOverviewIntent = intentFactory.createMainActivityIntent(this)
        startActivity(showMainOverviewIntent)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.trip_fragment_container, fragment)
        transaction.commit()
    }

}
