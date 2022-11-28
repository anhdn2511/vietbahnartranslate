package com.vietbahnartranslate.view.history

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.vietbahnartranslate.R
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.viewmodel.history.HistoryViewModel

class HistoryFragment : Fragment() {
    private val TAG = "History Fragment"
    private val mHistoryViewModel: HistoryViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[HistoryViewModel::class.java]
    }
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = ItemAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /**
         * Observe LiveData
         */
        Log.d(TAG, "onCreateView()")
        mHistoryViewModel.initHistory()
        mHistoryViewModel.historyList.observe(viewLifecycleOwner, this::historyChanged)

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        mRecyclerView = view.findViewById(R.id.history_recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayout.VERTICAL))

        return view
    }

    private fun historyChanged(_data: List<Translation>) {
        val data = mutableListOf<ItemAdapter.ModelItem>()
        _data.forEach {
            data.add(ItemAdapter.TranslationModelItem(it))
        }
        mAdapter.setData(data)
    }

    fun onFavouriteClick(id: Int?, isFavourite: Boolean) {
        if (isFavourite) {
            val builder = AlertDialog.Builder(requireContext())
            builder
                .setTitle("Chọn thư mục phù hợp")
                .setItems(arrayOf("Từ đã lưu", "Từ mới")) { dialog, which ->
                    when (which) {
                        0 -> {
                            mHistoryViewModel.onFavouriteClick(id, true, 0)
                        }
                        1 -> {
                            mHistoryViewModel.onFavouriteClick(id, true, 1)
                        }
                    }
                }
            builder.create()
            builder.show()
        }
        mHistoryViewModel.onFavouriteClick(id, false, null)
    }
}