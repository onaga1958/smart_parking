package com.findparking.app.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.findparking.app.data.entities.CollectionWithBooks
import kotlinx.android.synthetic.main.item_home_collection.view.*

class HomePageViewHolder(
    itemView: View
    ): RecyclerView.ViewHolder(itemView) {
    fun bind(collectionWithBooks: CollectionWithBooks) {
        itemView.tvCollectionTitle.text = collectionWithBooks.collection.title
    }
}