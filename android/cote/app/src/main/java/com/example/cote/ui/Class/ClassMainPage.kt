package com.example.cote.ui.Class

import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cote.data.remote.ClassModel
import com.example.cote.R
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent

@Composable
fun ClassMainPage(modifier: Modifier , navController: NavController){

    val viewModel = hiltViewModel<ClassMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)

    var Classes by remember { mutableStateOf<List<ClassModel>?>(null) }

    var AddClassDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        val res = viewModel.ReadClass(token!!)

        if(res.getOrNull() != null){
            Log.d("tagoapd" , res.toString())
            if(res.getOrNull()!!.code == 200 ){
                Classes = res.getOrNull()!!.data
            }
        }

        Log.d("tagoapd" , Classes.toString())
    }

    Column(
        Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(end = 20.dp)

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp, 55.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .align(Alignment.CenterEnd)
                    .clickable{
                        AddClassDialog = true
                    }

            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    tint = Color.White,
                    contentDescription = "add" ,
                    modifier = Modifier
                        .size(30.dp)
                )

            }
        }
        Spacer(Modifier.size(30.dp))

        if (Classes != null) {
            LazyColumn {
                items(Classes!!) {
                    ClassItem(it, navController)
                }
            }
        }
    }

    if(AddClassDialog){
        AddClassDialog({AddClassDialog = false} , navController)
    }


}


@Composable
fun ClassItem(Class : ClassModel , navController: NavController){


    var SettingDialog by remember { mutableStateOf(false) }

    var EditDialog by remember { mutableStateOf(false) }

    var DeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White )
            .clickable{
                SettingDialog = true
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
                text = Class.name,
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

    if(SettingDialog){
        ClassSettingDialog(
            {SettingDialog = false } ,
            {SettingDialog = false
                DeleteDialog = true}  ,
            {SettingDialog = false
            EditDialog = true}
        )
    }

    if(DeleteDialog){
        DeleteClassDialog(Class._id ,   {DeleteDialog = false} , navController)
    }

    if(EditDialog){
        EditClassDialog(Class._id , {EditDialog = false} , navController  , Class.name)
    }

}


@Composable
fun ClassSettingDialog(onDismissRequest : () -> Unit , onDeleteDialogClicked : () -> Unit , onEditDialogClicked : () -> Unit ) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .size(400.dp, 350.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp , Color.Black , RoundedCornerShape(30.dp))

        ) {

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize() ,
            ){

                Spacer(Modifier.size(40.dp))

                Text(
                    text = "تنظیمات",
                    color = Color.Black ,
                    fontSize = 34.sp
                )
                Spacer(Modifier.size(20.dp))


                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                ) {


                    ClassChangeOption("حذف", R.drawable.delete , onDeleteDialogClicked)
                    Spacer(Modifier.size(20.dp))

                    ClassChangeOption("ادیت", R.drawable.edit , onEditDialogClicked)


                }
            }
        }
    }
}


@Composable
fun EditClassDialog(
    id : String , onDismissRequest: () -> Unit , navController: NavController , oldName : String
){


    var NewName by remember { mutableStateOf(oldName) }

    var Submit  by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ClassMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)



    LaunchedEffect(Submit) {
        if(Submit){
            val res = viewModel.ChangeClassName(token!! , id , NewName)

            Log.d("ResChangeName"  , res.toString())
            if(res.getOrNull() != null) {
                if(res.getOrNull()!!.code == 200 ){
                    val message = "عملیات با موفقیت انجام شد"
                    navController.navigate("checkMarkPage/200/${message}")
                }
            }else{
                val message = "مشکلی پیش آمده"
                navController.navigate("checkMarkPage/500/${message}")
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest ,

    ) {

        Box(
            modifier = Modifier
                .size(450.dp, 350.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp , Color.Black , RoundedCornerShape(30.dp))
        ){

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ){

                Spacer(Modifier.size(40.dp))

                Text(
                    text = "ادیت" ,
                    color = Color.Black,
                    fontSize = 34.sp
                )

                Spacer(Modifier.size(40.dp))

                TextField(
                    value = NewName,
                    onValueChange = { NewName = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .width(280.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background ,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                    placeholder = {
                        Text(
                            text = "اسم جدید ...",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.size(30.dp))


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(150.dp , 70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black)
                        .clickable{
                            Submit = true
                        }
                ){

                    Text(
                        text = "ثبت" ,
                        color = Color.White,
                        fontSize = 34.sp
                    )

                }
            }

        }
    }
}

@Composable
fun AddClassDialog(
     onDismissRequest: () -> Unit , navController: NavController
){


    var name by remember { mutableStateOf("") }

    var Submit  by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<ClassMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)



    LaunchedEffect(Submit) {
        if(Submit){
            val res = viewModel.AddClass(token!! , name)

            Log.d("ResChangeName"  , res.toString())
            if(res.getOrNull() != null) {
                if(res.getOrNull()!!.code == 200 ){
                    val message = "عملیات با موفقیت انجام شد"
                    navController.navigate("checkMarkPage/200/${message}")
                }
            }else{
                val message = "مشکلی پیش آمده"
                navController.navigate("checkMarkPage/500/${message}")
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest ,

        ) {

        Box(
            modifier = Modifier
                .size(450.dp, 350.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp , Color.Black , RoundedCornerShape(30.dp))
        ){

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ){

                Spacer(Modifier.size(40.dp))

                Text(
                    text = "کلاس جدید" ,
                    color = Color.Black,
                    fontSize = 34.sp
                )

                Spacer(Modifier.size(40.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .width(280.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background ,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text ),
                    placeholder = {
                        Text(
                            text = "اسم کلاس ...",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.size(30.dp))


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(150.dp , 70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black)
                        .clickable{
                            Submit = true
                        }
                ){

                    Text(
                        text = "ادامه" ,
                        color = Color.White,
                        fontSize = 34.sp
                    )

                }
            }

        }
    }
}


@Composable
fun DeleteClassDialog(id : String , onDismissRequest : () -> Unit , navController: NavController) {


    var  timer by remember { mutableStateOf(10) }
    var  text by remember { mutableStateOf("10") }
    var  btnColor by remember { mutableStateOf(Color(0xFF3F3C3C)) }
    var  deleteClass by remember { mutableStateOf(false) }


    val viewModel = hiltViewModel<ClassMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)



    if(timer == 0) {
        text = "حذف کلاس"
        btnColor= Color(0xFFC94C4C)
    }

    LaunchedEffect(Unit) {
        for(i in 1..10){
            delay(1000)
            timer -= 1
            text = timer.toString()
        }
    }

    LaunchedEffect(deleteClass) {
        if(deleteClass) {
            if(btnColor ==  Color(0xFFC94C4C)){
                val res = viewModel.DeleteClass(token!! , id )
                if(res.getOrNull() != null) {
                    if(res.getOrNull()!!.code == 200 ){
                        val message = "عملیات با موفقیت انجام شد"
                        navController.navigate("checkMarkPage/200/${message}")
                    }
                }else{
                    val message = "مشکلی پیش آمده"
                    navController.navigate("checkMarkPage/500/${message}")
                }
            }
        }
        deleteClass = false
    }
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .size(550.dp, 500.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp , Color.Black , RoundedCornerShape(30.dp))
                .clickable{
                    deleteClass = true
                }
        ) {

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize() ,
            ){

                Spacer(Modifier.size(40.dp))

                Text(
                    text = "دقت کنید",
                    color = Color(0xFFC94C4C) ,
                    fontSize = 34.sp
                )
                Spacer(Modifier.size(10.dp))


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                ) {


                    Text(
                        text = "در صورت پاک کردن این کلاس کل دانش آموزان مربوط به این سیستم حذف می شوند کل حضور و غیاب های انجام شده حذف شده و دیگر قابل رویت نمی باشد در صورت تایید بعد از تمام شدن تایمر دکمه حذف را فشار دهید",
                        color = Color.Black ,
                        fontSize = 20.sp ,
                        maxLines = 6 ,
                        textAlign = TextAlign.Center ,
                        modifier = Modifier
                            .padding(8.dp)
                    )

                    Spacer(Modifier.size(30.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(start = 10.dp , end = 10.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(btnColor)
                    ){
                        Text(
                            text = text,
                            color = Color.White ,
                            fontSize = 34.sp
                        )
                    }




                }
            }
        }
    }
}



@Composable
fun ClassChangeOption(text : String , ImageId  : Int , onDismissRequest: () -> Unit){

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(110.dp , 160.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable{
                onDismissRequest()
            }
            .background(MaterialTheme.colorScheme.background)
    ){

        Column(
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Spacer(Modifier.size(15.dp))

            Icon(
                painter = painterResource(ImageId) ,
                contentDescription = text ,
                tint = Color.White ,
                modifier = Modifier
                    .size(80.dp)
            )

            Text(
                text = text,
                color = Color.White ,
                fontSize = 26.sp
            )


            Spacer(Modifier.size(15.dp))

        }
    }


}