package com.example.school.ui.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun navHost(MainNavController : NavController) {

    val navController = rememberNavController()

    val StarterRoute = "LoginType"

    NavHost(
        navController = navController ,
        startDestination = StarterRoute
    ){
        composable(route = "LoginType"){
            LoginType(navController)
        }

        composable(route = "LoginWithSleeve"){
            LoginWithSleeve(navController)
        }

        composable(route = "LoginWithNumber"){
            LoginWithNumber(navController)
        }

        composable(route = "LoginOtpCode"){
            LoginOtpCode(navController , MainNavController)
        }

    }
}