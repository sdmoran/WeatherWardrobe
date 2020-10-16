package com.cs4518.android.weatherwardrobe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cs4518.android.weatherwardrobe.weather.data.WardrobeItemDetailViewModel
import java.io.File
import java.util.*
private const val TAG = "ClothingItemFragment"
private const val ARG_WARDROBEITEM_ID = "id"
private const val REQUEST_PHOTO = 2

class ClothingItemFragment : Fragment() {

    private lateinit var warDrobeItem: WardrobeItem
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var nameField: EditText
    private lateinit var typeField: EditText
    private lateinit var tagsField: EditText
    private lateinit var save_button: Button
    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView

    private val wardrobeItemDetailViewModel: WardrobeItemDetailViewModel by lazy {
        ViewModelProviders.of(this).get(WardrobeItemDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        warDrobeItem = WardrobeItem()
        val wardrobeItemId: UUID = arguments?.getSerializable(ARG_WARDROBEITEM_ID) as UUID
        Log.d(TAG, "args bundle wardrobeItem ID: $wardrobeItemId")
        wardrobeItemDetailViewModel.loadWardrobeItem(wardrobeItemId)
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

        photoButton = view.findViewById(R.id.wardrobe_camera) as ImageButton
        photoView = view.findViewById(R.id.wardrobe_photo) as ImageView

        save_button = view.findViewById(R.id.save_button) as Button

        save_button.apply {
            isEnabled = true
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wardrobeItemDetailViewModel.wardrobeLiveData.observe(
            viewLifecycleOwner,
            Observer { warDrobeItem ->
                warDrobeItem?.let {
                    this.warDrobeItem = warDrobeItem
                    photoFile = wardrobeItemDetailViewModel.getPhotoFile(warDrobeItem)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.cs4518.android.weatherwardrobe.fileprovider",
                        photoFile)
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        nameField.setText(warDrobeItem.name)
        typeField.setText(warDrobeItem.type)
        tagsField.setText(warDrobeItem.tags)
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        } else {
            photoView.setImageDrawable(null)
        }
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
                warDrobeItem.name = sequence.toString()
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
                warDrobeItem.type = sequence.toString()
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
                warDrobeItem.tags = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        nameField.addTextChangedListener(nameWatcher)
        typeField.addTextChangedListener(typeWatcher)
        tagsField.addTextChangedListener(tagWatcher)

        save_button.setOnClickListener{
            wardrobeItemDetailViewModel.addOrUpdateItem(warDrobeItem)
//            if(warDrobeItem.id == null){
//                wardrobeItemDetailViewModel.saveWarDrobeItem(warDrobeItem)
//            }else{
//                wardrobeItemDetailViewModel.updateWarDrobeItem(warDrobeItem)
//            }
        }

        photoButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }

            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }

                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        wardrobeItemDetailViewModel.updateWarDrobeItem(warDrobeItem)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return

            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    companion object {

        fun newInstance(id: UUID): ClothingItemFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WARDROBEITEM_ID, id)
            }
            return ClothingItemFragment().apply {
                arguments = args
            }
        }
    }

}