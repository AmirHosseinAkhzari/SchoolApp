package com.example.cote.ui.addAstin

import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cote.R
import kotlinx.coroutines.delay

@Composable
fun addAstinUi(modifier: Modifier , navController : NavController){

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

        Spacer(Modifier.size(60.dp))
        Text(
            text = "کُت",
            style = MaterialTheme.typography.titleLarge ,
        )

        Spacer(Modifier.size(100.dp))
        NFCHnadeler(NFCStatus)
    }
}



@Composable
fun NFCHnadeler(status : String){

    if(status == "NFCisUnAvilabel"){
        NFCisUnAvilabel(Modifier)
    }else if (status == "NFCisOff"){
        NFCisOff()
    }else{
        NFCIsOn()
    }
}



@Composable
fun NFCisUnAvilabel(modifier: Modifier = Modifier){

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(360.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier.fillMaxSize()
        ){


            Icon(
                painter = painterResource(R.drawable.block) ,
                tint = Color.Black ,
                contentDescription = "block" ,
                modifier = Modifier
                    .size(220.dp)
            )

            Text(
                text = "این گوشی از این سرویس",
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontSize = 35.sp
            )
            Text(
                text = "پشتیبانی نمی کند ",
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontSize = 35.sp
            )


        }

    }
}

@Composable
fun NFCisOff(modifier: Modifier = Modifier){

    val intent = Intent(Settings.ACTION_NFC_SETTINGS)

    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(380.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
    ){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally ,
            modifier = Modifier.fillMaxSize()
        ){

            Spacer(Modifier.size(30.dp))

            Icon(
                painter = painterResource(R.drawable.setting) ,
                tint = Color.White ,
                contentDescription = "setting" ,
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.Black)
                    .clickable{
                        context.startActivity(intent)
                    }
            )

            Spacer(Modifier.size(30.dp))

            Text(
                text = "را فعال کنید NFC سرویس",
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontFamily = FontFamily(Font(R.font.tanha)),
                fontSize = 30.sp ,

            )


        }

    }
}


@Composable
fun NFCIsOn(modifier: Modifier = Modifier){

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ){

    }
}





