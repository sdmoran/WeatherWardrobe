package com.cs4518.android.weatherwardrobe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.clothing_item_fragment.*

class ClothingItemFragment : Fragment() {

    private lateinit var clothingItem: ClothingItem
    private lateinit var nameField: EditText
    private lateinit var typeField: EditText
    private lateinit var tagsField: EditText
    private lateinit var save_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clothingItem = ClothingItem()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.clothing_item_fragment, container, false)

        nameField = view.findViewById(R.id.clothing_item_name) as EditText
        typeField = view.findViewById(R.id.clothing_item_type) as EditText
        tagsField = view.findViewById(R.id.clothing_item_tags) as EditText

        save_button = view.findViewById(R.id.save_button) as Button

        save_button.apply {
            isEnabled = false
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                clothingItem.clothingName = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        val typeWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                clothingItem.clothingName = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        val tagWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                clothingItem.clothingName = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        nameField.addTextChangedListener(nameWatcher)
        typeField.addTextChangedListener(typeWatcher)
        tagsField.addTextChangedListener(tagWatcher)

        save_button.setOnClickListener{
            //clothingItem.clothingName
            //clothingItem.clothingType
            //clothingItem.clothingAttribute
        }
    }

}