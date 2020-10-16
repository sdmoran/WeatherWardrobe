package com.cs4518.android.weatherwardrobe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.cs4518.android.weatherwardrobe.databinding.ActivityMainBinding
import com.cs4518.android.weatherwardrobe.databinding.FragmentWardrobeListBinding
import com.cs4518.android.weatherwardrobe.databinding.ListItemWardrobeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var photoView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: FragmentWardrobeListBinding = DataBindingUtil.setContentView(this, R.layout.fragment_wardrobe_list)

//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = ClothingAdapter()
//        }

        setContentView(R.layout.activity_main)
    }

    private inner class ClothingItemHolder(private val binding: ListItemWardrobeBinding) :
            RecyclerView.ViewHolder(binding.root) {
    }

    private inner class ClothingAdapter() :
        RecyclerView.Adapter<ClothingItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingItemHolder {
                val binding = DataBindingUtil.inflate<ListItemWardrobeBinding>(
                    layoutInflater,
                    R.layout.list_item_wardrobe,
                    parent,
                    false
                )
                return ClothingItemHolder(binding)
            }

        override fun onBindViewHolder(holder: ClothingItemHolder, position: Int) {

        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }
    }
}