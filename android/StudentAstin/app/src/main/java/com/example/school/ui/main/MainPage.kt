package com.example.school.ui.main

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


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
fun Heder(mode : String){

    val MaxScreenheight = LocalConfiguration.current.screenHeightDp.dp
    val lastHeight = remember { mutableStateOf(MaxScreenheight) }
    var animatedHeight = remember { Animatable(lastHeight.value.value) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {

        if(mode == "firstTime"){
            scope.launch {
                delay(500)
                animatedHeight.animateTo(
                    targetValue = 150f,
                    animationSpec = spring(
                        dampingRatio = 0.8f,
                        stiffness = 30f
                    )
                )
            }
        }else if (mode == "close") {

            animatedHeight.animateTo(
                targetValue = 150f,
                animationSpec = spring(
                    dampingRatio = 0.9f,
                    stiffness = 10f
                )
            )
        }
        else{
            animatedHeight.snapTo(150f)
        }


    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 40.dp , bottomStart = 40.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
            .height(animatedHeight.value.dp)


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




@Composable
fun mainPage(modifier: Modifier = Modifier , navController: NavController , mode : String){
    val view = LocalView.current
    val colors = appColors()
    val viewModel = hiltViewModel<MainPageViewModel>()
    val topAppBarColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val context = LocalContext.current
    val mainToken = viewModel.GetMainToken(context)

    val activity = context as? Activity

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }
    }


    var Dietail by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if(mode == "firstTime"){
            Dietail= true
        }else{
            delay(200)
            Dietail= true
        }

    }

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


        if(Dietail) {

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .size(400.dp, 450.dp)
                        .padding(30.dp)
                        .border(
                            2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            RoundedCornerShape(40.dp)
                        )
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp, top = 30.dp)
                    ) {
                        AppIcon(
                            color = colors["attendance"]!!,
                            name = "حضور غیاب",
                            painter = painterResource(R.drawable.attendance),
                            mode = true,
                            navigation = { navController.navigate("attendance") },
                            modifier = Modifier.weight(1f)

                        )

                        AppIcon(
                            colors["library"]!!,
                            "به زودی",
                            painterResource(R.drawable.library),
                            mode = false,
                            {},
                            Modifier.weight(1f))

                        AppIcon(
                            colors["cafeteria"]!!,
                            "به زودی",
                            painterResource(R.drawable.cafeteria),
                            mode = false,
                            {},
                            Modifier.weight(1f))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp, top = 20.dp)

                    ) {

                        AppIcon(
                            colors["poll"]!!,
                            "به زودی",
                            painterResource(R.drawable.poll),
                            mode = false,
                            {},
                            Modifier.weight(1f))
                        AppIcon(
                            colors["important"]!!,
                            "به زودی",
                            painterResource(R.drawable.important),
                            mode = false,
                            {},
                            Modifier.weight(1f))
                        AppIcon(
                            colors["score"]!!,
                            "به زودی",
                            painterResource(R.drawable.score),
                            mode = false,
                            {},
                            Modifier.weight(1f))
                    }
                }

                Text(
                    text = "گزینه ها",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .align(Alignment.TopCenter)
                )
            }
        }


        Heder(mode)


    }
}



@Composable
fun AppIcon(color  :Color , name : String  , painter: Painter , mode : Boolean , navigation : () -> Unit  , modifier: Modifier ){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = modifier
            .padding(5.dp)
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
        AutoSizeText(
            name
        )
    }
}


@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    maxFontSize: TextUnit = 16.sp,
    minFontSize: TextUnit = 8.sp,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    var fontSize by remember { mutableStateOf(maxFontSize) }
    Text(
        text = text,
        maxLines = 1,
        softWrap = false,
        style = style.copy(fontSize = fontSize),
        color = color,
        modifier = modifier,
        onTextLayout = { result ->
            if (result.didOverflowWidth && fontSize > minFontSize) {
                fontSize = (fontSize.value - 1f).sp
            }
        }
    )
}
