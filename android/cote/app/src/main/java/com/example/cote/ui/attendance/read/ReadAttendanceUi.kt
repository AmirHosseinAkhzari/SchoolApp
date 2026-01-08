package com.example.cote.ui.attendance.read

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cote.data.remote.Student
import com.example.cote.ui.Astin.addAstin.AddAstinViewModel
import com.example.cote.ui.Astin.addAstin.StudentItem
import com.example.cote.ui.Astin.addAstin.searchEngin
import com.example.cote.R
import com.razaghimahdi.compose_persian_date.bottom_sheet.DatePickerLinearModalBottomSheet
import com.razaghimahdi.compose_persian_date.core.components.rememberDialogDatePicker
import com.razaghimahdi.compose_persian_date.dialog.PersianLinearDatePickerDialog
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate

@Composable
fun PersianDatePicker(onDismissRequest : () -> Unit, onDateChanged : (Int , Int , Int) -> Unit ) {


    val rememberPersianDialogDatePicker = rememberDialogDatePicker()


    LaunchedEffect(Unit) {

        rememberPersianDialogDatePicker.updateMaxYear(1404)
        rememberPersianDialogDatePicker.updateMinYear(1404)

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


    val viewModel = hiltViewModel<AddAstinViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)!!

    var students by remember { mutableStateOf<List<Student>?>(null) }

    var query by remember { mutableStateOf("") }

    var DateDialog by remember { mutableStateOf(false) }

    var persianDate = PersianDate()




    var day by remember { mutableStateOf(persianDate.shDay) }
    var month by remember { mutableStateOf(persianDate.shMonth) }
    var year by remember { mutableStateOf(persianDate.shYear) }


    LaunchedEffect(Unit) {


        val res = viewModel.ReadStudent(token)

        if (res.isSuccess){
            students = res.getOrNull()!!.students
        }

        Log.d("Stu" , students.toString())
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
                    items(searchEngin(query, students!!)) {
                        StudentItem(it, navController)
                    }
                }

            }
        }
    }


    if(DateDialog){
        Log.d("test3" , "done1 ")

        PersianDatePicker(
            onDismissRequest = { DateDialog = false },
            onDateChanged = { y, m, d ->
                year = y
                day =d
                month = m
            }
        )
        Log.d("yy" , year.toString())
        Log.d("yy" , day.toString())
        Log.d("yy" , month.toString())


    }

}
