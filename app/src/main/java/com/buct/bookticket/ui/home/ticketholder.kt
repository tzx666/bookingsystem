package com.buct.bookticket.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.buct.bookticket.R
import com.buct.bookticket.util.scheduleData
import java.text.SimpleDateFormat

class ticketAdapter(private val myDataset: ArrayList<scheduleData>): RecyclerView.Adapter<ticketAdapter.viewholder>() {
    private val sdf1 =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ")
    private val sdf2 =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val sdf3 =
        SimpleDateFormat("MM月dd日")
    private lateinit var onItemClickListener:OnItemClickListener

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }
    class viewholder(view: View) : RecyclerView.ViewHolder(view) {
        val pretakeoff=view.findViewById<TextView>(R.id.textView10)
        val preland=view.findViewById<TextView>(R.id.textView11)
        val takeoffplace=view.findViewById<TextView>(R.id.textView12)
        val landplace=view.findViewById<TextView>(R.id.textView13)
        val priceC=view.findViewById<TextView>(R.id.textView14)
        var cardView:CardView=view.findViewById<CardView>(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val View = LayoutInflater.from(parent.context)
            .inflate(R.layout.ticket_item, parent, false) as View
        return viewholder(View)
    }

    override fun getItemCount()= myDataset.size

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.pretakeoff.setText(sdf3.format(sdf1.parse(myDataset[position].pre_takeoff)))
        holder.preland.setText(sdf3.format(sdf1.parse(myDataset[position].pre_landing)))
        holder.takeoffplace.setText(myDataset.get(position).name)
        holder.landplace.setText(myDataset.get(position).name1)
        holder.priceC.setText("￥"+myDataset.get(position).tickctC_price)
        holder.cardView.setOnClickListener {
            onItemClickListener.onItemClick(holder.cardView,position)
        }
        holder.cardView.setOnLongClickListener{
            onItemClickListener?.onItemLongClick(holder.itemView, position)
            true
        }
    }

}