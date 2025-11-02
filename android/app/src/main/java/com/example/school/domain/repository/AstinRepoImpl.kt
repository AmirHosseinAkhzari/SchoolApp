package com.example.school.domain.repository

import android.app.Activity
import android.app.Application
import android.util.Log
import com.example.school.R
import com.example.school.data.model.NfcManager
import com.example.school.data.remote.AstinApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AstinRepoImpl(
    private val api : AstinApi ,
    private val nfcManager: NfcManager ,
    private val appContext : Application
) : AstinRepo {



    init {
        val appName = appContext.getText(R.string.app_name)
        println("hello from repo $appName")
        Log.d("test" ,"hello from repo $appName" )
    }

    private val _nfcData = MutableStateFlow<String?>(null)
    override val nfcData = _nfcData.asStateFlow()
    override  fun readNfc(activity: Activity) {
        nfcManager.onTagRead = {uid ->
            _nfcData.value = uid
            Log.d("test" ,uid )
        }
        nfcManager.enableReader(activity)
    }

    override  fun stopReading(activity: Activity) {
        nfcManager.disableReader(activity)
    }

}