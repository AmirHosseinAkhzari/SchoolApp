package com.example.school

import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.school.ui.login.LoginPage
import com.example.school.ui.login.LoginWithSleeve
import com.example.school.ui.navHost.MainNavHost

@Composable
fun SchoolApp(modifier : Modifier = Modifier){

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