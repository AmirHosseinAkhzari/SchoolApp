package com.example.cote.ui

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cote.ui.Astin.AstinMainPageUi
import com.example.cote.ui.Astin.addAstin.WirteAstin
import com.example.cote.ui.Astin.addAstin.addAstinUi
import com.example.cote.ui.login.LoginPage
import com.example.cote.ui.main.MainPageUi
import com.example.cote.ui.Astin.readAstin.ReadAstin
import com.example.cote.ui.attendance.AttendanceMainPageUi
import com.example.cote.ui.attendance.Sms.BiometricChecker
import com.example.cote.ui.attendance.read.ReadAttendance
import com.example.cote.ui.pages.CheckMarkPage


@Composable
fun MainNavHost(modifier: Modifier = Modifier, starterRoute  :String){

    val navController = rememberNavController()

    NavHost(
        navController = navController ,
        startDestination = starterRoute ,
        enterTransition = {
            EnterTransition.None
        } ,
        exitTransition = {
            ExitTransition.None
        }
    ){

        composable("login"){
            LoginPage(modifier , navController)
        }

        composable("main"){
            MainPageUi(modifier , navController)
        }

        composable("mainAstin"){
            AstinMainPageUi(modifier , navController)
        }

        composable("mainAttendance"){
            AttendanceMainPageUi(modifier , navController)
        }

        composable("SendSms"){
            BiometricChecker(modifier , navController)
        }

        composable("ReadAttendance"){
            ReadAttendance(modifier , navController)
        }

        composable("addAstin"){
            addAstinUi(modifier , navController)
        }

        composable(route= "addAstinNFCTag/{id}/{step}/{uid}"
            , arguments = listOf(navArgument("id") {type = NavType.StringType},
                    navArgument("step") {type = NavType.StringType} ,
                navArgument("uid") {type = NavType.StringType})

        ){

            val id = it.arguments?.getString("id")
            val step = it.arguments?.getString("step")
            val uid = it.arguments?.getString("uid")

            Log.d("data" , id!!)
            Log.d("data" , step!!)
            Log.d("data" , uid!!)




            WirteAstin(modifier , navController , id, step , uid )
        }

        composable(route= "ReadAstin/{step}/{uid}"
            , arguments = listOf(
                navArgument("step") {type = NavType.StringType} ,
                navArgument("uid") {type = NavType.StringType})

        ){

            val step = it.arguments?.getString("step")
            val uid = it.arguments?.getString("uid")


            Log.d("testNav" , step!!)
            Log.d("testNav" , uid!!)



            ReadAstin(modifier , navController , step , uid )
        }


        composable(route= "checkMarkPage/{code}/{message}"
            , arguments = listOf(
                navArgument("code") {type = NavType.StringType} ,
                navArgument("message") {type = NavType.StringType})

        ){

            val code = it.arguments?.getString("code")
            val message = it.arguments?.getString("message")






            CheckMarkPage(modifier , navController , code!! , message!! )
        }




    }


}