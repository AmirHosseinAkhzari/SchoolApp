package com.example.cote.ui.Student

import com.example.cote.ui.Class.ClassMainPageViewModel


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
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
import com.example.cote.data.remote.ReqAddStudent
import com.example.cote.data.remote.Student
import com.example.cote.data.remote.StudentFullData
import com.example.cote.ui.Class.ClassSettingDialog
import com.example.cote.ui.Class.DeleteClassDialog
import com.example.cote.ui.Class.EditClassDialog
import com.example.cote.ui.login.isKeyboardOpen
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent


data class InputModel(
    val name : String ,
    val value : String ,
    val onValueChange :  (String) -> Unit ,
    val keyboardType: KeyboardType  =KeyboardType.Text  ,
    val onClick : () -> Unit = {} ,
    val enable : Boolean = true
)

//
//var name by remember { mutableStateOf("") }
//var family by remember { mutableStateOf("") }
//var birthDate by remember { mutableStateOf("") }
//var nationalCode by remember { mutableStateOf("") }
//var studentPhone by remember { mutableStateOf("") }
//var parentPhone by remember { mutableStateOf("") }
//var homePhone by remember { mutableStateOf("")}
data class stuData(
    val firstName : String ,
    val lastname : String ,
    val  birthDate : String ,
    val nationalCode : String ,
    val studentPhone : String ,
    val parentPhone : String ,
    val homePhone : String ,
)

@Composable
fun StudentMainPage(modifier: Modifier , navController: NavController){

    val viewModel = hiltViewModel<StudentMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)

    var Students by remember { mutableStateOf<List<Student>?>(null) }

    var EditStudent by remember { mutableStateOf(false) }

    var StudentData by remember { mutableStateOf<StudentFullData?>(null) }



    var AddStudentDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        val res = viewModel.ReadStudent(token!!)

        if(res.getOrNull() != null){
            Log.d("tagoapd" , res.toString())
            if(res.getOrNull()!!.code == 200 ){
                Students = res.getOrNull()!!.students
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
                        AddStudentDialog = true
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

        if (Students != null) {
            LazyColumn {
                items(Students!!) {
                    StudentItem(it, navController ,  )
                }
            }
        }
    }

    if(AddStudentDialog){
        AddOrEditStudentDialog({AddStudentDialog = false} , navController , "add" , null)
    }


}


@Composable
fun StudentItem(Stu : Student , navController: NavController){


    var SettingDialog by remember { mutableStateOf(false) }

    var EditDialog by remember { mutableStateOf(false) }

    var DeleteDialog by remember { mutableStateOf(false) }

    val FullName = Stu.firstname + " " + Stu.lastname
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
        DeleteStudentDialog(Stu._id , {DeleteDialog = false} , navController)
    }

    if(EditDialog){
        AddOrEditStudentDialog({EditDialog = false} , navController , "edit" , Stu._id)
    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditStudentDialog(
    onDismissRequest: () -> Unit,
    navController: NavController ,
    mode : String ,
    id : String?

) {


//    var name by remember { mutableStateOf("امیرحسین") }
//    var family by remember { mutableStateOf("اخضری") }
//    var birthDate by remember { mutableStateOf("1404/01/01") }
//    var nationalCode by remember { mutableStateOf("2500766111") }
//    var studentPhone by remember { mutableStateOf("09304682860") }
//    var parentPhone by remember { mutableStateOf("09304682860") }
//    var homePhone by remember { mutableStateOf("07152249874")}

    val viewModel = hiltViewModel<StudentMainPageViewModel>()
    val context = LocalContext.current
    val token = viewModel.GetMainToken(context)

    var stu by remember { mutableStateOf<StudentFullData?>(null) }

    LaunchedEffect(Unit) {
        if(mode == "edit"){
            val res = viewModel.ReadOneStudent(token!! , id!! )
            Log.d("resReadOneStu" , res.getOrNull().toString())

            if(res.getOrNull() != null) {
                if(res.getOrNull()!!.code == 200){
                    stu = res.getOrNull()!!.data
                }
            }
        }
    }

    var name by remember { mutableStateOf("") }
    var family by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var nationalCode by remember { mutableStateOf("") }
    var studentPhone by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var homePhone by remember { mutableStateOf("")}
    var ClassName by remember { mutableStateOf("")}
    var ClassId by remember { mutableStateOf("")}



    LaunchedEffect(stu) {
        if( stu!= null){
            name = stu!!.firstname
            family = stu!!.lastname
            birthDate = stu!!.birthday
            nationalCode = stu!!.nationalid
            studentPhone = stu!!.number
            parentPhone = stu!!.ParentNumber
            homePhone = stu!!.LocalNumber
            ClassName = stu!!.className
            ClassId = stu!!.classId
        }
    }


    var SelctedClass by remember { mutableStateOf<ClassModel?>(null)}

    var ClassDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var submit by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    if(SelctedClass != null){
        ClassName = SelctedClass!!.name
        ClassId = SelctedClass!!._id
    }



    val inputList = listOf<InputModel>(
        InputModel(
            name = "نام" ,
            value = name ,
            onValueChange = {name  = it} ,
        ) ,
        InputModel(
            name = "نام خانوادگی" ,
            value = family ,
            onValueChange = {family  = it} ,
        ) ,
        InputModel(
            name = "تاریخ تولد (1404/01/01)" ,
            value = birthDate ,
            onValueChange = {birthDate  = it} ,
        ) ,
        InputModel(
            name ="کد ملی" ,
            value = nationalCode ,
            onValueChange = {nationalCode  = it} ,
            keyboardType = KeyboardType.Number
        ) ,
        InputModel(
            name ="شماره دانش‌آموز" ,
            value = studentPhone ,
            onValueChange = {studentPhone  = it} ,
            keyboardType = KeyboardType.Number
        ) ,
        InputModel(
            name ="شماره ولی" ,
            value = parentPhone ,
            onValueChange = {parentPhone  = it} ,
            keyboardType = KeyboardType.Number
        ) ,
        InputModel(
            name ="شماره منزل" ,
            value = homePhone ,
            onValueChange = {homePhone  = it} ,
            keyboardType = KeyboardType.Number
        ),
        InputModel(
            name = "کلاس" ,
            value = if(ClassName == "") "کلاس ..." else ClassName,
            onValueChange = {} ,
            enable = false ,
            onClick = {ClassDialog = true}
        )



    )

    val focusRequesters = remember {
        List(inputList.size) { FocusRequester() }
    }




    fun isValid(): Boolean {
        if (!birthDate.matches(Regex("\\d{4}/\\d{2}/\\d{2}")))
            return false.also { error = "فرمت تاریخ تولد نادرست است" }

        if (!nationalCode.matches(Regex("\\d{10}")))
            return false.also { error = "کد ملی باید ۱۰ رقم باشد" }

        if (!studentPhone.matches(Regex("09\\d{9}")))
            return false.also { error = "شماره دانش‌آموز نادرست است" }

        if (!parentPhone.matches(Regex("09\\d{9}")))
            return false.also { error = "شماره ولی نادرست است" }

        if (!homePhone.matches(Regex("0\\d{10,}")))
            return false.also { error = "شماره منزل نادرست است" }

        if(mode == "add"){
            if(SelctedClass == null){
                return false.also { error = "کلاسی انتخاب کنید" }
            }
        }

        error = null
        return true
    }
    LaunchedEffect(submit) {
        if(submit && isValid()){


            if(mode == "add"){
                val res = viewModel.AddStudent(token!! ,
                    ReqAddStudent(
                        name ,
                        family,
                        birthDate ,
                        nationalCode ,
                        studentPhone ,
                        parentPhone ,
                        homePhone ,
                        SelctedClass!!._id,
                        "Student"
                    ))

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
            }else if(mode == "edit"){
                val res = viewModel.UpdateStudent(token!! ,
                    id!! ,
                    ReqAddStudent(
                        name ,
                        family,
                        birthDate ,
                        nationalCode ,
                        studentPhone ,
                        parentPhone ,
                        homePhone ,
                        ClassId ,
                        "Student"
                    ))

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

        }else{
            submit = false
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .width(420.dp)
                .height(500.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp , Color.Black , RoundedCornerShape(30.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally  ,
                verticalArrangement = Arrangement.Center ,
                modifier = Modifier
                    .fillMaxSize()
            ) {


                Text(
                    text = "دانش آموز جدید" ,
                    color = Color.Black,
                    fontSize = 28.sp
                )


                Spacer(Modifier.height(16.dp))


                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    itemsIndexed(inputList) { index, item ->


                            Input(
                                it = item,
                                focusRequester = focusRequesters[index],
                                onNext = {
                                    if (index < focusRequesters.lastIndex) {
                                        focusRequesters[index + 1].requestFocus()
                                    } else {
                                        focusManager.clearFocus()
                                    }
                                },
                                isLast = index == focusRequesters.lastIndex
                            )
                    }


                }

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(Modifier.height(16.dp))
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

                Spacer(Modifier.height(16.dp))

            }
        }

    }

    if(ClassDialog){
        ChoseClass(
            navController ,
            {ClassDialog = false} ,
            {
                SelctedClass = it
                ClassDialog = false
            }
        )
    }

}


@Composable
fun ChoseClass( navController: NavController  , onDismissRequest: () -> Unit , onClick : (ClassModel) -> Unit) {

    val viewModel = hiltViewModel<StudentMainPageViewModel>()
    val context = LocalContext.current
    val token = viewModel.GetMainToken(context)
    var Classes by remember { mutableStateOf<List<ClassModel>?>(null) }


    LaunchedEffect(Unit) {
        val res = viewModel.ReadClass(token!!)

        if(res.getOrNull() != null ){
            if(res.getOrNull()!!.code == 200){
                Classes = res.getOrNull()!!.data
            }
        }
    }




    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .size(400.dp, 650.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(10.dp)
                .border(4.dp, Color.Black, RoundedCornerShape(30.dp))

        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ){

                Text(
                    text = "یک کلاس را انتخاب کنید" ,
                    color = Color.Black,
                    fontSize = 28.sp
                )

                if(Classes != null){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ){

                        items(Classes!!){
                            ClassItem(it , navController , onClick)
                        }
                    }
                }



            }


        }
    }


}


@Composable
fun ClassItem(Class : ClassModel , navController: NavController , onClick : (ClassModel) -> Unit){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp , end = 20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.background )
            .clickable{
                onClick(Class)
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
                color = Color.White ,
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




@Composable
fun Input(
    it: InputModel,
    focusRequester: FocusRequester,
    onNext: () -> Unit,
    isLast: Boolean
) {
    TextField(
        value = it.value,
        onValueChange = it.onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
        modifier = Modifier
            .width(280.dp)
            .clickable{
                it.onClick()
                Log.d("onclick" , "done")
            }
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = it.keyboardType,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onNext() }
        ),
        placeholder = {
            Text(
                text = it.name,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        shape = RoundedCornerShape(16.dp) ,
        enabled = it.enable
    )

    Spacer(Modifier.size(20.dp))
}





@Composable
fun DeleteStudentDialog(id : String , onDismissRequest : () -> Unit , navController: NavController) {


    var  timer by remember { mutableStateOf(10) }
    var  text by remember { mutableStateOf("10") }
    var  btnColor by remember { mutableStateOf(Color(0xFF3F3C3C)) }
    var  deleteClass by remember { mutableStateOf(false) }


    val viewModel = hiltViewModel<StudentMainPageViewModel>()

    val context = LocalContext.current

    val token = viewModel.GetMainToken(context)



    if(timer == 0) {
        text = "حذف دانش آموز"
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
                val res = viewModel.DeleteStudent(token!! , id )
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
                        text = "در صورت حذف کردن دانش آموز هر نوع دیتا مثل حضور و غیاب اطلاعات ثبت شده استین ها و.. به صورت کامل حذف می شود",
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

