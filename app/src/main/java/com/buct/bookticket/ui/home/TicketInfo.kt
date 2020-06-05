package com.buct.bookticket.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buct.bookticket.R
import com.buct.bookticket.util.scheduleData
import com.buct.bookticket.util.tickinfo
import com.buct.bookticket.util.toTickInfo
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TicketInfo : Fragment() {

    companion object {
        fun newInstance() = TicketInfo()
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: TicketInfoViewModel
    private lateinit var textView: TextView
    private var ticketinfores=ArrayList<scheduleData>()
    private lateinit var viewAdapter: ticketAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ticket_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView=view?.findViewById(R.id.recycleview2)as RecyclerView
        viewManager = LinearLayoutManager(activity)
        viewAdapter = ticketAdapter(ticketinfores)
        textView=view?.findViewById(R.id.textView25) as TextView
        recyclerView.adapter=viewAdapter
        recyclerView.layoutManager=viewManager
        EventBus.getDefault().register(this)
        viewModel = ViewModelProviders.of(this).get(TicketInfoViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewAdapter.setOnItemClickListener(object: ticketAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val data=ticketinfores.get(position)
                view?.findNavController()?.navigate(TicketInfoDirections.actionTicketInfoToShowTicketInfo(data))
                Toast.makeText(activity,"click " + position + " item", Toast.LENGTH_SHORT).show()
            }

            override fun onItemLongClick(view: View, position: Int) {
                //showToast("long click " + position + " item")
            }
        })

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode=ThreadMode.MAIN,sticky = true)
    fun getticketinfo(tickinfo1: tickinfo){
        ticketinfores.clear()
        System.out.println(tickinfo1.message)
        if(tickinfo1.message?.length!! <5){
            textView.isVisible=true
            recyclerView.isVisible=false
        }else{
            textView.isVisible=false
            recyclerView.isVisible=true
            var  ticketinfores1=toTickInfo(tickinfo1.message.toString())
            ticketinfores.addAll(ticketinfores1)
            viewAdapter.notifyDataSetChanged()
            for(it in ticketinfores){
                System.out.println(it.Pid)
            }
        }
    }
}