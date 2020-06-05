package com.buct.bookticket.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.buct.bookticket.R
import com.buct.bookticket.util.scheduleData
import com.buct.bookticket.util.sdf1
import com.buct.bookticket.util.sdf3

class ShowTicketInfo : Fragment() {

    companion object {
        fun newInstance() = ShowTicketInfo()
    }
    val args:ShowTicketInfoArgs by navArgs()
    private lateinit var viewModel: TicketInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.show_ticket_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data=args.data
        Log.d("ShowTicketInfo", "onActivityCreated: "+data.FullName)
        val com=view?.findViewById<TextView>(R.id.textView8)
        val takeofftime=view?.findViewById<TextView>(R.id.textView9)
        val landtime=view?.findViewById<TextView>(R.id.textView15)
        val takeopre=view?.findViewById<TextView>(R.id.textView16)
        val landpre=view?.findViewById<TextView>(R.id.textView17)
        val pA=view?.findViewById<TextView>(R.id.textView18)
        val pB=view?.findViewById<TextView>(R.id.textView19)
        val pC=view?.findViewById<TextView>(R.id.textView20)
        val btn1=view?.findViewById<TextView>(R.id.button6)
        val btn2=view?.findViewById<TextView>(R.id.button7)
        val btn3=view?.findViewById<TextView>(R.id.button8)
        com?.setText(data.FullName+"承运")
        takeofftime?.setText(sdf3.format(sdf1.parse(data.pre_takeoff)))
        landtime?.setText(sdf3.format(sdf1.parse(data.pre_landing)))
        takeopre?.setText(data.name)
        landpre?.setText(data.name1)

        btn1?.setOnClickListener {
            val action=ShowTicketInfoDirections.actionShowTicketInfoToConfirmTicket(data =data,type = 1 )
            view?.findNavController()?.navigate(action)
        }
        btn2?.setOnClickListener {
            val action=ShowTicketInfoDirections.actionShowTicketInfoToConfirmTicket(data =data,type = 2 )
            view?.findNavController()?.navigate(action)
        }
        btn3?.setOnClickListener {
            val action=ShowTicketInfoDirections.actionShowTicketInfoToConfirmTicket(data =data,type = 3 )
            view?.findNavController()?.navigate(action)
        }
        if(data.tickctA_price.equals("0")){
            btn1?.isClickable=false
            pA?.setText("头等舱： 无余票")
        }
        else{
            pA?.setText("头等舱： ￥"+data.tickctA_price)
        }
        if(data.tickctB_price.equals("0"))
            {
                btn2?.isClickable=false
                pB?.setText("商务舱： 无余票")
            }
        else{
            pB?.setText("商务舱： ￥"+data.tickctB_price)
        }
        if(data.tickctC_price.equals("0"))
            {
                btn3?.isClickable=false
                pC?.setText("经济舱： 无余票")
            }
        else{
            pC?.setText("经济舱： ￥"+data.tickctC_price)
        }
        viewModel = ViewModelProviders.of(this).get(TicketInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}