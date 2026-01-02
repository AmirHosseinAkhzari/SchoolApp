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
import com.example.cote.ui.addAstin.WirteAstin
import com.example.cote.ui.addAstin.addAstinUi
import com.example.cote.ui.login.LoginPage
import com.example.cote.ui.main.MainPageUi
import com.example.cote.ui.readAstin.ReadAstin


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

//        composable(route= "ReadAstin/{step}/{uid}"
//            , arguments = listOf(
//                navArgument("step") {type = NavType.StringType} ,
//                navArgument("uid") {type = NavType.StringType})
//
//        ){
//
//            val step = it.arguments?.getString("step")
//            val uid = it.arguments?.getString("uid")
//
//
//
//            ReadAstin(modifier , navController , step!! , uid!! )
//        }




    }


}