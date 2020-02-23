package com.tdl.recipee.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by loc.ta on 2/22/2020.
 */
abstract class GenericTypeAdapter<T>(
    private val list: MutableList<T>,
    private val layoutResId: Int
) : RecyclerView.Adapter<GenericTypeAdapter.ViewHolder>() {

    protected abstract fun onItemClick(itemView: View, position: Int)

    protected open fun View.bindViewHolder(item: T) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ViewHolder(view).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(itemView, adapterPosition)
                }
            }
        }
    }

    fun set(items: MutableList<T>) {
        this.list.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.itemView.bindViewHolder(list[position])

    override fun getItemCount() = list.size

    fun getItems(): List<T> = Collections.unmodifiableList(list)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}