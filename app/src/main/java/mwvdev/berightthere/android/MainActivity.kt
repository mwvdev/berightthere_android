package mwvdev.berightthere.android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import mwvdev.berightthere.android.service.IntentFactory
import mwvdev.berightthere.android.viewmodel.MainViewModel
import mwvdev.berightthere.android.viewmodel.MainViewState
import javax.inject.Inject


class MainActivity : FragmentActivity() {

    companion object {
        private const val packageName = "mwvdev.berightthere.android"

        const val CheckinIntentAction = "$packageName.checkin-intent"
        const val CheckedInBroadcastAction = "$packageName.checked-in-broadcast"
    }

    @Inject
    lateinit var intentFactory: IntentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewModel.state.observe(this, Observer {
            when (it) {
                MainViewState.Ready -> {
                    showFragment(MainOverviewFragment())
                }
                MainViewState.CheckingIfPermissionRequestNeeded -> {
                    ensurePermission()
                }
                MainViewState.RequestingPermission -> {
                    requestPermission()
                }
                MainViewState.ShowingPermissionRationale -> {
                    showFragment(MainPermissionRationaleFragment())
                }
                MainViewState.CheckingIn -> {
                    showFragment(MainCheckingInFragment())
                }
                is MainViewState.CheckedIn -> {
                    sendCheckedInBroadcast(it.tripIdentifier)
                    startTripActivity(it.tripIdentifier)
                }
                is MainViewState.CheckinFailed -> {
                    val toast = Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    toast.show()

                    showFragment(MainOverviewFragment())
                }
            }
        })

        if (shouldCheckinImmediately(intent)) {
            viewModel.checkin()
        }
    }

    private fun sendCheckedInBroadcast(tripIdentifier: String) {
        val intent = Intent(CheckedInBroadcastAction).apply {
            putExtra("tripIdentifier", tripIdentifier)
        }
        sendBroadcast(intent)
    }

    private fun startTripActivity(tripIdentifier: String) {
        val intent = Intent(this, TripActivity::class.java).apply {
            putExtra("tripIdentifier", tripIdentifier)
            putExtra("transportMode", viewModel.transportMode)
        }
        startActivity(intent)
    }

    private fun shouldCheckinImmediately(intent: Intent?) =
        intent != null && intent.action == CheckinIntentAction

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment_container, fragment)
        transaction.commit()
    }

    private fun ensurePermission() {
        when {
            hasRequiredPermission(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.checkin()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.showPermissionRationale()
            }
            else -> {
                viewModel.requestPermission()
            }
        }
    }

    private fun requestPermission() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    viewModel.checkin()
                } else {
                    viewModel.showPermissionRationale()
                }
            }

        requestPermissionLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun hasRequiredPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

}
