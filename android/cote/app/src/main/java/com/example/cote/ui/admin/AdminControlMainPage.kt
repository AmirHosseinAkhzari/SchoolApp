package com.example.cote.ui.admin

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
import com.example.cote.data.remote.Admin
import com.example.cote.data.remote.ReqAddAdmin
import com.example.cote.ui.Class.AddClassDialog
import com.example.cote.ui.Class.ClassChangeOption
import com.example.cote.ui.Student.Input
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent

@Composable
fun AdminControlMainPage(modifier: Modifier , navController: NavController){

    val viewModel = hiltViewModel<AdminControlMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)

    var Admins by remember { mutableStateOf<List<Admin>?>(null) }

    var AddAdminDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        val res = viewModel.ReadAdmins(token!!)

        if(res.getOrNull() != null){
            if(res.getOrNull()!!.code == 200 ){
                Admins = res.getOrNull()!!.data
            }
        }

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
                        AddAdminDialog = true
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

        if (Admins != null) {
            LazyColumn {
                items(Admins!!) {
                    AdminItem(it, navController)
                }
            }
        }
    }

    if(AddAdminDialog){
         AddAdminDialog({ AddAdminDialog =false } , navController)
    }


}


@Composable
fun AdminItem(admin : Admin , navController: NavController){




    var DeleteDialog by remember { mutableStateOf(false) }

    val fullName = admin.firstname + " " + admin.lastname

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White )
            .clickable{
                DeleteDialog = true
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
                text = fullName,
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

    if(DeleteDialog){
        DeleteAdminDialog({DeleteDialog = false} , admin._id , navController)
    }




}


@Composable
fun DeleteAdminDialog(
    onDismissRequest : () -> Unit ,
    id: String ,
    navController: NavController
){


    var Delete by remember { mutableStateOf(false) }

    val viewModel = hiltViewModel<AdminControlMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)

    LaunchedEffect(Delete) {
        if(Delete){
            val res = viewModel.DeleteAdmin(token!! , id  )
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize() ,
            ){


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .clickable{
                            Delete = true
                        }

                ){

                    Icon(
                        painter = painterResource(R.drawable.delete) ,
                        tint = Color.White ,
                        modifier = Modifier.size(100.dp) ,
                        contentDescription = "delete"
                    )

                }
            }
        }
    }
}

@Composable
fun AddAdminDialog(
    onDismissRequest : () -> Unit ,
    navController: NavController
){


    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    var submit by remember { mutableStateOf(false) }



    val viewModel = hiltViewModel<AdminControlMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)

    fun isValid(): Boolean {
        if (!number.matches(Regex("09\\d{9}")))
            return false.also { error = "شماره ادمین اشتباه است" }

        error = null
        return true
    }

    LaunchedEffect(submit) {

        if(submit && isValid()){

            val res = viewModel.AddAdmin(token!! , ReqAddAdmin(number , firstname , lastname))

            if(res.getOrNull() != null) {
                if(res.getOrNull()!!.code == 200 ){
                    val message = "عملیات با موفقیت انجام شد"
                    navController.navigate("checkMarkPage/200/${message}")
                }
            }else{
                val message = "مشکلی پیش آمده"
                navController.navigate("checkMarkPage/500/${message}")
            }
        }else {
            submit = false
        }

    }




    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .size(350.dp, 450.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp , Color.Black , RoundedCornerShape(30.dp))

        ) {

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize() ,
            ){

                Text(
                    text = "ادمین جدید" ,
                    color = Color.Black,
                    fontSize = 28.sp
                )

                NormalInput(
                    firstname ,
                    {firstname = it } ,
                    "اسم ..."
                )

                NormalInput(
                    lastname,
                    {lastname = it } ,
                    "فامیل ..."
                )

                NormalInput(
                    number,
                    {number = it } ,
                    "شماره ..." ,
                    KeyboardType.Number
                )


                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(Modifier.size(20.dp))


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(150.dp , 70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black)
                        .clickable{
                            submit = true
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
fun NormalInput(
    value : String ,
    onValueChange : (String) -> Unit ,
    placeholder  : String ,
    keyboardType: KeyboardType = KeyboardType.Text ,

){
    Spacer(Modifier.size(10.dp))

    TextField(
        value = value,
        onValueChange = onValueChange,
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType ),
        placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
        },
        shape = RoundedCornerShape(16.dp)
    )
}