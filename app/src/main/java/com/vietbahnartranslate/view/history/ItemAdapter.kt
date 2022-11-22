package com.vietbahnartranslate.view.history

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    private val mData = mutableListOf<ModelItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ModelItem>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ItemCustomView(parent.context)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ModelItem.WORD_ITEM_TYPE -> {
                val item = mData[position]
                if (item is WordModelItem) {
                    val data = item.data
                    holder.customView.setData(data)
                }
            }
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun getItemViewType(position: Int): Int = mData[position].itemType

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val customView: ItemCustomView = itemView as ItemCustomView
    }

    open class ModelItem(val itemType: Int) {
        companion object {
            const val WORD_ITEM_TYPE = 0
        }
    }

    class WordModelItem(val data: WordModel) : ModelItem(WORD_ITEM_TYPE)
}