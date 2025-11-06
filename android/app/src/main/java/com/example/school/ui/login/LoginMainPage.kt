package com.example.school.ui.login

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.school.R
import com.example.school.ui.theme.DarkGreen
import com.example.school.ui.theme.LightSurmeh
import com.example.school.ui.theme.Milky
import com.example.school.ui.theme.Surmeh
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


@Composable
fun isKeyboardOpen(): Boolean {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    return ime.getBottom(density) > 0
}



enum class LoginPages{
    LoginType ,LoginWithSleeve  ,LoginWithNumber , LoginOtpCode
}


@Composable
fun LoginPage(modifier: Modifier = Modifier , MainNavHost : NavController) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var Screen by remember { mutableStateOf(LoginPages.LoginType) }

            AnimatedVisibility(
                visible = !isKeyboardOpen()
            ){
                Column {
                    Spacer(Modifier.size(80.dp))
                    Text(
                        text = "آستین",
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



