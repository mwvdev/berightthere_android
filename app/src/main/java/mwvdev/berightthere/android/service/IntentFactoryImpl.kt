package mwvdev.berightthere.android.service

import android.content.Context
import android.content.Intent
import mwvdev.berightthere.android.MainActivity
import mwvdev.berightthere.android.TripActivity
import mwvdev.berightthere.android.model.TransportMode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntentFactoryImpl @Inject constructor() : IntentFactory {

    override fun createMainActivityIntent(packageContext: Context): Intent {
        return Intent(packageContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun createSendIntent(packageContext: Context, text: String): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
    }

    override fun createTripActivityIntent(
        packageContext: Context,
        tripIdentifier: String,
        transportMode: TransportMode
    ): Intent {
        return Intent(packageContext, TripActivity::class.java).apply {
            putExtra("tripIdentifier", tripIdentifier)
            putExtra("transportMode", transportMode)
        }
    }

}
