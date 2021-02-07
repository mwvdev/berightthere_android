@file:JvmName("BindingUtils")

package mwvdev.berightthere.android.viewmodel.binding

import androidx.databinding.InverseMethod
import mwvdev.berightthere.android.R
import mwvdev.berightthere.android.model.TransportMode

@InverseMethod("buttonIdToTransportMode")
fun transportModeToButtonId(transportMode: TransportMode?): Int {
    var selectedButtonId = -1

    transportMode?.run {
        selectedButtonId = when (this) {
            TransportMode.Walking -> R.id.walkingChip
            TransportMode.Bicycle -> R.id.bicycleChip
            TransportMode.Car -> R.id.carChip
        }
    }

    return selectedButtonId
}

fun buttonIdToTransportMode(selectedButtonId: Int): TransportMode? {
    return when (selectedButtonId) {
        R.id.walkingChip -> TransportMode.Walking
        R.id.bicycleChip -> TransportMode.Bicycle
        R.id.carChip -> TransportMode.Car
        else -> null
    }
}