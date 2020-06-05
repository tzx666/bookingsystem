package com.buct.bookticket

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.buct.bookticket.util.Get
import com.buct.bookticket.util.Islogin
import com.buct.bookticket.util.message
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : AppCompatActivity() {
    private val tag="MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate: ")
        setContentView(R.layout.activity_main)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.register) {
                runOnUiThread { navView.setVisibility(View.GONE) }
            } else {
                runOnUiThread { navView.setVisibility(View.VISIBLE) }
            }
            println(destination.id == R.id.loginFragment)
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onRestart() {
        Log.d(tag, "onRestart: ")
        super.onRestart()
    }

    override fun onStart() {
        EventBus.getDefault().register(this);
        Log.d(tag, "onStart: ")
        super.onStart()
        val info = getSharedPreferences(
            "info", Context.MODE_PRIVATE)
        val cookie=info.getString("cookie","")
        if(cookie.equals("")){
            info.edit().putBoolean("islogin",false).apply()
            val msg=Islogin()
            msg.message="0"
            EventBus.getDefault().post(msg)
        }
        else{
            Thread(Runnable {
                var stA=Get(this,"http://49.233.81.150:9090/service/getprovice")
                System.out.println("获取"+stA)
                if(stA.equals("408")){
                    System.out.println("未登录")
                    info.edit().putBoolean("islogin",false).apply()
                    val msg=Islogin()
                    msg.message="0"
                    EventBus.getDefault().post(msg)
                }else{
                    val msg=Islogin()
                    msg.message="1"
                    info.edit().putString("provice",stA).apply()
                    info.edit().putBoolean("islogin",true).apply()
                    EventBus.getDefault().post(msg)
                }
            }).start()
            Log.d(tag, "onStart: "+cookie)
        }
    }

    override fun onStop() {
        Log.d(tag, "onStop: ")
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onPause() {
        Log.d(tag, "onPause: ")
        super.onPause()
    }

    override fun onResume() {
        Log.d(tag, "onResume: ")
        super.onResume()
    }

    override fun onDestroy() {
        Log.d(tag, "onDestroy: ")
        super.onDestroy()
    }
    @Subscribe
    fun getInfo(message: message){
        Log.d(ContentValues.TAG, "getInfo: "+message.message)
    }
}
