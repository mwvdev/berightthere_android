package mwvdev.berightthere.android.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import mwvdev.berightthere.android.exception.CheckinException
import mwvdev.berightthere.android.model.TransportMode
import mwvdev.berightthere.android.persistence.entity.Trip
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.repository.ITripRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val tripRepository: ITripRepository) : ViewModel() {

    private val _state = MutableLiveData<MainViewState>(MainViewState.Ready)
    val state: LiveData<MainViewState> = _state

    val tripsWithLocations: LiveData<List<TripWithLocations>> = tripRepository.getTripsWithLocations()

    var transportMode: TransportMode = TransportMode.Car

    fun checkin() {
        _state.value = MainViewState.CheckingIn

        viewModelScope.launch {
            try {
                val tripIdentifier = tripRepository.checkin(transportMode)

                _state.value = MainViewState.CheckedIn(tripIdentifier)
            }
            catch(e: CheckinException) {
                _state.value = MainViewState.CheckinFailed("Failed to checkin, please retry.")
            }
        }
    }

    fun checkIfPermisionRequestNeeded() {
        _state.value = MainViewState.CheckingIfPermissionRequestNeeded
    }

    fun requestPermission() {
        _state.value = MainViewState.RequestingPermission
    }

    fun showPermissionRationale() {
        _state.value = MainViewState.ShowingPermissionRationale
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripRepository.delete(trip)
        }
    }

}

sealed class MainViewState {

    object Ready: MainViewState()

    object CheckingIfPermissionRequestNeeded : MainViewState()
    object RequestingPermission : MainViewState()
    object ShowingPermissionRationale : MainViewState()

    object CheckingIn: MainViewState()

    data class CheckedIn(
        val tripIdentifier: String
    ) : MainViewState()

    data class CheckinFailed(
        val message: String
    ) : MainViewState()

}