package com.example.school.domain.repository

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow


interface  AstinRepo  {


    val nfcData: StateFlow<String?>
    fun readNfc(activity: Activity)
    fun stopReading(activity: Activity)

}