package com.findparking.app.features.main.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.findparking.app.R
import com.findparking.app.data.entities.CollectionWithBooks
import com.findparking.app.viewholders.HomePageViewHolder

class HomeAdapter: RecyclerView.Adapter<HomePageViewHolder>() {

    private var placesList = mutableListOf<CollectionWithBooks>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_home_collection, parent, false)
        return HomePageViewHolder(view)
    }

    override fun getItemCount(): Int = placesList.size

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        holder.bind(placesList[position])
    }

    fun setList(newList: List<CollectionWithBooks>) {
        if (placesList.isEmpty()) {
            placesList.addAll(newList)
            notifyItemRangeInserted(0, placesList.size)
        } else {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return placesList[oldItemPosition].collection._id == newList[newItemPosition].collection._id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val new = newList[newItemPosition]
                    val old = placesList[oldItemPosition]
                    return old.collection.title == new.collection.title &&
                            old.collection.image == new.collection.image
                    //todo add all fields
                }

                override fun getOldListSize() = placesList.size
                override fun getNewListSize() = newList.size
            })
            placesList.clear()
            placesList.addAll(newList)
            diff.dispatchUpdatesTo(this)
        }
    }

}