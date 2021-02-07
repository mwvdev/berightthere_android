package mwvdev.berightthere.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.trip_adapter_item.view.*
import mwvdev.berightthere.android.R
import mwvdev.berightthere.android.persistence.entity.TripWithLocations
import mwvdev.berightthere.android.service.mapper.TripItemMapper
import mwvdev.berightthere.android.viewmodel.MainViewModel


class TripAdapter(
    private val tripItemMapper: TripItemMapper,
    private val viewModel: MainViewModel,
    private val itemDeletedCallback: () -> Unit
) :
    ListAdapter<TripWithLocations, TripAdapter.TripViewHolder>(DiffCallback()) {

    inner class TripViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TripViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.trip_adapter_item, parent, false) as CardView
        return TripViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val item = getItem(position)

        holder.cardView.cardHeader.text = tripItemMapper.mapTitle(item.trip.createdAt)
        holder.cardView.cardTransportMode.setImageResource(tripItemMapper.mapTransportModeIcon(item.trip.transportMode))
        holder.cardView.cardDistance.text = tripItemMapper.mapDistance(item.locations)
        holder.cardView.cardCreatedAt.text = tripItemMapper.mapCreatedAt(item.trip.createdAt)
    }

    fun deleteTrip(tripPosition: Int) {
        val item = getItem(tripPosition)

        viewModel.deleteTrip(item.trip)
        itemDeletedCallback()
    }

    class DiffCallback : DiffUtil.ItemCallback<TripWithLocations>() {
        override fun areItemsTheSame(
            oldItem: TripWithLocations,
            newItem: TripWithLocations
        ): Boolean {
            return oldItem.trip.tripIdentifier == newItem.trip.tripIdentifier
        }

        override fun areContentsTheSame(
            oldItem: TripWithLocations,
            newItem: TripWithLocations
        ): Boolean {
            return oldItem == newItem
        }
    }

}
