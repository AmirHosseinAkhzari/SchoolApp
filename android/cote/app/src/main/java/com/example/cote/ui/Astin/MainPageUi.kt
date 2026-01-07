package com.example.cote.ui.Astin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cote.R
import com.example.cote.ui.main.item
import com.example.cote.ui.main.option


@Composable
fun AstinMainPageUi(modifier: Modifier , navController : NavController){

    val options : List<option> = listOf(
        option(
            Color.White,
            "اضافه کردن آستین",
            painterResource(R.drawable.addastin),
            "addAstin"
        ),

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

