package com.example.school

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.school.ui.login.LoginPage
import com.example.school.ui.login.LoginWithSleeve

@Composable
fun SchoolApp(modifier : Modifier = Modifier){

    LoginPage(modifier)


}