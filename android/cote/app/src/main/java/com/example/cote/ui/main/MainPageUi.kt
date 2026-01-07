package com.example.cote.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cote.R
import okhttp3.Route
import java.nio.file.WatchEvent

data class option (
    val color: Color ,
    val text: String ,
    val size : Int ,
    val image: Painter ,
    val route : String
)

@SuppressLint("SuspiciousIndentation")
@Composable
fun MainPageUi(modifier: Modifier , navController : NavController){

    val options : List<option> = listOf(
        option(
            Color.White ,
            "آستین" ,
            90 ,
            painterResource(R.drawable.astin) ,
            "mainAstin"
        ) ,
        option(
            Color.White ,
            "حضور و غیاب" ,
            90 ,
            painterResource(R.drawable.attendance) ,
            "mainAttendance"
        )
    )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            items(options) {
                item(it ,  navController )
            }
        }
    }
    




@Composable
fun item(option: option , navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(option.color )
            .clickable{
                navController.navigate(route = option.route)
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()

        ){

            Spacer(Modifier.size(20.dp))

            Image(
                painter = option.image ,
                contentDescription = "Icon" ,
                modifier = Modifier
                    .size(size = option.size.dp)
            )

            Spacer(Modifier.size(10.dp))

            Text(
                text = option.text,
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontSize = 30.sp
            )

        }
    }

    Spacer(Modifier.size(20.dp))

}