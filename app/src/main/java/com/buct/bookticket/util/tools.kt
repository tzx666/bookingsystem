package com.buct.bookticket.util

import java.text.SimpleDateFormat
import java.util.*

class tools {
    companion object {
        fun getNow(): String {
            if (android.os.Build.VERSION.SDK_INT >= 24){
                return SimpleDateFormat("yyyy-MM-dd").format(Date())
            }else{
                var tms = Calendar.getInstance()
                return tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH).toString() + "-" + tms.get(Calendar.DAY_OF_MONTH).toString()
            }
        }
    }
}