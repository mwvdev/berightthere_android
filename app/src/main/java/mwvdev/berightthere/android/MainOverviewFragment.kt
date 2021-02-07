package mwvdev.berightthere.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_overview_fragment.*
import mwvdev.berightthere.android.adapter.TripAdapter
import mwvdev.berightthere.android.adapter.TripItemSwipeCallback
import mwvdev.berightthere.android.databinding.MainOverviewFragmentBinding
import mwvdev.berightthere.android.service.mapper.TripItemMapper
import mwvdev.berightthere.android.viewmodel.MainViewModel
import javax.inject.Inject

class MainOverviewFragment : Fragment() {

    @Inject
    lateinit var tripItemMapper: TripItemMapper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MainOverviewFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTripButton.setOnClickListener {
            viewModel.checkIfPermisionRequestNeeded()
        }

        val tripAdapter = TripAdapter(tripItemMapper, viewModel) {
            val snackbar = Snackbar.make(main_overview_fragment, "Trip deleted", Snackbar.LENGTH_SHORT)
            snackbar.show()
        }

        tripRecyclerView.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(requireActivity())
            adapter = tripAdapter
        }

        val itemTouchHelper = ItemTouchHelper(TripItemSwipeCallback(tripAdapter))
        itemTouchHelper.attachToRecyclerView(tripRecyclerView)

        viewModel.tripsWithLocations.observe(requireActivity(), Observer { trips ->
            tripAdapter.submitList(trips)
        })
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)

        super.onAttach(context)
    }

}