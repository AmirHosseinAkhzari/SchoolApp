package com.example.cote.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cote.R
import com.example.cote.ui.Astin.addAstin.isKeyboardOpen
import kotlinx.coroutines.delay

@Composable
fun CheckMarkPage( modifier: Modifier , navController: NavController , code : String , message: String){

    val time : Int
    if(code == "200") {
        time = 2500
    }else{
        time = 5000
    }
    LaunchedEffect(Unit) {
        delay(time.toLong())
        navController.navigate("main")
    }

    var color : Color
    var resId : Int

    if(code == "200") {
        color = Color(0xFF028864D)
        resId = R.raw.success
    }else{
        color =Color(0xFFE25D5D)
        resId = R.raw.fail
    }


    Column (
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = Modifier
            .fillMaxSize()
    ) {


        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 20.dp, start = 20.dp)
                .height(600.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(color)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(40.dp))
                        .size(300.dp)


                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                        )

                        val composition by rememberLottieComposition(
                            LottieCompositionSpec.RawRes(resId)
                        )
                        LottieAnimation(
                            composition = composition
                        )
                    }
                }
            }
        }
    }
}