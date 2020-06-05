package com.buct.bookticket.ui.User

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buct.bookticket.util.Get
import com.buct.bookticket.util.Post
import com.buct.bookticket.util.userinfo
import com.buct.bookticket.util.userinnfo

class NotificationsViewModel : ViewModel() {

    private val _logout=MutableLiveData<String>()
    private val _userinfo=MutableLiveData<userinfo>()
    fun updatebalance(context: Context,user:String):LiveData<userinfo>{
        Thread(Runnable {
            val res=Get(context,"http://49.233.81.150:9090/service/balanceChange?name="+user)
            System.out.println("刷新结果为"+res)
            if(res?.toInt()==1){
                val res=Get(context,"http://49.233.81.150:9090/service/getuserinfo?name="+user)
                Log.d("userinfo", "GetInfo: "+res)
                val userinfo=userinnfo(res.toString())
                _userinfo.postValue(userinfo)
            }else{
                _userinfo.postValue(null)
            }
        }).start()
        return _userinfo
    }
    fun GetInfo(context: Context):LiveData<userinfo>{
        val info = context.getSharedPreferences(
            "info", Context.MODE_PRIVATE)
        Thread(Runnable {
            val user=info.getString("user",null)
            val res=Get(context,"http://49.233.81.150:9090/service/getuserinfo?name="+user)
            Log.d("userinfo", "GetInfo: "+res)
            val userinfo=userinnfo(res.toString())
            _userinfo.postValue(userinfo)
        }).start()
        return _userinfo
    }
    fun Logout(context: Context):LiveData<String>{
        Thread(Runnable {
            val res=Get(context,"http://49.233.81.150:9090/api/loginout")
            if(res.equals("登出成功")){
                val info = context.getSharedPreferences(
                    "info", Context.MODE_PRIVATE)
                info.edit().putBoolean("islogin",false).apply()
                info.edit().putString("cookie","").apply()
                info.edit().putString("provice","").apply()
                _logout.postValue("1")
            }else{
                _logout.postValue("0")
            }
        }).start()
        return _logout
    }
}