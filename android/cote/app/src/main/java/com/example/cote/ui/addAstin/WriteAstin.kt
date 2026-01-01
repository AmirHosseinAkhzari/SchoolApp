package com.example.cote.ui.addAstin

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import com.example.cote.R


@Composable
fun WirteAstin(modifier: Modifier , navController : NavController , id : String , step : String , uid : String? = null){

    val viewModel = hiltViewModel<AddAstinViewModel>()

    val context = LocalContext.current

    var NFCStatus by remember {  mutableStateOf(viewModel.GetNFCStatus(context))}

    LaunchedEffect(Unit) {
        while (true) {
            NFCStatus = viewModel.GetNFCStatus(context)
            delay(1000)
        }
    }
    Log.d("NFC" ,NFCStatus)
    Column (
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = Modifier
            .fillMaxSize()
    ) {

        AnimatedVisibility(
            visible = !isKeyboardOpen()
        ) {
            Column {
                Spacer(Modifier.size(60.dp))
                Text(
                    text = "کُت",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }

        Spacer(Modifier.size(20.dp))
        Steps(id , step , navController , uid!!)

    }
}

@Composable
fun Steps(id : String , step: String , navController : NavController , uid : String){

    Log.d("uid1" , step)


    if(step == "1" ){
        Step1(id , navController )
    }else if (step == "2"){
        Step2(id , uid)

    }

}




@Composable
fun Step1(id : String , navController: NavController , ){


    var dot by remember { mutableStateOf(".") }


    val viewModel = hiltViewModel<AddAstinViewModel>()
    LaunchedEffect(Unit) {
        while (true){
            if(dot.length == 3){
                dot = "."
            }else{
                dot = dot + "."
            }
            delay(1000)
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {

        val res = viewModel.ReadNFCUid(context)

        if (res != null){
            navController.navigate("addAstinNFCTag/${id}/2/${res}")
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp , start = 20.dp )
            .height(600.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ){
            Spacer(Modifier.size(60.dp))
            Text(
                text = "(0/3)",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black ,
                fontFamily = FontFamily(Font(R.font.sorena))
            )
            Spacer(Modifier.size(20.dp))

            Image(
                painter = painterResource(R.drawable.astinguidimage) ,
                contentDescription = "AstinGuideImage" ,
                modifier = Modifier
                    .size(300.dp)
                    .padding(end = 14.dp)
            )
            Text(
                text = "گوشیت رو نزدیک آستین کن ",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black ,

                fontFamily = FontFamily(Font(R.font.portadaregular))
            )

            Text(
                text = dot,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black ,

                fontFamily = FontFamily(Font(R.font.portadaregular))
            )

        }
    }
}


@Composable
fun Step2(id : String , uid: String) {

    var dot by remember { mutableStateOf(".") }

    var SendIt by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            if (dot.length == 3) {
                dot = "."
            } else {
                dot = dot + "."
            }
            delay(1000)
        }
    }

    LaunchedEffect(SendIt) {
        if(SendIt){
            
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 20.dp)
            .height(600.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(Modifier.size(60.dp))
            Text(
                text = "(1/3)",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.sorena))
            )
            Spacer(Modifier.size(20.dp))


            Text(
                text = "ثبت در سایت",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,

                fontFamily = FontFamily(Font(R.font.portadaregular))
            )




            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .size(300.dp)
                    .background(Color(0xFF028864D))
                    .clickable {
                          SendIt = true
                    }

            ) {

                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "check",
                    modifier = Modifier
                        .size(170.dp),
                    tint = Color.White
                )

            }
            Text(
                text = dot,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,

                fontFamily = FontFamily(Font(R.font.portadaregular))
            )

        }
    }

}



@Composable
fun step4(isPlaying : () -> Unit){

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp, start = 20.dp)
            .height(600.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xFF028864D))
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
                    .clickable {
                        isPlaying()
                    }

            ) {


                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.success)
                )
                LottieAnimation(
                    composition = composition
                )
            }


        }
    }
}