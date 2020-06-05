package com.buct.bookticket.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat

 val sdf1 =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ")
 val sdf2 =
    SimpleDateFormat("HH:mm")
 val sdf3 =
    SimpleDateFormat("MM月dd日")
fun encode(text: String): String {
    try {
        //获取md5加密对象
        val instance: MessageDigest = MessageDigest.getInstance("MD5")
        //对字符串加密，返回字节数组
        val digest:ByteArray = instance.digest(text.toByteArray())
        var sb : StringBuffer = StringBuffer()
        for (b in digest) {
            //获取低八位有效值
            var i :Int = b.toInt() and 0xff
            //将整数转化为16进制
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                //如果是一位的话，补0
                hexString = "0" + hexString
            }
            sb.append(hexString)
        }
        return sb.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""
}
fun toProviceMap(stA:String):HashMap<String,areaData>{
    val gson= Gson()
    val data= object : TypeToken<ArrayList<areaData>>() {}.type
    lateinit var res: ArrayList<areaData>
    res=gson.fromJson(stA,data)
    Log.d("webService", "onResponse: "+res[0])
     var map=HashMap<String,areaData>()
    for(i in 0..res.size-1){
        map.put(res[i].sname,res[i])
    }
    return map
}
fun toTickInfo(stA:String):ArrayList<scheduleData>{
    val gson= Gson()
    val data= object : TypeToken<ArrayList<scheduleData>>() {}.type
    lateinit var res: ArrayList<scheduleData>
    res=gson.fromJson(stA,data)
    Log.d("webService", "onResponse: "+res[0])
    return res
}
fun todingdan(stA:String):ArrayList<dingdan>{
    val gson= Gson()
    val data= object : TypeToken<ArrayList<dingdan>>() {}.type
    lateinit var res: ArrayList<dingdan>
    res=gson.fromJson(stA,data)
    //Log.d("webService", "onResponse: "+res[0])
    return res
}
fun userinnfo(stA:String):userinfo{
    val gson= Gson()
    val data= object : TypeToken<userinfo>() {}.type
    lateinit var res: userinfo
    res=gson.fromJson(stA,data)
    Log.d("webService", "onResponse: "+res)
    return res
}
fun Get(context:Context,url:String): String? {
    val info = context.getSharedPreferences(
        "info", Context.MODE_PRIVATE)
    val cookie=info.getString("cookie","")
    val client= OkHttpClient();
    val JSON: MediaType = "application/json".toMediaType()
    var builder = Request.Builder()
    builder.url(url)
    if (cookie != null) {
        builder.addHeader("Content-Type","application/json")
            .addHeader("cookie",cookie)
            .build()
    }
    val response = client.newCall(builder.build()).execute()
    return response.body?.string()
}
fun Post(context: Context,url:String,body:String):String{
    val info = context.getSharedPreferences(
        "info", Context.MODE_PRIVATE)
    val cookie=info.getString("cookie","")
    val client= OkHttpClient();
    val JSON: MediaType = "application/json".toMediaType()
    var builder = Request.Builder()
    builder.url(url)
    if (cookie != null) {
        builder.addHeader("Content-Type","application/json")
            .addHeader("cookie",cookie)
            .post(RequestBody.create(JSON,body))
            .build()
    }
    val response = client.newCall(builder.build()).execute()
    return response.body!!.string()
}