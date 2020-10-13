package com.cs4518.android.weatherwardrobe

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.databinding.FragmentWardrobeListBinding
import com.cs4518.android.weatherwardrobe.databinding.ListItemWardrobeBinding

private const val TAG = "WardrobeListFragment"

class WardrobeListFragment : Fragment(R.layout.fragment_wardrobe_list) {
    private var fragmentBinding: FragmentWardrobeListBinding? = null

    private lateinit var wardrobe: Wardrobe

    private val wardrobeRepository = WardrobeRepository.get()
    private val wardrobeItemsLiveData = wardrobeRepository.getWardrobeItems()


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_wardrobe_list, menu)
        wardrobeRepository.addWardrobeItem( WardrobeItem(name="Red Sweater", type="Sweater", tags="Good,Warm,Fluffy"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        wardrobe = Wardrobe()

        Log.d(TAG, "In onViewCreated!!!")
        val binding = FragmentWardrobeListBinding.bind(view)
        fragmentBinding = binding

        binding.wardrobeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            val liveDataItems = wardrobe.items
            liveDataItems.observe(viewLifecycleOwner,
            Observer { items ->
                items?.let {
                    Log.d(TAG, "Got ${items.size}")
                    adapter = WardrobeItemAdapter(items)
                }
            })
        }
    }

    companion object {
        fun newInstance() : WardrobeListFragment {
            return WardrobeListFragment()
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

    private inner class WardrobeItemAdapter(private val sounds: List<WardrobeItem>) :
        RecyclerView.Adapter<WardrobeItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeItemHolder {
            val binding = DataBindingUtil.inflate<ListItemWardrobeBinding>(
                layoutInflater,
                R.layout.list_item_wardrobe,
                parent,
                false
            )
            return WardrobeItemHolder(binding)
        }

        override fun getItemCount(): Int {
            Log.d(TAG, "GOT ${sounds.size} items!")
            return sounds.size
        }

        override fun onBindViewHolder(holder: WardrobeItemHolder, position: Int) {
            val wardrobeItem = sounds[position]
            Log.d(TAG, "Attempting to bind ${wardrobeItem.name}")
            holder.bind(wardrobeItem)
        }
    }
}