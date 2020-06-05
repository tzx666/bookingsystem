package com.buct.bookticket.ui.home

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.buct.bookticket.R
import com.buct.bookticket.ui.User.NotificationsViewModel
import com.buct.bookticket.util.*
import org.json.JSONObject

class ConfirmTicket : Fragment() {
    val args: ConfirmTicketArgs by navArgs()
    companion object {
        fun newInstance() = ConfirmTicket()
    }
    private lateinit var schduleid:TextView //航班号
    private lateinit var date:TextView //日期
    private lateinit var pretakeoff:TextView //起飞时间
    private lateinit var preland:TextView //降落时间
    private lateinit var takeoff:TextView //起飞机场
    private lateinit var land:TextView //降落机场
    private lateinit var typeplane:TextView //舱位
    private lateinit var price:TextView //价格
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var USER:TextView
    private lateinit var cost:TextView
    private lateinit var isuseful:TextView
    private lateinit var switch: Switch
    private var realcost=0.0;
    private  var  isusepoint:Boolean=false
    private var balance:Double=0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirm_ticket_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data=args.data
        val types = args.type
        System.out.println(data.toString()+types)
        viewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        schduleid=view?.findViewById(R.id.textView29) as TextView
        date=view?.findViewById(R.id.textView37) as TextView
        pretakeoff=view?.findViewById(R.id.textView32) as TextView
        preland=view?.findViewById(R.id.textView33) as TextView
        takeoff=view?.findViewById(R.id.textView34) as TextView
        land=view?.findViewById(R.id.textView35) as TextView
        typeplane=view?.findViewById(R.id.textView30) as TextView
            price=view?.findViewById(R.id.textView31) as TextView
        USER=view?.findViewById(R.id.textView26) as TextView
        isuseful=view?.findViewById(R.id.textView27) as TextView
        cost=view?.findViewById(R.id.textView28) as TextView
        switch=view?.findViewById(R.id.switch1) as Switch
        viewModel.GetInfo(requireContext()).observe(viewLifecycleOwner, Observer {
            it->balance=it.account_balance.toDouble()
            if(it.points.toDouble()> 1000.0){
                switch.isClickable=true
            isuseful.isVisible=false
            }else{
            switch.isClickable=false
            isuseful.isVisible=true
            }
        })
        switch.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                cost.setText("实际支付"+(price.text.toString().toDouble()-100))
                realcost=price.text.toString().toDouble()-100
                isusepoint=true
                // The toggle is enabled
            } else {
                isusepoint=false
                cost.setText("实际支付"+price.text)
                realcost=price.text.toString().toDouble()
                // The toggle is disabled
            }
    }
        val info = activity?.getSharedPreferences(
            "info", Context.MODE_PRIVATE)
        val user=info?.getString("user",null)
        USER.setText(user)
        schduleid.setText(data.ComID+data.ID)
        date.setText(sdf3.format(sdf1.parse(data.pre_takeoff)))
        pretakeoff.setText(sdf2.format(sdf1.parse(data.pre_takeoff)))
        preland.setText(sdf2.format(sdf1.parse(data.pre_landing)))

        takeoff.setText(data.name)
        land.setText(data.name1)
        if(types==1){
            typeplane.setText("头等舱")
            price.setText(data.tickctA_price)
            cost.setText("实际支付"+data.tickctA_price)
            realcost=price.text.toString().toDouble()
        }else if(types==2){
            typeplane.setText("商务舱")
            price.setText(data.tickctB_price)
            cost.setText("实际支付"+data.tickctB_price)
            realcost=price.text.toString().toDouble()
        }else{
            typeplane.setText("经济舱")
            price.setText(data.tickctC_price)
            cost.setText("实际支付"+data.tickctC_price)
            realcost=price.text.toString().toDouble()
        }
        val processbar=ProgressBar(activity)
        processbar.setIndeterminate(true)
        val button:Button=view?.findViewById(R.id.button13)as Button
        button.setOnClickListener {
            val builder:AlertDialog.Builder= AlertDialog.Builder(activity).setView(processbar)
            if(balance<realcost){
                Toast.makeText(activity,"余额不足", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dialog=builder.show()
            Thread(Runnable {
                var object1=JSONObject()
                object1.put("type",types)
                object1.put("id",data.ID);
                object1.put("price",realcost);
                object1.put("user",user);
                val res= Post(requireContext(),"http://49.233.81.150:9090/servier/confirmticket",object1.toString())
                System.out.println(res)
                if(res.length>2){
                    System.out.println("预定成功")
                    if(isusepoint){
                        val res1=Get(requireContext(),"http://49.233.81.150:9090/service/Subpoint?name="+user+"&point=1000")
                        if(res1?.toInt()==1){
                            Get(requireContext(),"http://49.233.81.150:9090/service/Addpoint?name="+user)
                            activity?.runOnUiThread(Runnable {
                                Toast.makeText(activity,"预定成功", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            })
                        }else{
                            activity?.runOnUiThread(Runnable {
                                Toast.makeText(activity,"服务故障", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            })
                        }
                    }else{
                        activity?.runOnUiThread(Runnable {
                            Toast.makeText(activity,"预定成功", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        })
                    }
                }else{
                    activity?.runOnUiThread(Runnable {
                        Toast.makeText(activity,"服务故障", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
                }

            }).start()
        }
        // TODO: Use the ViewModel
    }

}