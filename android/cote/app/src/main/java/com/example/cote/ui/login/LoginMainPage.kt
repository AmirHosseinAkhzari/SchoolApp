package com.example.cote.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun isKeyboardOpen(): Boolean {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    return ime.getBottom(density) > 0
}

enum class LoginPages{
    LoginWithNumber , LoginOtpCode
}

@Composable
fun LoginPage(modifier: Modifier = Modifier , MainNavHost : NavController) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var Screen by remember { mutableStateOf(LoginPages.LoginWithNumber) }

        AnimatedVisibility(
            visible = !isKeyboardOpen()
        ){
            Column {
                Spacer(Modifier.size(80.dp))
                Text(
                    text = "کُت",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(Modifier.size(40.dp))

        Card(
            modifier = Modifier
                .size(370.dp, 450.dp)
                .clip(RoundedCornerShape(20.dp))
                .shadow(elevation = 300.dp)

        ) {

            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground)

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AnimatedVisibility(true) {
                        navHost(MainNavHost)
                    }

                }
            }

        }
    }
}