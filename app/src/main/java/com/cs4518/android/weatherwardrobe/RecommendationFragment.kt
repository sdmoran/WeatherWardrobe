package com.cs4518.android.weatherwardrobe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.databinding.FragmentRecommendationBinding
import com.cs4518.android.weatherwardrobe.databinding.ListItemWardrobeBinding

private const val TAG = "RecommendationFragment"

class RecommendationFragment : Fragment(R.layout.fragment_recommendation) {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var wardrobe: Wardrobe
    private lateinit var wardrobeRepository: WardrobeRepository
    //private lateinit var liveDataItems: LiveData<List<WardrobeItem>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecommendationBinding.bind(view)

        wardrobe = Wardrobe()
        wardrobeRepository = WardrobeRepository.get()
//        Log.d(TAG, wardrobeRepository.dailyWeatherData.toString())

        binding.currentTemp.text = wardrobeRepository.dailyWeatherData.temp.day.toString()
        binding.currentWeather.text = (wardrobeRepository.dailyWeatherData.pop * 100).toString()
        binding.currentWind.text = wardrobeRepository.dailyWeatherData.wind_speed.toString()

        binding.recommendationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val liveDataItems = wardrobe.items
            liveDataItems.observe(viewLifecycleOwner,
            Observer { items ->
                items?.let {
                    Log.d(TAG, "Got ${items.size}!")
                    updateUI(items)
                }
            })
        }
    }

    private inner class WardrobeItemHolder(private val binding: ListItemWardrobeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = WardrobeItemViewModel()
        }

        fun bind(item: WardrobeItem) {
            binding.apply {
                viewModel?.item = item
                executePendingBindings()
            }
        }
    }

    private inner class WardrobeItemAdapter(private var items: List<WardrobeItem>) :
        RecyclerView.Adapter<RecommendationFragment.WardrobeItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationFragment.WardrobeItemHolder {
            val binding = DataBindingUtil.inflate<ListItemWardrobeBinding>(
                layoutInflater,
                R.layout.list_item_wardrobe,
                parent,
                false
            )
            return WardrobeItemHolder(binding)
        }

        override fun getItemCount(): Int {
            Log.d(TAG, "GOT ${items.size} items!")
            return items.size
        }

        override fun onBindViewHolder(holder: RecommendationFragment.WardrobeItemHolder, position: Int) {
            val wardrobeItem = items[position]
            holder.bind(wardrobeItem)
        }
    }

    private fun updateUI(items: List<WardrobeItem>) {
        val adapter = WardrobeItemAdapter(items)
        binding.recommendationRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): RecommendationFragment {
            return RecommendationFragment()
        }
    }
}