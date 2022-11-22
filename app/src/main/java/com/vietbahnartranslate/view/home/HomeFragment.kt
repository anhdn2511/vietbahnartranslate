package com.vietbahnartranslate.view.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.vietbahnartranslate.R
import com.vietbahnartranslate.model.repository.TranslateRepo
import com.vietbahnartranslate.viewmodel.home.HomeViewModel


class HomeFragment : Fragment() {
    private val TAG = "Home Fragment"

    private val homeViewModel : HomeViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[HomeViewModel::class.java]
    }

    private lateinit var vietnameseInputText: EditText
    private lateinit var bahnaricOutputText: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Observe LiveData
         */
        homeViewModel.translatedBahnaric.observe(viewLifecycleOwner, this::bahnaricChanged)
        homeViewModel.playSpeech("Lơ̆m jơnang noh ksô")

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        vietnameseInputText = view.findViewById(R.id.vietnamese_input_text)
        bahnaricOutputText = view.findViewById(R.id.bahnaric_output_text)

        vietnameseInputText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // TODO("Not yet implemented")
                homeViewModel.translate(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, " afterTextChanged ${s.toString()}")
                homeViewModel.translate(s.toString())
            }

        })
        return view
    }

    private fun bahnaricChanged(text: String) {
        Log.d(TAG, "bahnaric changed $text")
        bahnaricOutputText.text = text
    }
}