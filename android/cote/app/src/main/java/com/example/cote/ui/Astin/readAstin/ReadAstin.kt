package com.example.cote.ui.Astin.readAstin

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cote.R
import com.example.cote.data.remote.StudentFullData
import com.example.cote.ui.Astin.addAstin.NFCHnadeler

import com.example.cote.ui.Astin.addAstin.isKeyboardOpen
import kotlinx.coroutines.delay


@Composable
fun ReadAstin(modifier: Modifier = Modifier , navController: NavController , step : String , uid : String){


    val viewModel = hiltViewModel<ReadAstinViewModel>()


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

        Spacer(Modifier.size(50.dp))
        if(step == "0"){
            NFCHnadeler(NFCStatus , navController , "Read" )
        }else if (step == "1") {
            ReadAstinSteps(step = "1" , navController , uid = "0")
        }else{
            ReadAstinSteps(step = "2" , navController , uid = uid)

        }
    }

}

@Composable
fun ReadAstinSteps(step: String , navController : NavController , uid : String){



    if(step == "1" ){
        Step1( navController )
    }else if (step == "2"){

        Step2( navController , uid)
    }

}

@Composable
fun Step1( navController: NavController , ){


    var dot by remember { mutableStateOf(".") }


    val viewModel = hiltViewModel<ReadAstinViewModel>()
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
            Log.d("rades" , res)
            navController.navigate("ReadAstin/2/${res}")
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
            Spacer(Modifier.size(100.dp))

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
fun Step2(navController: NavController, uid : String){

    var Student by remember { mutableStateOf<StudentFullData?>(null) }

    var isEmpity by remember { mutableStateOf(false) }
    val viewModel = hiltViewModel<ReadAstinViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)


    LaunchedEffect(Unit) {

        val res = viewModel.ReadAstin(token!! , uid)
        Log.d("gigi" , res.toString())

        if(res.isSuccess){

            if(res.getOrNull()!!.code == 200){
                Student = res.getOrNull()!!.stu
            }else if (res.getOrNull()!!.code == 500){
                isEmpity = true
            }
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
    ) {


        BackButton {
            navController.navigate("main")
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {


            Spacer(Modifier.size(50.dp))

            Text(
                text = "اطلاعات آستین",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 30.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.tanha))
            )
            Spacer(Modifier.size(50.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)

                    .clip(RoundedCornerShape(30.dp))

                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (Student != null && !isEmpity) {


                    Spacer(Modifier.size(20.dp))

                    Text(
                        text = "نام : ${Student!!.firstname + " " + Student!!.lastname}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.tanha))
                    )

                    Spacer(Modifier.size(20.dp))

                    Text(
                        text = "تاریخ تولد  : ${Student!!.birthday}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.tanha))
                    )

                    Spacer(Modifier.size(20.dp))

                    Text(
                        text = "شماره تلفن : ${Student!!.number}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.tanha))
                    )

                    Spacer(Modifier.size(20.dp))

                    Text(
                        text = "شماره تلفن ولی : ${Student!!.ParentNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.tanha))
                    )

                    Spacer(Modifier.size(20.dp))

                    Text(
                        text = "کد ملی : ${Student!!.nationalid}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.tanha))
                    )

                    Spacer(Modifier.size(20.dp))

                    Text(
                        text = " کلاس : ${Student!!.className}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.tanha))
                    )

                }else if (isEmpity){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFDA6C6C))
                    ){

                            Text(
                                text = "تگ خالی است",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 50.dp)
                            )

                            val composition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.fail)
                            )
                            LottieAnimation(
                                composition = composition ,
                                modifier = Modifier
                                    .padding(50.dp)
                            )

                    }
                }
            }
        }
    }

}

@Composable
private fun BoxScope.BackButton(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(60.dp)
            .padding(10.dp)
            .align(Alignment.TopStart)
            .clickable { onClick() },
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "back",
        tint = MaterialTheme.colorScheme.background,
    )
}

