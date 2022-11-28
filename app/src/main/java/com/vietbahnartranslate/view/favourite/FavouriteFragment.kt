package com.vietbahnartranslate.view.favourite

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.vietbahnartranslate.R
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.viewmodel.favourite.FavouriteViewModel

class FavouriteFragment : Fragment() {
    private val TAG = "Favourite Fragment"
    private val mFavouriteViewModel: FavouriteViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[FavouriteViewModel::class.java]
    }
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = FavouriteItemAdapter(this)

    /**
     * Temp to test group, config later
     */
    private lateinit var button1: Button
    private lateinit var button2: Button

    private var checkButton1 = true
    private var checkButton2 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Observe LiveData
         */
        Log.d(TAG, "onCreateView()")
        mFavouriteViewModel.initFavouriteList()
        mFavouriteViewModel.favouriteList.observe(viewLifecycleOwner, this::favouriteListChanged)

        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        mRecyclerView = view.findViewById(R.id.favourite_recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

        button1 = view.findViewById(R.id.button_1)
        button2 = view.findViewById(R.id.button_2)

        button1.setOnClickListener {
            toggleColorGroup()
        }

        button2.setOnClickListener {
            toggleColorGroup()
        }

        return view
    }

    private fun favouriteListChanged(_data: List<Translation>) {
        val data = mutableListOf<FavouriteItemAdapter.ModelItem>()
        _data.forEach {
            data.add(FavouriteItemAdapter.TranslationModelItem(it))
        }
        mAdapter.setData(data)
    }

    fun onFavouriteClick(id: Int?, isFavourite: Boolean, checkButton1: Boolean) {
        mFavouriteViewModel.onFavouriteClick(id, isFavourite)
    }

    private fun toggleColorGroup() {
        checkButton1 = !checkButton1
        checkButton2 = !checkButton2
        button1.backgroundTintList =
            if(checkButton1) ContextCompat.getColorStateList(requireContext(), R.color.green_primary)
            else ContextCompat.getColorStateList(requireContext(), R.color.green_secondary)
        button2.backgroundTintList =
            if(checkButton2) ContextCompat.getColorStateList(requireContext(), R.color.green_primary)
            else ContextCompat.getColorStateList(requireContext(), R.color.green_secondary)
        if (checkButton1) {
            mFavouriteViewModel.toggleFavouriteList(0)
        }
        else {
            mFavouriteViewModel.toggleFavouriteList(1)
        }
    }
}