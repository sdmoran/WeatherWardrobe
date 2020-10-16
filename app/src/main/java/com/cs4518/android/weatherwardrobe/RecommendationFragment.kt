package com.cs4518.android.weatherwardrobe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.databinding.FragmentRecommendationBinding
import com.cs4518.android.weatherwardrobe.databinding.ListItemWardrobeBinding
import java.io.File

private const val TAG = "RecommendationFragment"

class RecommendationFragment : Fragment(R.layout.fragment_recommendation) {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var wardrobe: Wardrobe
    private lateinit var wardrobeRepository: WardrobeRepository
    //private lateinit var liveDataItems: LiveData<List<WardrobeItem>>

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecommendationBinding.bind(view)

        wardrobe = Wardrobe()
        wardrobeRepository = WardrobeRepository.get()
//        Log.d(TAG, wardrobeRepository.dailyWeatherData.toString())

        binding.dayTemp.text = wardrobeRepository.getTempDay()
        binding.chanceRain.text = "Chance of rain ${wardrobeRepository.getChanceOfRain()}"
        binding.currentWind.text = "Wind ${wardrobeRepository.getWind()}"

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

        private lateinit var photoFile: File
        val photoView: ImageView = itemView.findViewById(R.id.warDrobeMiniImage)
        private val filesDir = context?.applicationContext?.filesDir

        init {
            binding.viewModel = WardrobeItemViewModel()
        }

        fun bind(item: WardrobeItem) {
            binding.apply {
                viewModel?.item = item
                executePendingBindings()
                photoFile = File(filesDir, item.photoFileName)
                updatePhotoView()
            }
        }

        private fun updatePhotoView() {
            if (photoFile.exists()) {
                val bitmap = getScaledBitmap(photoFile.path, requireActivity())
                photoView.setImageBitmap(bitmap)
            } else {
                photoView.setImageDrawable(null)
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
        val recommendations = getRecommendations(items)
        val adapter = WardrobeItemAdapter(recommendations)
        binding.recommendationRecyclerView.adapter = adapter
    }

    private fun getRecommendations(items: List<WardrobeItem>): List<WardrobeItem> {
        val filters: MutableList<String> = mutableListOf()

        val dayHigh = wardrobeRepository.dailyWeatherData.temp.max
        val dayLow = wardrobeRepository.dailyWeatherData.temp.min
        val rainPercent = wardrobeRepository.dailyWeatherData.rain

        // If rain % greater than 50, suggest something waterproof
        if(rainPercent > .5) {
            filters.add("Waterproof")
        }

        if(dayHigh > 288.706) { // 60 degrees Fahrenheit, seems reasonable as cutoff for cool/warm
            filters.add("Cool")
        }
        else {
            filters.add("Warm")
        }

        var candidateTops = items
        var candidateBottoms = items
        var candidateAccessories = items

        // Try to apply filters to each article of clothing and see what we get
        for(f in filters) {
            candidateTops = candidateTops.filter { it ->
                it.type == "Top" && it.tags.contains(f, ignoreCase = true)
            }
            candidateBottoms = candidateBottoms.filter { it ->
                it.type == "Bottom" && it.tags.contains(f, ignoreCase = true)
            }
            candidateAccessories = candidateAccessories.filter { it ->
                it.type == "Accessory" && it.tags.contains(f, ignoreCase = true)
            }
        }

        var top = candidateTops.shuffled().firstOrNull()
        var bottom = candidateBottoms.shuffled().firstOrNull()
        var accessory = candidateAccessories.shuffled().firstOrNull()

        // If no clothing matches the criteria, try to just get a random one of the proper type
        if(top == null) {
            top = items.filter { it ->
                it.type == "Top"
            }.firstOrNull()
        }

        if(bottom == null) {
            bottom = items.filter { it ->
                it.type == "Bottom"
            }.firstOrNull()
        }

        if(accessory == null) {
            accessory = items.filter { it ->
                it.type == "Accessory"
            }.firstOrNull()
        }

        val garments =  mutableListOf<WardrobeItem?>(top, bottom, accessory)

        val ret: MutableList<WardrobeItem> = mutableListOf<WardrobeItem>()

        for(i in 0 until garments.size) {
            if(garments[i] == null) {
                ret.add(WardrobeItem(name="No item found!"))
            }
            else {
                ret.add(garments[i]!!)
            }
        }

        return ret
    }

    companion object {
        fun newInstance(): RecommendationFragment {
            return RecommendationFragment()
        }
    }
}