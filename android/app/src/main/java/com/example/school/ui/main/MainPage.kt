package com.example.school.ui.main

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController


@Composable
fun mainPage(modifier: Modifier = Modifier , navController: NavController){
    val view = LocalView.current


    val topAppBarColor = MaterialTheme.colorScheme.onBackground.toArgb()
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor =topAppBarColor
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
    Box(
        modifier = modifier ,
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 40.dp , bottomStart = 40.dp))
                .background(MaterialTheme.colorScheme.onBackground)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.size(10.dp))
            Text(
                text = "آستین",
                style = MaterialTheme.typography.titleLarge ,
                color = MaterialTheme.colorScheme.background ,

            )
            Spacer(Modifier.size(10.dp))

        }
    }
}