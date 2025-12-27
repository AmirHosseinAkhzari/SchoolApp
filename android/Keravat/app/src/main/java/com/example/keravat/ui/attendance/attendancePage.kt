package com.example.keravat.ui.attendance

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

import com.example.keravat.data.remote.ResReadAttendance
import kotlinx.coroutines.launch
import com.example.keravat.R
import com.example.keravat.data.remote.total
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AttendancePage(modifier: Modifier = Modifier , navController : NavController) {

    val viewModel = hiltViewModel<AttendanceViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)


    Log.d("res" , token.toString())

    var data by remember { mutableStateOf<ResReadAttendance?>(null)}

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    fun fetchData(isPullRefresh: Boolean = false) {
        scope.launch {
            if (isPullRefresh) isRefreshing = true

            // Fetch data
            val result = viewModel.readAttendance(token!!).getOrNull()

            data = result

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



        Box(
            Modifier
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
            "done" to Color(0xFF1C5B20),
        )
    } else {
        mapOf(
            "present" to Color(0xFF89EC8D),
            "absent" to Color(0xFFF53046),
            "lateness" to Color(0xFFF8C01F),
            "done" to Color(0xFF2A862F),

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
fun DiscriptionAdded(onDismissRequest :() -> Unit) {

    val colors = appColors()


    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .size(300.dp, 380.dp)
                .clip(RoundedCornerShape(20.dp))

                .background(colors["done"]!!)
                .padding(10.dp)
                .border(
                    8.dp,
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(20.dp)
                )
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
                        .height(150.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(MaterialTheme.colorScheme.background)
                ){

                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.success)
                    )

                    LottieAnimation(
                        composition = composition
                    )
                }

                Spacer(Modifier.size(20.dp))

                Text(
                    text = "توضیحات با موفقیت ثبت شده" ,
                    color = MaterialTheme.colorScheme.background ,
                    modifier = Modifier.fillMaxWidth() ,
                    style = MaterialTheme.typography.bodyMedium ,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}

@Composable
fun AddDiscriptionDialog(onDismissRequest :() -> Unit , time : String , date : String , status : String ){


    val colors = appColors()

     var value by remember { mutableStateOf("") }

    var submit by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }

    var Error by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val viewModel = hiltViewModel<AttendanceViewModel>()

    val token = viewModel.GetMainToken(LocalContext.current)


    val backgroundColor by animateColorAsState(
        targetValue = if (submit)  colors["done"]!! else colors[status]!!,
        animationSpec = tween(durationMillis = 1000)
    )
    Dialog(
        onDismissRequest = onDismissRequest
    ) {

        Box(
            Modifier
                .size(300.dp , 380.dp)
                .clip(RoundedCornerShape(20.dp))

                .background(backgroundColor)
                .padding( 10.dp)
                .border(
                    8.dp ,
                    MaterialTheme.colorScheme.background ,
                    RoundedCornerShape(20.dp)
                )
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally ,

                modifier = Modifier
                    .fillMaxSize()
            ){


                Spacer(Modifier.size(50.dp))


                val title =  translateStatusToPersian(status)
                Log.d("color" , title)
                Log.d("color" , status)

                Text(
                    text = title ,
                    color = MaterialTheme.colorScheme.background ,
                    modifier = Modifier.fillMaxWidth() ,
                    style = MaterialTheme.typography.titleMedium ,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.size(10.dp))

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    label = {
                        Text(
                            text = "دلیل $title",
                            color = MaterialTheme.colorScheme.secondary ,
                            style = MaterialTheme.typography.bodySmall ,
                            modifier = Modifier.background(Color.Transparent)
                        )
                    },
                    placeholder = {
                        Text(
                            text = "دلیل $title را وارد کنید ",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f) ,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = false,
                    maxLines = 3,
                    textStyle = MaterialTheme.typography.bodySmall ,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground ,


                    )
                )

                Spacer(Modifier.size(10.dp))
                if(isError){
                    Text(
                        text = Error ,
                        color = MaterialTheme.colorScheme.background ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                        ,
                        style = MaterialTheme.typography.bodySmall ,
                        textAlign = TextAlign.Start
                    )
                }
                Spacer(Modifier.size(10.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(70.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .clickable{
                            scope.launch {

                                if(value == ""){
                                    isError = true
                                }else{
                                    val res = viewModel.addAttendanceDescription(token!! , value , date)

                                    if(res.getOrNull()!!.code == 200 ){
                                        submit = true
                                        delay(1000)
                                        onDismissRequest()

                                    }
                                }

                            }

                        }
                ){

                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = "Confirm",
                        modifier = Modifier.size(180.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }


    }

}

@Composable
fun StatusItem(time : String , date : String , status : String , descriptionMode : Boolean){

    val colors = appColors()

    var DialogMode by remember { mutableStateOf(false) }

    Box(
        Modifier
            .clickable{
                DialogMode = true
            }
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
            text = " $date : ورود " ,
            style = MaterialTheme.typography.bodyMedium ,
            color = MaterialTheme.colorScheme.background,
            fontFamily = FontFamily(Font(R.font.tanha)) ,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomStart)
        )
        Spacer(Modifier.size(8.dp))

    }

    if(DialogMode == true){
        if(descriptionMode){
            AddDiscriptionDialog({DialogMode = false} , time , date , status)
        }else{
            DiscriptionAdded({DialogMode = false})
        }

    }

}



@Composable
fun MainAttendanceUi(res: ResReadAttendance?, navController: NavController) {

    var Dietail by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(700)
        Dietail= true
    }
    val data = res ?: ResReadAttendance(
        total = total(
            present = 0,
            lateness = 0,
            absent = 0
        ),
        Info = listOf()
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header
            AttendanceHeader(navController)

            Spacer(Modifier.size(30.dp))

            if(Dietail){
                // Summary Box (Absent / Present / Lateness)
                AttendanceSummary(data)

                // Main List
                AttendanceList(data)
            }

        }
    }
}

@Composable
fun AttendanceHeader(navController: NavController) {

    val maxHight = LocalConfiguration.current.screenHeightDp.dp
    val headerController = rememberHeaderController(initialHeight = 150.dp)
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(expanded) {
        if (expanded) {
            headerController.expand(maxHight)
        } else {
            headerController.collapse(150.dp)
        }
    }


    LaunchedEffect(Unit) {
        expanded = true
        delay(1000)
        expanded = false
    }


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
    ) {
        Box(Modifier.fillMaxWidth()) {
            BackButton {
                scope.launch {
                    expanded = true
                    delay(1000)
                    navController.navigate("main/close")
                }
            }

            BackHandler {
                scope.launch {
                    expanded = true
                    delay(1000)
                    navController.navigate("main/close")
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .height(headerController.height.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.size(10.dp))
                Text(
                    text = "کراوات",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )
                Spacer(Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun AttendanceSummary(data: ResReadAttendance) {
    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            number(data.total.absent.toString(), "غیبت ها ")
            Spacer(Modifier.size(40.dp))
            number(data.total.present.toString(), "حضور ها")
            Spacer(Modifier.size(40.dp))
            number(data.total.lateness.toString(), "دیرکرد ها")
        }
    }
}

@Composable
fun AttendanceList(data: ResReadAttendance) {
    Column(
        modifier = Modifier
            .padding(15.dp)
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20.dp))
            .fillMaxSize()
    ) {
        Spacer(Modifier.size(8.dp))
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {
            items(data.Info) { item ->
                StatusItem(
                    time = item.checkIn,
                    date = item.date,
                    status = translateStatus(item.status)
                )
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

fun translateStatusToPersian(name : String) : String{
    return when (name){
        "present" -> "حاضر"
        "absent" -> "غایب"
        "lateness" -> "دیر اومده"
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

class HeaderController(
    initialHeight: Dp = 150.dp
) {
    private val _height = Animatable(initialHeight.value)
    val height: Float get() = _height.value

    private var scope: CoroutineScope? = null

    fun attachScope(scope: CoroutineScope) {
        this.scope = scope
    }

    fun expand(target: Dp, damping: Float = 0.8f, stiffness: Float = 30f) {
        scope?.launch {
            _height.animateTo(
                targetValue = target.value,
                animationSpec = spring(
                    dampingRatio = damping,
                    stiffness = stiffness
                )
            )
        }
    }

    fun collapse(target: Dp, damping: Float = 0.9f, stiffness: Float = 10f) {
        scope?.launch {
            _height.animateTo(
                targetValue = target.value,
                animationSpec = spring(
                    dampingRatio = damping,
                    stiffness = stiffness
                )
            )
        }
    }
}

@Composable
fun rememberHeaderController(initialHeight: Dp = 150.dp): HeaderController {
    val controller = remember { HeaderController(initialHeight) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        controller.attachScope(scope)
    }
    return controller
}
