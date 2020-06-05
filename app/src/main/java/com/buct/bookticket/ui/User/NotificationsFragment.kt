package com.buct.bookticket.ui.User

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.buct.bookticket.R
import com.google.gson.internal.`$Gson$Types`.equals
import java.util.Arrays.equals
import java.util.Objects.equals

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private  lateinit var user:String;
    private  var balance:Double=0.0;
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val buttondingdan=view?.findViewById<Button>(R.id.button10)
        buttondingdan?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_notifications_to_itemFragment)
        }
        val info = activity?.getSharedPreferences(
            "info", Context.MODE_PRIVATE)
        val islogin=info?.getBoolean("islogin",false)
        if(!islogin!!){
            view?.findNavController()?.navigate(R.id.action_navigation_notifications_to_loginFragment)
        }
        val textView:TextView=view?.findViewById(R.id.textView21) as TextView //用户名
        val textView1:TextView=view?.findViewById(R.id.textView22) as TextView //余额
        val textView2:TextView=view?.findViewById(R.id.textView23) as TextView //等级
        val textView3:TextView=view?.findViewById(R.id.textView24) as TextView //积分
         user= info.getString("user",null) as String
        val button:Button=view?.findViewById(R.id.button12) as Button //关于
        button.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigation_notifications_to_about)
        }
        notificationsViewModel.GetInfo(requireContext()).observe(viewLifecycleOwner, Observer {
                it->balance=it.account_balance.toDouble()
                    textView.setText(user)
                    textView1.setText("余额: "+it.account_balance)
                    if(it.level.equals("1")){
                        textView2.setText("等级： 普通会员")
                    }else if(it.level.equals("2")){
                        textView2.setText("等级： 银卡会员")
                    }else{
                        textView2.setText("等级： 金卡会员")
                    }
                    textView3.setText("积分： "+it.points)
        })
        val button1:Button= (view?.findViewById(R.id.button9)) as Button;//退出登录
        button1.setOnClickListener {
            notificationsViewModel.Logout(requireContext()).observe(viewLifecycleOwner, Observer<String> {
               res-> if(res.equals("1")){
                    Toast.makeText(activity,"登出成功",Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.action_navigation_notifications_to_loginFragment)
                }else{
                    Toast.makeText(requireContext(),"登出失败",Toast.LENGTH_SHORT).show()
                }
            })
        }
        val button2:Button= (view?.findViewById(R.id.button)) as Button;
        button2.setOnClickListener {
            System.out.println("将要领钱的是user是"+user)
            if(balance>9999){
                Toast.makeText(requireContext(),"再玩就玩坏了",Toast.LENGTH_SHORT).show()
            }else{
                notificationsViewModel.updatebalance(requireContext(),user).observe(viewLifecycleOwner,
                    Observer {
                            it->
                        if(it==null){
                            Toast.makeText(requireContext(),"可能被玩坏了",Toast.LENGTH_SHORT).show()
                        }else{
                            textView.setText(user)
                            textView1.setText("余额: "+it.account_balance)
                            if(it.level.equals("1")){
                                textView2.setText("等级： 普通会员")
                            }else if(it.level.equals("2")){
                                textView2.setText("等级： 银卡会员")
                            }else{
                                textView2.setText("等级： 金卡会员")
                            }
                            textView3.setText("积分： "+it.points)
                            Toast.makeText(requireContext(),"领取成功",Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}
