package com.example.school.ui.main

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.school.R
import com.example.school.data.remote.ReqAddNotificationToken
import com.example.school.ui.theme.attendanceColor
import com.example.school.ui.theme.cafeteriaColor
import com.example.school.ui.theme.importantColor
import com.example.school.ui.theme.libraryColor
import com.example.school.ui.theme.pollColor
import com.example.school.ui.theme.scoreColor
import com.google.firebase.Firebase
import com.google.firebase.FirebaseCommonKtxRegistrar
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.messaging


@Composable
fun appColors(): Map<String, Color> {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        mapOf(
            "attendance" to Color(0xFF3FAF46),
            "library" to Color(0xFF8D7B75),
            "cafeteria" to Color(0xFFB08968),
            "poll" to Color(0xFF2196F3),
            "important" to Color(0xFFCF2C3E),
            "score" to Color(0xFFE1B12C),
        )
    } else {
        mapOf(
            "attendance" to Color(0xFF89EC8D),
            "library" to Color(0xFFD7CCC8),
            "cafeteria" to Color(0xFFF5E6CC),
            "poll" to Color(0xFF74C7FF),
            "important" to Color(0xFFF53046),
            "score" to Color(0xFFF8C01F),
        )
    }
}



@Composable
fun mainPage(modifier: Modifier = Modifier , navController: NavController){
    val view = LocalView.current
    val colors = appColors()


    val viewModel = hiltViewModel<MainPageViewModel>()
    val topAppBarColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val context = LocalContext.current
    val mainToken = viewModel.GetMainToken(context)

    Firebase.messaging.token.addOnCompleteListener { task ->


        if (!task.isSuccessful ){
            Log.w("FCM", "Token failed", task.exception)
            return@addOnCompleteListener
        }

        val token = task.result

        if (mainToken != null){
            Log.d("FCM" , mainToken)

            viewModel.SendNotificationToken(token  , mainToken)
        }

    }


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
        

        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {



            Column(
                modifier = Modifier

                    .size(400.dp, 400.dp)
                    .padding(30.dp)
                    .border(2.dp, color = MaterialTheme.colorScheme.onBackground, RoundedCornerShape(40.dp))
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp , end = 30.dp , top = 30.dp)
                ){
                    AppIcon(
                        color = colors["attendance"]!!,
                        name = "حضور و غیاب",
                        painter = painterResource(R.drawable.attendance),
                        mode = true,
                        navigation = { navController.navigate("attendance") }
                    )

                    AppIcon(colors["library"]!! , "به زودی" , painterResource(R.drawable.library) , mode = false , {})

                    AppIcon(colors["cafeteria"]!! , "به زودی" , painterResource(R.drawable.cafeteria) , mode = false , {})
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp , end = 30.dp , top = 20.dp)

                ){

                    AppIcon(colors["poll"]!! , "به زودی" , painterResource(R.drawable.poll) , mode = false , {})
                    AppIcon(colors["important"]!!  , "به زودی" , painterResource(R.drawable.important) , mode = false , {})
                    AppIcon(colors["score"]!!  , "به زودی", painterResource(R.drawable.score) , mode = false , {})



                }
            }

            Text(
                text = "گزینه ها",
                color = MaterialTheme.colorScheme.onBackground ,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .align(Alignment.TopCenter)
            )
        }
    }
}


@Composable
fun AppIcon(color  :Color , name : String  , painter: Painter , mode : Boolean , navigation : () -> Unit  ){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = Modifier.padding(5.dp)
    ) {
        var blurSize = 0
        if(!mode){
            blurSize = 10
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color)
                .clickable{
                    navigation()
                }


        ) {
            if (!mode){
                Icon(
                    imageVector = Icons.Rounded.Lock ,
                    contentDescription = "lock" ,
                    modifier = Modifier
                        .size(40.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurSize.dp)

            ) {
                Column {
                    Icon(
                        painter = painter,
                        contentDescription = name,
                        tint = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .size(75.dp)
                    )
                }
            }
        }
        Text(
            text = name ,
            style = MaterialTheme.typography.bodyMedium ,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}