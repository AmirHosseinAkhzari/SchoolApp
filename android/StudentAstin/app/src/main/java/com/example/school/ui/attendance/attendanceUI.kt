package com.example.school.ui.attendance

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.school.R
import com.example.school.data.remote.Info
import com.example.school.data.remote.ResReadAttendance
import com.example.school.data.remote.total
import kotlinx.coroutines.launch
import kotlin.toString



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AttendancePage(modifier: Modifier = Modifier , navController : NavController) {

    val viewModel = hiltViewModel<AttendanceViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)


    Log.d("res" , token.toString())

    var data by remember { mutableStateOf<ResReadAttendance?>(null)}
    var blur by remember { mutableStateOf(10) }

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    fun fetchData(isPullRefresh: Boolean = false) {
        scope.launch {
            if (isPullRefresh) isRefreshing = true

            // Fetch data
            val result = viewModel.readAttendance(token!!).getOrNull()

            data = result

            // Update UI states
            if (data == null) {
                blur = 100
            } else {
                Log.d("res", data.toString())
                blur = 0
            }

            if (isPullRefresh) isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        fetchData(isPullRefresh = false)
    }


    Box(
        modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {

        if(blur == 10){
            CircularProgressIndicator()
        }

        Box(
            Modifier
                .blur(blur.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                state = pullRefreshState,
                onRefresh = { fetchData(isPullRefresh = true) },
                modifier = Modifier.fillMaxSize()
            ) {

                MainAttendanceUi(data , navController )
            }
        }
    }

}

@Composable
fun appColors(): Map<String, Color> {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        mapOf(
            "present" to Color(0xFF3FAF46),
            "absent" to Color(0xFF8F202D),
            "lateness" to Color(0xFFE1B12C),
        )
    } else {
        mapOf(
            "present" to Color(0xFF89EC8D),
            "absent" to Color(0xFFF53046),
            "lateness" to Color(0xFFF8C01F),
        )
    }
}
@Composable
fun number(number : String , name : String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center ,
    ) {
        Text(
            text = number
        )
        Text(
            text = name ,
            style = MaterialTheme.typography.bodySmall
        )

    }
}


@Composable
fun StatusItem(time : String , dete : String , status : String){

    val colors = appColors()

    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors[status]!!)
            .padding( 5.dp)
            .border(
                5.dp ,
                MaterialTheme.colorScheme.background ,
                RoundedCornerShape(20.dp)
            )
            .height(120.dp)
    ){

        Text(
            text = time ,
            style = MaterialTheme.typography.bodyMedium ,
            color = MaterialTheme.colorScheme.background,
            fontFamily = FontFamily(Font(R.font.tanha)) ,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.TopEnd)
        )

        Text(
            text = " $dete : ورود " ,
            style = MaterialTheme.typography.bodyMedium ,
            color = MaterialTheme.colorScheme.background,
            fontFamily = FontFamily(Font(R.font.tanha)) ,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomStart)
        )


    }
    Spacer(Modifier.size(8.dp))

}



@Composable
fun MainAttendanceUi(res: ResReadAttendance? , navController : NavController) {

    Log.d("res" ,res.toString() )
    val data = res ?: ResReadAttendance(
        total = total(
            present = 0,
            lateness = 0,
            absent = 0
        ),
        Info = listOf()
    )

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp))
                    .background(MaterialTheme.colorScheme.onBackground)
                    .fillMaxWidth()
            ) {
                Box(
                    Modifier.fillMaxWidth()
                ){
                    BackButton {
                        navController.navigate("main")
                    }
                    Column(
                        Modifier.fillMaxWidth() ,
                        verticalArrangement = Arrangement.Center ,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.size(10.dp))
                        Text(
                            text = "آستین",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.background,
                        )
                        Spacer(Modifier.size(10.dp))
                    }
                }

            }

            Spacer(Modifier.size(30.dp))

            Box(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {

                Row {
                    Spacer(Modifier.size(40.dp))

                    number(data.total.absent.toString(), "غیبت ها ")

                    Spacer(Modifier.size(40.dp))

                    number(data.total.present.toString(), "حضور ها")

                    Spacer(Modifier.size(40.dp))

                    number(data.total.lateness.toString(), "دیرکرد ها")
                }

            }

            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(20.dp)
                    )
                    .fillMaxSize()
            ) {

                LazyColumn(
                    Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxSize()
                ) {

                    items(data.Info) { item ->
                        StatusItem(
                            time = item.checkIn,
                            dete = item.date,
                            status =translateStatus(item.status)
                        )
                    }
                }
            }
        }
    }
}


fun translateStatus(name : String) : String{
    return when (name){
        "حاضر" -> "present"
        "غایب" -> "absent"
        "دیر اومده" -> "lateness"
        else -> ""
    }
}

@Composable
private fun BoxScope.BackButton(onClick: () -> Unit) {
    Log.d("hi" , "hi I'm a Btn ")
    Icon(
        modifier = Modifier
            .size(60.dp)
            .padding(10.dp)
            .align(Alignment.BottomStart)
            .clickable { onClick() },
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "back",
        tint = MaterialTheme.colorScheme.background,
    )
}
