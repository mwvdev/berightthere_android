package mwvdev.berightthere.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.repository.ITripRepository
import javax.inject.Inject

class TripViewModel @Inject constructor(private val tripRepository: ITripRepository) : ViewModel() {

    lateinit var tripIdentifier: String private set
    lateinit var tripWithLocations: LiveData<TripWithLocations> private set

    private val _locationServiceSubscription = MutableLiveData<Boolean>(true)
    var locationServiceSubscription: LiveData<Boolean> = _locationServiceSubscription

    private val _autoMoveCamera = MutableLiveData<Boolean>(true)
    val autoMoveCamera: LiveData<Boolean> = _autoMoveCamera

    fun initializeView(tripIdentifier: String) {
        this.tripIdentifier = tripIdentifier

        viewModelScope.launch {
            tripWithLocations = tripRepository.getTripWithLocations(tripIdentifier)
        }
    }

    fun getTripUrl() = tripRepository.getTripUrl(tripIdentifier)

    fun locationServiceSubscribed() {
        _locationServiceSubscription.value = true
    }

    fun locationServiceUnsubscribed() {
        _locationServiceSubscription.value = false
    }

    fun enableAutoMoveCamera() {
        _autoMoveCamera.value = true
    }

    fun disableAutoMoveCamera() {
        _autoMoveCamera.value = false
    }

}
