package mwvdev.berightthere.android.service

import android.R
import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mwvdev.berightthere.android.TripActivity
import mwvdev.berightthere.android.exception.AddLocationException
import mwvdev.berightthere.android.model.MeasuredLocation
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.repository.ITripRepository
import javax.inject.Inject

class LocationService : Service() {

    companion object {
        const val Tag = "LocationService"

        const val ChannelId = "LocationServiceChannel"

        const val HandlerThreadName = "LocationServiceThread"
    }

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val binder = LocalBinder()

    private lateinit var serviceHandler: Handler

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val locationCallback = createLocationCallback()

    @Inject
    lateinit var tripRepository: ITripRepository

    @Inject
    lateinit var intentFactory: IntentFactory

    private lateinit var tripIdentifier: String
    private lateinit var transportMode: TransportMode

    override fun onCreate() {
        AndroidInjection.inject(this)

        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val handlerThread = HandlerThread(HandlerThreadName)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tripIdentifier = intent!!.extras!!.getString("tripIdentifier")!!
        transportMode = intent.extras!!.get("transportMode") as TransportMode

        createNotificationChannel()

        val notificationPendingIntent = createTripActivityPendingIntent()
        val stopTripPendingIntent: PendingIntent = createStopTripPendingIntent()

        val notification: Notification = NotificationCompat
            .Builder(this, ChannelId)
            .setContentTitle("Be right there")
            .setContentText("Tracking location")
            .setSmallIcon(R.drawable.ic_dialog_map)
            .setContentIntent(notificationPendingIntent)
            .addAction(R.drawable.ic_delete, "Stop trip", stopTripPendingIntent)
            .build()

        notification.flags = (notification.flags or Notification.FLAG_ONGOING_EVENT)

        startForeground(1, notification)

        subscribe()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        serviceHandler.removeCallbacksAndMessages(null)
        serviceJob.cancel()

        super.onDestroy()
    }

    fun subscribe() {
        try {
            fusedLocationProviderClient!!.requestLocationUpdates(
                createLocationRequest(), locationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(Tag, unlikely.message!!)
        }
    }

    fun unsubscribe() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val lastLocation = locationResult.lastLocation

                serviceScope.launch {
                    try {
                        tripRepository.addLocation(
                            tripIdentifier,
                            MeasuredLocation(
                                lastLocation.latitude,
                                lastLocation.longitude,
                                lastLocation.accuracy
                            )
                        )
                    } catch (e: AddLocationException) {
                        Log.e(Tag, e.message!!)
                    }
                }
            }
        }
    }

    private fun createTripActivityPendingIntent(): PendingIntent? {
        val tripActivityIntent = Intent(this, TripActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            this, 0, tripActivityIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private fun createStopTripPendingIntent(): PendingIntent {
        val stopTripIntent = intentFactory.createMainActivityIntent(this)
        return PendingIntent.getActivity(this, 0, stopTripIntent, 0)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                ChannelId,
                "Be right there service channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.smallestDisplacement = mapTransportMode(transportMode)
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        return locationRequest
    }

    private fun mapTransportMode(transportMode: TransportMode): Float {
        return when (transportMode) {
            TransportMode.Walking -> 15.0f
            TransportMode.Bicycle -> 25.0f
            TransportMode.Car -> 75.0f
        }
    }

    inner class LocalBinder : Binder() {

        fun getService(): LocationService = this@LocationService

    }

}