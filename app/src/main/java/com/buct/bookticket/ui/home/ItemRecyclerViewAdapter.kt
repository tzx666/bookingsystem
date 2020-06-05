package com.buct.bookticket.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.buct.bookticket.R

import com.buct.bookticket.ui.home.dummy.DummyContent.DummyItem
import com.buct.bookticket.util.dingdan

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ItemRecyclerViewAdapter(
    private val values: List<dingdan>
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.costView.setText("价格： "+item.cost)
        holder.hashView.setText("订单号： "+item.tranhash)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val costView: TextView = view.findViewById(R.id.textView41)
        val hashView: TextView = view.findViewById(R.id.textView40)

    }
}