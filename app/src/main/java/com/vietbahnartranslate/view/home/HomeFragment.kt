package com.vietbahnartranslate.view.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.mlkit.vision.text.TextRecognizer
import com.vietbahnartranslate.R
import com.vietbahnartranslate.model.source.remote.FirebaseConnector
import com.vietbahnartranslate.utils.KeyboardUtils
import com.vietbahnartranslate.view.MainActivity
import com.vietbahnartranslate.viewmodel.home.HomeViewModel


class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    private val MY_PERMISSIONS_REQUEST_CAMERA: Int = 101

    private val homeViewModel : HomeViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[HomeViewModel::class.java]
    }

    private lateinit var vietnameseInputText: EditText
    private lateinit var bahnaricOutputText: TextView
    private lateinit var translateButton: ImageButton
    private lateinit var cameraButton: ImageButton
    private lateinit var speakerButton: ImageButton

    private lateinit var vietnameseConstraintLayout: ConstraintLayout

    /**
     * Request Permission for camera
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCameraPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Observe LiveData
         */
        Log.d(TAG, "onCreateView()")
        homeViewModel.translatedBahnaric.observe(viewLifecycleOwner, this::bahnaricChanged)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        vietnameseInputText = view.findViewById(R.id.vietnamese_input_text)
        bahnaricOutputText = view.findViewById(R.id.bahnaric_output_text)
        translateButton = view.findViewById(R.id.button_translate)
        cameraButton = view.findViewById(R.id.button_camera)
        speakerButton = view.findViewById(R.id.button_speaker)
        vietnameseConstraintLayout = view.findViewById(R.id.constraint_layout_vietnamese)

        vietnameseInputText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO("Not yet implemented")
                //homeViewModel.translate(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, " afterTextChanged ${s.toString()}")

                // If string is not empty, translate and change Translate button can be touched
                if (!s.isNullOrBlank()) {
                    homeViewModel.translate(s.toString())
                }
                else {
                    speakerButton.visibility = View.GONE
                    translateButton.visibility = View.GONE
                }
            }

        })

        vietnameseInputText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) KeyboardUtils.onKeyboardVisible(activity as MainActivity)
            else KeyboardUtils.onKeyboardInvisible(v, activity as MainActivity)
        }

        translateButton.visibility = View.GONE

        translateButton.setOnClickListener {
            // TODO: save Translation to History and back to Full screen, hide keyboard
            // TODO: check input text is empty, if empty button cannot be touched
            Log.d(TAG, "onTranslateButtonClick")
            KeyboardUtils.onKeyboardInvisible(it, activity as MainActivity)
            homeViewModel.onAddToFavouriteButtonClick(vietnameseInputText.text.toString())
            Toast.makeText(requireContext(), "Thêm vào lịch sử thành công", Toast.LENGTH_SHORT).show()
        }

        speakerButton.setOnClickListener {
            homeViewModel.speak()
        }

        cameraButton.setOnClickListener {
            onCameraClick()
        }

        return view
    }

    private fun bahnaricChanged(text: String) {
        Log.d(TAG, "bahnaric changed $text")
        bahnaricOutputText.text = text
        if (text.isNotBlank()) {
            speakerButton.visibility = View.VISIBLE
            translateButton.visibility = View.VISIBLE
        }
    }

    private fun onCameraClick() {
        // Open Camera Fragment
        if (isCameraPermissionGranted()) {
            findNavController().navigate(
                R.id.action_home_fragment_to_text_recognition_fragment
            )
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            MY_PERMISSIONS_REQUEST_CAMERA
        )
    }

    private fun isCameraPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }
}