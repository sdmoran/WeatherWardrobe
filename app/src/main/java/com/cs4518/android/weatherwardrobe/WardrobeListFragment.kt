package com.cs4518.android.weatherwardrobe

import android.content.Context
import android.location.Criteria
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4518.android.weatherwardrobe.databinding.FragmentWardrobeListBinding
import com.cs4518.android.weatherwardrobe.databinding.ListItemWardrobeBinding
import java.util.*

private const val TAG = "WardrobeListFragment"

class WardrobeListFragment : Fragment(R.layout.fragment_wardrobe_list) {
    private var fragmentBinding: FragmentWardrobeListBinding? = null

    private lateinit var binding: FragmentWardrobeListBinding

    private lateinit var wardrobe: Wardrobe
    private lateinit var liveDataItems: LiveData<List<WardrobeItem>>

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onWarDrobeSelected(id: UUID)
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_wardrobe_list, menu)

        val item = menu.findItem(R.id.menu_item_search)
        val searchView = item.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")
                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")
                    liveDataItems.observe(viewLifecycleOwner,
                        Observer { items ->
                            items?.let {
                                Log.d(TAG, "Got ${items.size}")
                                updateUI(items, searchStr = queryText)
                            }
                        })
                    return false
                }
            })
        }

//        wardrobeRepository.clearDB()
//        wardrobeRepository.addWardrobeItem( WardrobeItem(name="Blue Jeans", type="Jeans", tags="PANTS"))
//        wardrobeRepository.addWardrobeItem( WardrobeItem(name="Red Sweater", type="Sweater", tags="FUZZY"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        wardrobe = Wardrobe()

        Log.d(TAG, "In onViewCreated!!!")
        binding = FragmentWardrobeListBinding.bind(view)
        fragmentBinding = binding

        binding.wardrobeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            liveDataItems = wardrobe.items
            liveDataItems.observe(viewLifecycleOwner,
            Observer { items ->
                items?.let {
                    Log.d(TAG, "Got ${items.size}")
                    updateUI(items)
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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var itemCopy = WardrobeItem()

        init {
            binding.viewModel = WardrobeItemViewModel()
            itemView.setOnClickListener(this)
        }

        fun bind(item: WardrobeItem) {
            binding.apply {
                viewModel?.item = item
                itemCopy = item
                executePendingBindings()
            }
        }

        override fun onClick(v: View) {
            callbacks?.onWarDrobeSelected(itemCopy.id)
        }
    }

    private inner class WardrobeItemAdapter(private var items: List<WardrobeItem>) :
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
            Log.d(TAG, "GOT ${items.size} items!")
            return items.size
        }

        override fun onBindViewHolder(holder: WardrobeItemHolder, position: Int) {
            val wardrobeItem = items[position]
            Log.d(TAG, "Attempting to bind ${wardrobeItem.name}, number ${position}")
            holder.bind(wardrobeItem)
        }
    }

    private fun updateUI(items: List<WardrobeItem>, searchStr: String = "") {
        var filtered = items
        if(searchStr.isNotEmpty()) {
            filtered = items.filter { it.name.contains(searchStr, ignoreCase = true)
                                        || it.tags.contains(searchStr, ignoreCase = true)
                                        || it.type.contains(searchStr, ignoreCase = true)}
        }
        val adapter = WardrobeItemAdapter(filtered)
        Log.d(TAG, "Filtered items: ")
        for(i in items) {
            Log.d(TAG, "${i.name}")
        }
        binding.wardrobeRecyclerView.adapter = adapter
    }
}