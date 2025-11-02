package com.example.school.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.school.R
import com.example.school.ui.theme.DarkGreen
import com.example.school.ui.theme.Milky
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.delay


@Composable
fun LoginType(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.size(20.dp))
        Text(
            text = "ورود",
            style = MaterialTheme.typography.titleMedium ,
            color = MaterialTheme.colorScheme.onBackground

        )

        Spacer(Modifier.size(30.dp))

        Row {


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        shadowElevation = 10.dp.toPx()
                        shape = RoundedCornerShape(20.dp)
                        clip = false
                        ambientShadowColor = Color(0xFF2196F3)
                        spotShadowColor = Color(0xFF2196F3)
                    }
                    .size(160.dp, 250.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate("LoginWithSleeve")
                    }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        painter = painterResource(R.drawable.astin),
                        contentDescription = "login",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(90.dp)

                    )

                    Text(
                        text = "ورود با آستین",
                        color = MaterialTheme.colorScheme.onBackground ,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

            }

            Spacer(Modifier.size(20.dp))


            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        shadowElevation = 10.dp.toPx()
                        shape = RoundedCornerShape(20.dp)
                        clip = false
                        ambientShadowColor = Color(0xFF2196F3)
                        spotShadowColor = Color(0xFF2196F3)
                    }
                    .size(160.dp, 250.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                    .clickable {
                        navController.navigate("LoginWithNumber")
                    }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        painter = painterResource(R.drawable.otp),
                        contentDescription = "login",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(90.dp)
                    )

                    Text(
                        text = "ورود با شماره",
                        color = MaterialTheme.colorScheme.onBackground ,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
        }
    }
}


@Composable
fun LoginWithSleeve(navController: NavController) {




    val context = LocalContext.current

    val activity = context as Activity

    val LoginViewModel = hiltViewModel<LoginViewModel>()

    val uiState = loginViewModel().uiState.collectAsState()

    LoginViewModel.startNfc(activity)

//    DisposableEffect(Unit) {
//        val adapter  = NfcAdapter.getDefaultAdapter(context)
//
//        val callback = NfcAdapter.ReaderCallback { tag ->
//            val uid: ByteArray = tag.id
//            val hex = uid.joinToString("") { "%02X".format(it) }
//
//            activity.runOnUiThread {
//                Uid = hex
//            }
//
//        }
//
//        adapter.enableReaderMode(
//            activity ,
//            callback ,
//            NfcAdapter.FLAG_READER_NFC_A or
//            NfcAdapter.FLAG_READER_NFC_B ,
//            null
//        )
//
//        onDispose {
//            adapter?.disableReaderMode(activity)
//        }
//    }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {



        Spacer(Modifier.size(20.dp))

        Icon(
            painter = painterResource(R.drawable.loginwithsleeve),
            contentDescription = "login",
            tint = Milky,
            modifier = Modifier.size(300.dp)
        )



        Text(
            text = "گوشی رو به آستینت نزدیک کن",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.size(10.dp))

        Text(
            text = uiState.value.guidText,
            style = MaterialTheme.typography.bodyMedium,
            color = Milky
        )
    }
}

@Composable
private fun loginViewModel() = hiltViewModel<LoginViewModel>()


@Composable
fun LoginWithNumber(navController: NavController) {

    var textState by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer(Modifier.size(80.dp))

        Text(
            text = "شمارتان را وارد کنید" ,
        )

        Spacer(Modifier.size(30.dp))

        TextField(
            value = textState,
            onValueChange = { textState = it },
            singleLine = true,
            textStyle = MaterialTheme.typography.displaySmall ,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent ,
                unfocusedIndicatorColor = Color.Transparent ,
                disabledIndicatorColor = Color.Transparent ,
            ) ,
            placeholder = {
                Text(
                    text = "شماره ...",
                    style = MaterialTheme.typography.displaySmall,
                )
            }
        )

        Spacer(Modifier.size(40.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier =  Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(DarkGreen)
                .width(180.dp)
                .height(100.dp)
        ){
            Icon(
                imageVector = Icons.Rounded.Done ,
                contentDescription = "CheckMark" ,
                modifier = Modifier
                    .size(200.dp)
            )
        }


    }
}


@SuppressLint("UnspecifiedRegisterReceiverFlag")
@Composable
fun LoginOtpCode(navController: NavController) {
    val otpLength = 5
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val hiddenFieldFocus = remember { FocusRequester() }




    val smsConsentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let {
                val otp = Regex("\\d{4,6}").find(it)?.value
                otp?.let { code ->
                    textState = TextFieldValue(code)
                    keyboardController?.hide()
                    hiddenFieldFocus.freeFocus()
                }
            }
        }
    }



    LaunchedEffect(Unit) {
        hiddenFieldFocus.requestFocus()
        val client = SmsRetriever.getClient(context)
        client.startSmsUserConsent(null)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }


    DisposableEffect(Unit) {
        val smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
                    val extras = intent.extras
                    val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
                    when (status?.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val consentIntent =
                                extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                            consentIntent?.let { smsConsentLauncher.launch(it) }
                        }
                        CommonStatusCodes.TIMEOUT -> { /* Timeout */ }
                    }
                }
            }
        }
        context.registerReceiver(
            smsReceiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        )
        onDispose {
            context.unregisterReceiver(smsReceiver)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.size(80.dp))
        Text(text = "کد ارسالی را وارد کنید")
        Spacer(Modifier.size(30.dp))

        Box {
            TextField(
                value = textState,
                onValueChange = {
                    if (it.text.length <= otpLength) textState = it
                    if (it.text.length == otpLength) {
                        keyboardController?.hide()
                        hiddenFieldFocus.freeFocus()
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .alpha(0f)
                    .focusRequester(hiddenFieldFocus),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Transparent,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.Transparent,
                        backgroundColor = Color.Transparent
                    ),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in 0 until otpLength) {
                    val isActive = textState.text.length == i

                    val borderColor by animateColorAsState(
                        targetValue = if (isActive) Color.Cyan else Color.Gray,
                        animationSpec = tween(150)
                    )

                    val size by animateDpAsState(
                        targetValue = if (isActive) 55.dp else 50.dp,
                        animationSpec = tween(150)
                    )

                    Text(
                        text = if (textState.text.length > i) textState.text[i].toString() else "",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier
                            .size(size)
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .wrapContentSize(Alignment.Center)
                            .clickable {
                                hiddenFieldFocus.requestFocus()
                                textState = textState.copy(selection = TextRange(i))
                                keyboardController?.show()
                            }
                    )
                }
            }
        }

        Spacer(Modifier.size(40.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF006400)) // DarkGreen
                .width(180.dp)
                .height(100.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Done,
                contentDescription = "CheckMark",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}


