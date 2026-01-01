package com.example.cote.ui.addAstin

import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.example.cote.data.remote.Student
import kotlinx.coroutines.delay


@Composable
fun isKeyboardOpen(): Boolean {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    return ime.getBottom(density) > 0
}


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
        NFCHnadeler(NFCStatus , navController)
    }
}



@Composable
fun NFCHnadeler(status : String , navController: NavController){

    if(status == "NFCisUnAvilabel"){
        NFCisUnAvilabel(Modifier)
    }else if (status == "NFCisOff"){
        NFCisOff()
    }else{
        NFCIsOn(navController = navController)
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
fun NFCIsOn(modifier: Modifier = Modifier , navController: NavController){


    val viewModel = hiltViewModel<AddAstinViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)!!

    var students by remember { mutableStateOf<List<Student>?>(null) }

    var query by remember { mutableStateOf("") }




    LaunchedEffect(Unit) {


        val res = viewModel.ReadStudent(token)

        if (res.isSuccess){
            students = res.getOrNull()!!.students
        }

        Log.d("Stu" , students.toString())
    }


    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { Text(
            text = "... جسستوجو کنید" ,
            style = MaterialTheme.typography.bodySmall ,
            textAlign = TextAlign.Right ,
            modifier = Modifier.fillMaxWidth()
        ) },
        modifier = Modifier
            .padding(end = 10.dp , start = 10.dp , bottom = 30.dp)
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(12.dp)) ,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black ,
        ),
        shape = RoundedCornerShape(10.dp)  ,
        singleLine = true ,
        textStyle = MaterialTheme.typography.bodySmall ,



    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ){

        LazyColumn(
            modifier = Modifier.fillMaxSize() ,
            verticalArrangement = Arrangement.Top
        ){
            if(students != null ){
                items(searchEngin(query , students!!)){
                    StudentItem(it , navController )
                }
            }

        }
    }
}

@Composable
fun StudentItem(student : Student , navController : NavController){

    val FullName = student.firstname + " " + student.lastname
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White )
            .clickable{
                navController.navigate("addAstinNFCTag/${student._id}")
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()

        ){

            Spacer(Modifier.size(20.dp))


            Spacer(Modifier.size(10.dp))

            Text(
                text = FullName,
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontSize = 30.sp ,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .fillMaxWidth()
            )

        }
    }

    Spacer(Modifier.size(20.dp))
}


fun searchEngin(q : String ,  allStu : List<Student>): List<Student> {

    val query = q.trim()


    return if (q.isEmpty()){
        allStu
    }else{
         allStu.filter { student ->
            student.firstname.trim().startsWith(query) ||
                    student.lastname.trim().startsWith(query)
        }
    }


}




