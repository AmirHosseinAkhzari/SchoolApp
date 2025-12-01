package com.example.school.ui.navHost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.school.ui.attendance.AttendancePage
import com.example.school.ui.login.LoginPage
import com.example.school.ui.main.mainPage


@Composable
fun MainNavHost(modifier: Modifier = Modifier, starterRoute  :String){

    val navController = rememberNavController()

    NavHost(
        navController = navController ,
        startDestination = starterRoute
    ){

        composable("login"){
            LoginPage(modifier , navController)
        }

        composable("main"){
            mainPage(modifier , navController)
        }

        composable("attendance"){
            AttendancePage(modifier)
        }
    }


}