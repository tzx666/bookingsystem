package com.buct.bookticket.util

import java.io.Serializable

data class scheduleData (
    val pre_takeoff:String,
   val  tickctA_price:String,
    val tickctC_price:String,
    val ComID:String,
    val name:String,
    val Pid:String,
    val ID:String,
    val tickctB_price:String,
    val pre_landing:String,
    val name1:String,
    val FullName:String
):Serializable