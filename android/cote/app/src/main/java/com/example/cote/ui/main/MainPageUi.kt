package com.example.cote.ui.main

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
    val image: Painter ,
    val route : String
)

@Composable
fun MainPageUi(modifier: Modifier , navController : NavController){

    val options : List<option> = listOf(
        option(
            Color.White ,
            "اضافه کردن آستین" ,
            painterResource(R.drawable.addastin) ,
            "addAstin"
        ) ,

        option(
            Color.White ,
            "خوندن آستین" ,
                painterResource(R.drawable.readastin) ,
            "readAstin/0/0"
        )
    )
    Column (
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = modifier
    ){

        Spacer(Modifier.size(60.dp))
        Text(
            text = "کُت",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            items(options) {
                item(color = it.color, text = it.text, image = it.image , route = it.route , navController = navController )
            }
        }
    }
    

}


@Composable
fun item(color : Color , text : String , image : Painter , route : String,navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(color )
            .clickable{
                navController.navigate(route = route)
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()

        ){

            Spacer(Modifier.size(20.dp))

            Image(
                painter = image ,
                contentDescription = "Icon" ,
                modifier = Modifier
                    .size(90.dp)
            )

            Spacer(Modifier.size(10.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontSize = 30.sp
            )

        }
    }

    Spacer(Modifier.size(20.dp))

}