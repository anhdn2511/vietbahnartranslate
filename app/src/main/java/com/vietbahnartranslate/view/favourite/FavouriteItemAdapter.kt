package com.vietbahnartranslate.view.favourite

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.view.history.ItemCustomView

class FavouriteItemAdapter(private val favouriteFragment: FavouriteFragment) : RecyclerView.Adapter<FavouriteItemAdapter.ViewHolder>(){
    private val mData = mutableListOf<ModelItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ModelItem>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = FavouriteItemCustomView(this, parent.context)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ModelItem.WORD_ITEM_TYPE -> {
                val item = mData[position]
                if (item is TranslationModelItem) {
                    val data = item.data
                    holder.customView.setData(data)
                }
            }
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun getItemViewType(position: Int): Int = mData[position].itemType

    fun onFavouriteClick(id: Int?, isFavourite: Boolean) {
        //favouriteFragment.onFavouriteClick(id, isFavourite)
    }

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val customView: FavouriteItemCustomView = itemView as FavouriteItemCustomView
    }

    open class ModelItem(val itemType: Int) {
        companion object {
            const val WORD_ITEM_TYPE = 0
        }
    }

    class TranslationModelItem(val data: Translation) : ModelItem(WORD_ITEM_TYPE)
}