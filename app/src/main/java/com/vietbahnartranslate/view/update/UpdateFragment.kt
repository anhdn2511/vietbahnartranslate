package com.vietbahnartranslate.view.update

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.vietbahnartranslate.R
import com.vietbahnartranslate.utils.KeyboardUtils
import com.vietbahnartranslate.view.MainActivity
import com.vietbahnartranslate.viewmodel.update.UpdateViewModel
import com.vietbahnartranslate.viewmodel.home.HomeViewModel

class UpdateFragment : Fragment() {
    private val TAG = "UpdateFragment"

    private val updateViewModel: UpdateViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[UpdateViewModel::class.java]
    }

    private lateinit var vietnameseInputText: EditText
    private lateinit var bahnaricInputText: EditText
    private lateinit var updateButton: Button

    private lateinit var vietnameseConstraintLayout: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")

        /* variables used to contain input from user */
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        vietnameseInputText = view.findViewById(R.id.vietnamese_input_text)
        bahnaricInputText = view.findViewById(R.id.bahnaric_input_text)
        updateButton = view.findViewById(R.id.button_update)
        vietnameseConstraintLayout = view.findViewById(R.id.constraint_layout_vietnamese)

        /* functions to handle user input */
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
                    // TODO("Not yet implemented")
                }
                else {
                    updateButton.visibility = View.GONE
                }
            }

        })

        vietnameseInputText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) KeyboardUtils.onKeyboardVisible(activity as MainActivity)
            else KeyboardUtils.onKeyboardInvisible(v, activity as MainActivity)
        }

        bahnaricInputText.addTextChangedListener(object: TextWatcher {
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
                    // TODO("Not yet implemented")
                }
                else {
                    updateButton.visibility = View.GONE
                }
            }

        })

        bahnaricInputText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) KeyboardUtils.onKeyboardVisible(activity as MainActivity)
            else KeyboardUtils.onKeyboardInvisible(v, activity as MainActivity)
        }

        updateButton.visibility = View.GONE

        updateButton.setOnClickListener {
            // TODO: check input text is empty, if empty button cannot be touched
            updateViewModel.update(vietnameseInputText.toString(), bahnaricInputText.toString())
            Log.d(TAG, "onTranslateButtonClick")
            KeyboardUtils.onKeyboardInvisible(it, activity as MainActivity)
        }

        return inflater.inflate(R.layout.fragment_update, container, false)
    }

}