package mwvdev.berightthere.android.service

import android.content.Context
import android.content.Intent
import mwvdev.berightthere.android.model.TransportMode

interface IntentFactory {

    fun createMainActivityIntent(packageContext: Context): Intent

    fun createSendIntent(packageContext: Context, text: String): Intent

    fun createTripActivityIntent(
        packageContext: Context,
        tripIdentifier: String,
        transportMode: TransportMode
    ): Intent

}