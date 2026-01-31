package com.example.cote.ui.attendance.read

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cote.data.remote.Student
import com.example.cote.ui.Astin.addAstin.AddAstinViewModel
import com.example.cote.ui.Astin.addAstin.StudentItem
import com.example.cote.ui.Astin.addAstin.searchEngin
import com.example.cote.R
import com.example.cote.data.remote.ReqAttendanceChangeStatus
import com.example.cote.data.remote.stuAttendanceData
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import com.razaghimahdi.compose_persian_date.dialog.PersianLinearDatePickerDialog
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate

@Composable
fun PersianDatePicker(onDismissRequest : () -> Unit, onDateChanged : (Int , Int , Int) -> Unit  , year : Int , month : Int , day : Int) {


    val rememberPersianDialogDatePicker = rememberDialogDatePicker()


    LaunchedEffect(Unit) {

        rememberPersianDialogDatePicker.updateMaxYear(1404)
        rememberPersianDialogDatePicker.updateDate(
            persianYear =  year ,
            persianMonth = month ,
            persianDay = day
        )
    }



        PersianLinearDatePickerDialog(
            rememberPersianDialogDatePicker,
            modifier = Modifier
                .fillMaxWidth()
            ,
            dismissTitle = "تایید",
            onDismissRequest = onDismissRequest,
            onDateChanged = onDateChanged ,
            textButtonStyle = MaterialTheme.typography.bodyMedium ,
            backgroundColor = Color.White ,
            unSelectedTextStyle =  MaterialTheme.typography.bodyMedium ,
            selectedTextStyle =  MaterialTheme.typography.bodyMedium ,
            font = R.font.portadaregular
        )


}


@Composable
fun ReadAttendance(modifier: Modifier , navController: NavController){


    val viewModel = hiltViewModel<ReadAttendanceViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)!!

    var students by remember { mutableStateOf<List<stuAttendanceData>?>(null) }

    var query by remember { mutableStateOf("") }

    var DateDialog by remember { mutableStateOf(false) }

    var persianDate = PersianDate()


    // change list
    var  Newid   by remember { mutableStateOf("") }
    var Newstatus by remember { mutableStateOf("") }
    var updateList by remember { mutableStateOf(false) }



    var day by remember { mutableStateOf(persianDate.shDay) }
    var month by remember { mutableStateOf(persianDate.shMonth) }
    var year by remember { mutableStateOf(persianDate.shYear) }

    var Req by remember { mutableStateOf(true) }
    val date = "${year}/${month}/${day}"


    LaunchedEffect(Req) {
        if(Req){
            val res = viewModel.ReadAttendance(token , date).getOrNull()
            if(res != null){
                if(res.code == 200){
                    Log.d("testg" , res.data.toString())
                    students = res.data
                }
            }

            Req = false
        }
    }

    LaunchedEffect(updateList) {
        if(updateList){

            students = students
                ?.toMutableList()
                ?.map {
                    if (it.userId == Newid) {
                        it.copy(status = Newstatus)
                    } else {
                        it
                    }
                }

            updateList = false






        }
    }




    Column {


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(150.dp , 50.dp )
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .fillMaxWidth()
                .align(Alignment.End)
                .clickable{
                    DateDialog = true
                }


        ){
            Text(
                text = "${year}/${month}/${day}" ,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.sorena)),
                fontWeight = FontWeight(100),
                modifier = Modifier
                    .padding(10.dp)

            )
        }

        Spacer(Modifier.size(20.dp))

        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = {
                Text(
                    text = "... جسستوجو کنید",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            modifier = Modifier
                .padding(end = 10.dp, start = 10.dp)
                .fillMaxWidth()
                .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,

            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodySmall,


            )
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                if (students != null) {
                    items(searchEngin(query , students!!)) {
                        AttendanceItem(it  , date , ){ NewId , NewStatus ->
                            Newid = NewId
                            Newstatus = NewStatus
                            updateList = true
                        }
                    }
                }

            }
        }
    }


    if(DateDialog){
        Log.d("test3" , "done1 ")

        PersianDatePicker(
            onDismissRequest = {
                DateDialog = false
                Req = true},
            onDateChanged = { y, m, d ->
                year = y
                day =d
                month = m
            } ,
            year =  year ,
            month = month ,
            day = day
        )
        Log.d("yy" , year.toString())
        Log.d("yy" , day.toString())
        Log.d("yy" , month.toString())

    }

}

@Composable
fun AttendanceItem(student : stuAttendanceData  , date : String , onStatusChange : (String , String) -> Unit){


    var color = when (student.status) {
        "غایب" -> Color(0xFFC94C4C)
        "حاضر" -> Color(0xFF6CA76C)
        "دیر اومده" -> Color(0xFFD4C160)
        else -> Color(0xFF000000)
    }

    var dialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .clickable{
                dialog = true
            }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()

        ){
            Box (
                modifier = Modifier
                    .padding(start = 50.dp )
                    .align(Alignment.CenterStart)
                    .size(30.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color)
            )
            Spacer(Modifier.size(20.dp))

            Spacer(Modifier.size(10.dp))

            Text(
                text = student.fullname,
                style = MaterialTheme.typography.titleLarge ,
                color = Color.Black ,
                fontSize = 30.sp ,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd)
            )

        }
    }

    Spacer(Modifier.size(20.dp))


    if(dialog){
        attendanceDialog(student , {dialog = false} ,date , onStatusChange )
    }
}


@Composable
fun attendanceDialog(student : stuAttendanceData , onDismissRequest: () -> Unit , date: String  ,  onStatusChange : (String , String) -> Unit){

    Dialog(onDismissRequest) {


        Box(
            modifier = Modifier
                .size(600.dp , 450.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(6.dp , Color.Black )
            ){

                Spacer(Modifier.size(20.dp))
                Text(
                    text = student.status,
                    style = MaterialTheme.typography.titleLarge ,
                    color = Color.Black ,
                    fontSize = 36.sp ,
                    textAlign = TextAlign.Center ,
                    modifier = Modifier.fillMaxWidth()
                )

                QandA("اسم" , student.fullname)
                QandA("کلاس" , student.className)
                if(student.description != "—"){
                    QandA("ورود" , student.checkIn)
                }
                if(student.description != "—"){
                    QandA("دلیل غیبت" , student.description)
                }


                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    StateCircles(student.status , student.userId , date , onStatusChange)

                }







            }

        }
    }

}


@Composable
fun QandA(topic : String , answer : String){

    Spacer(Modifier.size(20.dp))


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Text(
            text = topic,
            style = MaterialTheme.typography.bodySmall ,
            color = Color.Black ,
            modifier = Modifier
                .padding(end = 26.dp)
                .align(Alignment.CenterEnd)
        )

        Text(
            text = answer ,
            style = MaterialTheme.typography.bodySmall ,
            color = Color.Black ,
            modifier = Modifier
                .padding(start = 26.dp)
                .align(Alignment.CenterStart)
        )

    }
}

fun searchEngin(
    q: String,
    allStu: List<stuAttendanceData>
): List<stuAttendanceData> {

    val query = q.trim()
    if (query.isEmpty()) return allStu

    return allStu.filter { student ->
        student.fullname
            .trim()
            .split(Regex("\\s+"))
            .any { it.startsWith(query) }
    }
}


@Composable
fun StateCircles(state : String , id : String , date : String  ,  onStatusChange : (String , String) -> Unit){

        var mainstate by remember { mutableStateOf(state) }

        Log.d("test78" ,mainstate )


        if (mainstate == "غایب") {
            StateCircle(Color(0xFFC94C4C), Color(0xFFC94C4C) , id = id , date = date  , status = "غایب" ){id , status ->
                mainstate = status
                onStatusChange(id , status)
            }
            StateCircle(Color(0xFFD4C160), Color.White , id = id  ,  date = date , status = "دیر اومده"   ){id , status ->
                mainstate = status
                onStatusChange(id , status)

            }
            StateCircle(Color(0xFF6CA76C), Color.White , false , id , date , status = "حاضر" ){id , status ->
                mainstate = status
                onStatusChange(id , status)
            }


        }else if (mainstate == "دیر اومده"){
            StateCircle(Color(0xFFC94C4C), Color.White , id = id ,  date = date , status = "غایب" ){id , status ->
                mainstate = status
                onStatusChange(id , status)
            }
            StateCircle(Color(0xFFD4C160), Color(0xFFD4C160) , id = id ,  date = date, status = "دیر اومده"){id , status ->
                mainstate = status
                onStatusChange(id , status)
            }
            StateCircle(Color(0xFF6CA76C), Color.White , false , id  ,  date = date , status = "حاضر"){id , status ->
                mainstate = status
                onStatusChange(id , status)
            }
        }else if(mainstate == "حاضر"){
            StateCircle(Color(0xFFC94C4C), Color.White , id = id ,  date = date , status = "غایب"){id , status ->
                mainstate = status
                onStatusChange(id , status)
            }
            StateCircle(Color(0xFFD4C160), Color.White , id = id , date = date , status = "دیر اومده"){id , status ->
                mainstate = status
                onStatusChange(id , status)

            }
            StateCircle(Color(0xFF6CA76C), Color(0xFF6CA76C) , false , id  ,  date = date , status = "حاضر" ){ id , status ->
                mainstate = status
                onStatusChange(id , status)
            }
        }



}

@Composable
fun StateCircle(mainColor  : Color , secColor : Color , SpacerMode : Boolean = true , id : String ,  date : String , status : String , onDateChanged: (String , String) -> Unit){


    var req by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ReadAttendanceViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)


    LaunchedEffect(req) {
        if(req){
            Log.d("out" , ReqAttendanceChangeStatus(
                id = id ,
                date = date ,
                status = status
            ).toString())
            val res = viewModel.ChangeStatusAttendance(token!! ,
                ReqAttendanceChangeStatus(
                    id = id ,
                    date = date ,
                    status = status
                ))

            if(res.getOrNull() != null){
                if(res.getOrNull()!!.code == 200){
                    onDateChanged(id , status )
                }
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center ,
        modifier = Modifier
            .clickable{
                req = true
            }
    ){
        Box (
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(mainColor)
        )

        Box (
            modifier = Modifier

                .size(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(secColor)
        )
    }

    if(SpacerMode){
        Spacer(Modifier.size(20.dp))
    }

}

