package com.tdl.recipee.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by loc.ta on 2/22/2020.
 */
class BaseAdapter<T>(private val list: MutableList<T>,
                     layoutResId: Int,
                     private val bindHolder: View.(T) -> Unit,
                     private val itemClick: T.() -> Unit = {}) : GenericTypeAdapter<T>(list, layoutResId) {

    override fun onItemClick(itemView: View, position: Int) {
        list[position].itemClick()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.bindHolder(list[position])
    }
}

fun <T> RecyclerView.setUp(items: MutableList<T>,
                           layoutResId: Int,
                           bindHolder: View.(T) -> Unit,
                           itemClick: T.() -> Unit = {},
                           manager: RecyclerView.LayoutManager = LinearLayoutManager(this.context)
): BaseAdapter<T> {
    layoutManager = manager
    return BaseAdapter(items, layoutResId, bindHolder, itemClick)
        .apply {
            adapter = this
        }
}