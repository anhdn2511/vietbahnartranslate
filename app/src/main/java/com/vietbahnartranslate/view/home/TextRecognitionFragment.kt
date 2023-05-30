package com.vietbahnartranslate.view.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.vietbahnartranslate.R
import com.vietbahnartranslate.viewmodel.home.TextRecognitionViewModel
import java.io.FileInputStream

class TextRecognitionFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 201

    private var bottomNavigationView : BottomNavigationView? = null
    private lateinit var extractTextButton: Button
    private lateinit var closeDialogButton: ImageView
    private lateinit var copyToClipBoardButton: ImageView
    private lateinit var textInImageTextView: TextView
    private lateinit var textInImagelayout: MaterialCardView

    private lateinit var cameraPreview: PreviewView
    private lateinit var imageView: ImageView

    private lateinit var appbar: MaterialToolbar

    /**
     * Read image
     */
    private lateinit var file: FileInputStream
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            file = FileInputStream(data?.data?.let {
                context?.contentResolver?.openFileDescriptor(
                    it,
                    "r"
                )?.fileDescriptor
            })
            imageView.setImageURI(data?.data)
        }
    }

    private val textRecognitionViewModel : TextRecognitionViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[TextRecognitionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textRecognitionViewModel.init()
        requestReadPermission()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        textRecognitionViewModel.textInImage.observe(viewLifecycleOwner, this::onTextInImageChanged)
        textRecognitionViewModel.textInImageLayoutVisibility.observe(viewLifecycleOwner, this::onTextInImageLayoutVisible)

        val view = inflater.inflate(R.layout.fragment_text_recognition, container, false)
        bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE

        // bind view
        extractTextButton = view.findViewById(R.id.text_recognition_extract_button)
        closeDialogButton = view.findViewById(R.id.text_recognition_close)
        copyToClipBoardButton = view.findViewById(R.id.text_recognition_copy_to_clipboard)
        textInImageTextView = view.findViewById(R.id.text_recognition_text_in_image)
        textInImagelayout = view.findViewById(R.id.text_recognition_text_in_image_layout)
        appbar = view.findViewById(R.id.appbar)

        cameraPreview = view.findViewById(R.id.text_recognition_preview_view)
        imageView = view.findViewById(R.id.text_recognition_image_view)


        // set on click listener to button
        extractTextButton.setOnClickListener {
            when {
                imageView.visibility == View.VISIBLE -> {
                    val bitmap = imageView.drawable.toBitmap()
                    textRecognitionViewModel.runTextRecognition(bitmap)
                }
                cameraPreview.bitmap != null -> {
                    imageView.visibility = View.VISIBLE
                    val bitmap = cameraPreview.bitmap
                    imageView.setImageBitmap(cameraPreview.bitmap)
                    textRecognitionViewModel.runTextRecognition(bitmap!!)
                }
            }
        }

        copyToClipBoardButton.setOnClickListener {
            val copiedText = textInImageTextView.text.toString()
            if (copiedText.isNotEmpty()) {
                textRecognitionViewModel.copyToClipboard(copiedText, requireContext())
            }
            Toast.makeText(requireContext(), "Sao chép chữ thành công", Toast.LENGTH_SHORT).show()
        }

        closeDialogButton.setOnClickListener {
            textInImagelayout.visibility = View.GONE
        }

        appbar.title = "Quét chữ từ ảnh"
        appbar.inflateMenu(R.menu.top_app_bar_text_recognition_fragment_menu)
        appbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_arrow_back_24, null)
        appbar.setNavigationOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        appbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.text_recognition_app_bar_camera -> {
                    Log.d(TAG, "app bar camera")
                    imageView.visibility = View.GONE
                    true
                }
                R.id.text_recognition_app_bar_gallery -> {
                    textInImagelayout.visibility = View.GONE
                    // TODO: open Image Picker
                    if (isReadPermissionGranted()) {
                        getImage()
                    } else {
                        val toast = Toast.makeText(requireContext(), "Chưa cấp quyền truy cập vào thư viện", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                    //
                    true
                }
                else -> false
            }
        }

        textRecognitionViewModel.startCamera(requireContext(), this, cameraPreview.surfaceProvider)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView?.visibility = View.VISIBLE
        textRecognitionViewModel.shutdown()
    }

    private fun onTextInImageChanged(text: String) {
        textInImageTextView.text = text
        Linkify.addLinks(textInImageTextView, Linkify.WEB_URLS)
    }

    private fun onTextInImageLayoutVisible(visibility: Int) {
        textInImagelayout.visibility = visibility
    }

    private fun requestReadPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    private fun isReadPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        pickImageLauncher.launch(Intent.createChooser(intent, "Image Title"))
    }
}