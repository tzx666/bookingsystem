package com.buct.bookticket.ui.home

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.buct.bookticket.R
import com.buct.bookticket.util.*
import com.codbking.widget.DatePickDialog
import com.codbking.widget.bean.DateType
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.text.SimpleDateFormat


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel;
    private lateinit var recyclerView: RecyclerView;
    private lateinit var viewAdapter: RecyclerView.Adapter<*>;
    private lateinit var viewManager: RecyclerView.LayoutManager;
    private lateinit var  textView:TextView;
    private lateinit var  textView1:TextView;
    private lateinit var  textView2:TextView;
    private  var  arrayprovice=ArrayList<String>()
    private  var  arraycity=ArrayList<String>()
    private lateinit var provice1:String;
    private lateinit var city1:String;
    private lateinit var map:HashMap<String,areaData>
    private lateinit var spinner: Spinner
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner
    private lateinit var map1:HashMap<String,areaData>
    private lateinit var provice2:String;
    private lateinit var city2:String;
    private var myDataset= arrayListOf<Int>(R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4);
    private val sdf1 =
        SimpleDateFormat("yyyy-MM-dd")
    fun showdate(){
        val dialog = DatePickDialog(activity)
        //设置上下年分限制
        dialog.setYearLimt(1)
        //设置标题
        dialog.setTitle("选择时间")
        //设置类型
        dialog.setType(DateType.TYPE_YMD)
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm")
        //设置选择回调
        dialog.setOnChangeLisener(null)
        //设置点击确定按钮回调
        dialog.setOnSureLisener { date ->
            run {
                Log.d(
                    TAG,
                    "onActivityCreated: " + sdf1.format(date))
                textView.setText(
                    sdf1.format(date)
                )
            }
        }
        dialog.show()
    }
    fun showarea(){
        val dialog: AlertDialog = AlertDialog.Builder(activity).setView(view)
            .setPositiveButton("确认") { dialog, which ->
               // Toast.makeText(activity,"你的选择是："+which,Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("取消"){ dialog, which ->
                //Toast.makeText(activity,"你的选择是："+which,Toast.LENGTH_LONG).show()
            }
            .create()
        val view: View =
            LayoutInflater.from(activity)
                .inflate(R.layout.airport_area, null, false)
         spinner=view.findViewById(R.id.spinner)
         spinner1=view.findViewById(R.id.spinner2)
        val startAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arrayprovice)
        val startAdapter1 = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arraycity)
        spinner.adapter=startAdapter
        spinner1.adapter=startAdapter1
        var listener=myItemClickListener()
        var listener1=myItemClickListener2()
        spinner.onItemSelectedListener=listener;
        spinner1.onItemSelectedListener=listener1
        dialog.setView(view)
        dialog.show();
    }
    fun showarea1(){
        val dialog: AlertDialog = AlertDialog.Builder(activity).setView(view)
            .setPositiveButton("确认") { dialog, which ->
               // Toast.makeText(activity,"你的选择是："+which,Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("取消"){ dialog, which ->
               // Toast.makeText(activity,"你的选择是："+which,Toast.LENGTH_LONG).show()
            }
            .create()
        val view: View =
            LayoutInflater.from(activity)
                .inflate(R.layout.airport_area, null, false)
         spinner2=view.findViewById(R.id.spinner)
        spinner3=view.findViewById(R.id.spinner2)
        val startAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arrayprovice)
        val startAdapter1 = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arraycity)
        spinner2.adapter=startAdapter
        spinner3.adapter=startAdapter1
        var listener=myItemClickListener1()
        var listener1=myItemClickListener3()
        spinner2.onItemSelectedListener=listener;
        spinner3.onItemSelectedListener=listener1;
        dialog.setView(view)
        dialog.show();
    }
    /*出发地省份选择*/
    internal inner class myItemClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            provice1=map.get(arrayprovice[position])?.pinyin.toString()

            Thread(Runnable {
                var object1=JSONObject()
                object1.put("pinyin",provice1)
                val res=Post(requireContext(),"http://49.233.81.150:9090/sqltest2",object1.toString())
                System.out.println(res)
                map1= toProviceMap(res)
                arraycity.clear()
                for(it in map1){
                    arraycity.add(it.key)
                }
                activity?.runOnUiThread(Runnable {
                    val startAdapter1 = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arraycity)
                    spinner1.adapter=startAdapter1
                })
            }).start()
           // Toast.makeText(activity,"你的选择是："+map.get(arrayprovice[position])?.pinyin,Toast.LENGTH_LONG).show()
        }
    }
    /*到达地省份选择*/
    internal inner class myItemClickListener1 : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            provice2=map.get(arrayprovice[position])?.pinyin.toString()
            System.out.println(provice2)
            //
            Thread(Runnable {
                var object1=JSONObject()
                object1.put("pinyin",provice2)
                val res=Post(requireContext(),"http://49.233.81.150:9090/sqltest2",object1.toString())
                System.out.println(res)
                map1= toProviceMap(res)
                arraycity.clear()
                for(it in map1){
                    arraycity.add(it.key)
                }
                activity?.runOnUiThread(Runnable {
                    val startAdapter1 = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arraycity)
                    spinner3.adapter=startAdapter1
                })
            }).start()
          //  Toast.makeText(activity,"你的选择是："+arrayprovice[position],Toast.LENGTH_LONG).show()
        }
    }
    /*出发地城市选择*/
    internal inner class myItemClickListener2 : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            city1=arraycity[position]
            activity?.runOnUiThread(Runnable {
                textView1.setText(city1)
            })
         //   Toast.makeText(activity,"你的选择是："+arraycity[position],Toast.LENGTH_LONG).show()
        }
    }
    /*到达地城市选择*/
    internal inner class myItemClickListener3 : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            city2=arraycity[position]
        //    Toast.makeText(activity,"你的选择是："+arraycity[position],Toast.LENGTH_LONG).show()
            activity?.runOnUiThread(Runnable {
                textView2.setText(city2)
            })
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: fragment创建了")
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        root.banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(ImageAdapter(myDataset))
            .setIndicator(CircleIndicator(activity))
            .start()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val info = activity?.getSharedPreferences(
            "info", Context.MODE_PRIVATE)
        val stA=info?.getString("provice",null)
        System.out.println("拿到了"+stA)
        if(stA!!.length>0){
            map=toProviceMap(stA)
            System.out.println(map.size)
            for (it in map){
                arrayprovice.add(it.key)
            }
        }else{
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_loginFragment)
        }
        textView=view?.findViewById(R.id.textView5) as TextView
        textView1=view?.findViewById(R.id.textView6) as TextView
        textView2=view?.findViewById(R.id.textView7) as TextView
        val button:Button=view?.findViewById(R.id.button11) as Button
        /*
        * 航线查询
        * */
        button.setOnClickListener {
          //  Toast.makeText(activity,textView.text.toString()+" "+textView1.text+" "+textView2.text,Toast.LENGTH_SHORT).show()
            var jsonObject=JSONObject()
            jsonObject.put("date",textView.text.toString())
            jsonObject.put("takeoff_place",textView1.text)
            jsonObject.put("land_place",textView2.text)
            Log.d("websearch", jsonObject.toString())
            Thread(Runnable {
               val res=Post(requireContext(),"http://49.233.81.150:9090/service/GetChoosedSchedule",jsonObject.toString())
               // System.out.println(res)
                var tickinfo=tickinfo()
                tickinfo.message=res
                EventBus.getDefault().postSticky(tickinfo)
                activity?.runOnUiThread(
                    Runnable {
                        view?.findNavController()?.navigate(R.id.action_navigation_home_to_ticketInfo)
                    }
                )
            }).start()
        }
        textView.setText(tools.getNow().toString())
        textView.setOnClickListener {
            showdate()
        }
        textView1.setOnClickListener {
            showarea()
        }
        textView2.setOnClickListener {
            showarea1()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onActivityCreated: 活动创建了")
        /*判断是否登录*/

    }
    @Subscribe(threadMode =ThreadMode.MAIN)
    fun getInfo(msg: Islogin){
        Log.d(TAG, "getInfo: "+msg.message )
        if(msg.message.equals("1")){
            val info = activity?.getSharedPreferences(
                "info", Context.MODE_PRIVATE)
            val islogin=info?.getBoolean("islogin",false)
            val stA=info?.getString("provice",null)
            if(stA!=null){
                 map=toProviceMap(stA)
                System.out.println(map.size)
                for (it in map){
                    arrayprovice.add(it.key)
                }
                System.out.println(arrayprovice.size)
            }
            Log.d(TAG, "islogin:"+islogin)
            if(!islogin!!){
                view?.findNavController()?.navigate(R.id.action_navigation_home_to_loginFragment)
            }

        }else{
            view?.findNavController()?.navigate(R.id.action_navigation_home_to_loginFragment)
        }
    }
}
