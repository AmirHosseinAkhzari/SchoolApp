package com.example.cote.ui.readAstin

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cote.R
import com.example.cote.data.remote.StudentFullData
import com.example.cote.ui.addAstin.AddAstinViewModel
import com.example.cote.ui.addAstin.NFCHnadeler

import com.example.cote.ui.addAstin.isKeyboardOpen
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
        NFCHnadeler(NFCStatus , navController , "Read" )
    }

}

@Composable
fun ReadAstinSteps(step: String , navController : NavController , uid : String){

    Log.d("uid1" , step)

    if(step == "1" ){
        Step1( navController )
    }else if (step == "2"){
        Step2( navController , StudentFullData(
            firstname = "adf" ,
            lastname = "dasd" ,
            birthday = "dad" ,
            number = "0dasf" ,
            ParentNumber = "asdasd" ,
            LocalNumber = "dasd" ,
            nationalId = "wasd" ,
            Classname = "adad"
        ))
    }

}

@Composable
fun Step1( navController: NavController , ){


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
            Spacer(Modifier.size(60.dp))
            Text(
                text = "(0/2)",
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
fun Step2(navController: NavController, Student : StudentFullData){


    Text(Student.toString())
}