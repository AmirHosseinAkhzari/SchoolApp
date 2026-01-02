package com.example.cote

import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.cote.ui.MainNavHost

@Composable
fun coteApp(modifier : Modifier = Modifier){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("MyPref", MODE_PRIVATE)
    val token  = sharedPref.getString("token" , null)

    Log.d("token" ,token.toString())
    if (token == null){
        MainNavHost(modifier , "login")

    }else{
        MainNavHost(modifier , "ReadAstin/2/hiu")
    }


}