package com.example.keravat

import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.keravat.ui.NavHost.MainNavHost

@Composable
fun KeravatApp(modifier : Modifier = Modifier){

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("MyPref", MODE_PRIVATE)
    val token  = sharedPref.getString("token" , null)

    Log.d("token" ,token.toString())
    if (token == null){
        MainNavHost(modifier , "login")

    }else{
        MainNavHost(modifier , "main")

    }


}