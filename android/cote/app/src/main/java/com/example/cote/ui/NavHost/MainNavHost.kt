package com.example.cote.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cote.ui.login.LoginPage

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

//        composable(route= "main/{mode}"
//            , arguments = listOf(navArgument("mode") {type = NavType.StringType})
//        ){
//
//            val mode = it.arguments?.getString("mode")
//            mainPage(modifier , navController , mode!! )
//        }


    }


}