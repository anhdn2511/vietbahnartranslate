package com.vietbahnartranslate.view.update

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vietbahnartranslate.R
import com.vietbahnartranslate.utils.KeyboardUtils
import com.vietbahnartranslate.view.MainActivity
import com.vietbahnartranslate.viewmodel.update.UpdateViewModel


class UpdateFragment : Fragment() {
    private val TAG = "UpdateFragment"

    private val updateViewModel: UpdateViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[UpdateViewModel::class.java]
    }

    private lateinit var vietnameseInputText: EditText
    private lateinit var bahnaricInputText: EditText
    private lateinit var updateButton: Button
    private lateinit var feedbackButton: Button

    private lateinit var vietnameseConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")
        updateViewModel.updatedBahnaric.observe(viewLifecycleOwner, this::bahnaricChanged)

        /* variables used to contain input from user */
        val view = inflater.inflate(R.layout.fragment_update, container, false)
        vietnameseInputText = view.findViewById(R.id.vietnamese_input_text)
        bahnaricInputText = view.findViewById(R.id.bahnaric_input_text)
        updateButton = view.findViewById(R.id.button_update)
        feedbackButton = view.findViewById(R.id.button_feedback)
        vietnameseConstraintLayout = view.findViewById(R.id.constraint_layout_vietnamese)

        /* functions to handle user input */

        vietnameseInputText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        vietnameseInputText.setTextIsSelectable(true);

        bahnaricInputText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        bahnaricInputText.setTextIsSelectable(true);

        val ic1: InputConnection = vietnameseInputText.onCreateInputConnection(EditorInfo())
        keyboard.setInputConnection(ic1)

        val ic2: InputConnection = bahnaricInputText.onCreateInputConnection(EditorInfo())
        keyboard.setInputConnection(ic2)

        Log.d(TAG, "inputting text" )
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
                }
                else {
                    updateButton.visibility = View.GONE
                    feedbackButton.visibility = View.GONE
                }
            }

        })

        vietnameseInputText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) KeyboardUtils.onKeyboardVisible(activity as MainActivity)
            else KeyboardUtils.onKeyboardInvisible(v, activity as MainActivity)
        }

        updateButton.visibility = View.VISIBLE
        feedbackButton.visibility = View.VISIBLE

        updateButton.setOnClickListener {
            // TODO: check input text is empty, if empty button cannot be touched
            Log.d(TAG, "onUpdateButtonClick")
            KeyboardUtils.onKeyboardInvisible(it, activity as MainActivity)
            updateViewModel.update(vietnameseInputText.text.toString(), bahnaricInputText.text.toString())
        }

        feedbackButton.setOnClickListener {
            // TODO: check input text is empty, if empty button cannot be touched
            Log.d(TAG, "onFeedbackButtonClick")
            KeyboardUtils.onKeyboardInvisible(it, activity as MainActivity)
            updateViewModel.feedback(vietnameseInputText.text.toString(), bahnaricInputText.text.toString())
        }

        return view
    }

    private fun bahnaricChanged(text: String) {
        Log.d(TAG, "bahnaric changed $text")
        if (text.isNotBlank()) {
            updateButton.visibility = View.VISIBLE
            feedbackButton.visibility = View.VISIBLE
        }
    }

}