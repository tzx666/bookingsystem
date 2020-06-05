package com.buct.bookticket.ui.userfragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buct.bookticket.util.Get
import com.buct.bookticket.util.Islogin
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.IOException

class LoginViewModel : ViewModel() {
    val _text: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    fun  get(name:String,psw:String,context: Context): LiveData<String> {
        check(name, psw, context)
        return _text
    }
    fun check(name:String,psw:String,context: Context){
       var object1=JSONObject()
        object1.put("name",name);
        object1.put("psw",psw);
        val client= OkHttpClient();
        val JSON: MediaType = "application/json".toMediaType()
        var builder = Request.Builder()
        builder.url("http://49.233.81.150:9090/service/login")
        builder.addHeader("Content-Type","application/json")
            .post( RequestBody.create(JSON,object1.toString() ))
            .build()
        client.newCall(builder.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("webService", "————失败了$e")
            }
            override fun onResponse(call: Call, response: Response) {
                var stA = response.body!!.string()
                System.out.println("登录结果"+stA)
                if(stA.equals("0")){
                    _text.postValue("0")
                }else{
                    val headers = response.headers
                    val cookies = headers.values("Set-Cookie")
                    val session = cookies[0]
                    val s = session.substring(0, session.indexOf(";"))
                    val info=context.getSharedPreferences("info", Context.MODE_PRIVATE).edit()
                    info.putString("cookie",s).apply()
                    info.putBoolean("islogin",true).apply()
                    info.putString("user",name).apply()
                    Thread(Runnable {
                        var stA= Get(context,"http://49.233.81.150:9090/service/getprovice")
                        System.out.println(stA)
                            info.putString("provice",stA).apply()
                        }).start()
                    _text.postValue("1")
                    Log.d("webService", "onResponse: "+stA+" "+s)
                }
            }
        })
    }
}
