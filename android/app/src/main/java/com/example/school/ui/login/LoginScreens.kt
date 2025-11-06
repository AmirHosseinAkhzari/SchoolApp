package com.example.school.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.example.school.ui.theme.Surmeh
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * Entry screen that lets user choose login method.
 */
@Composable
fun LoginType(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "ورود",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.background
        )

        Spacer(Modifier.size(30.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            LoginOptionCard(
                title = "ورود با آستین",
                iconRes = R.drawable.astin
            ) {
                navController.navigate("LoginWithSleeve")
            }

            Spacer(Modifier.size(16.dp))

            LoginOptionCard(
                title = "ورود با شماره",
                iconRes = R.drawable.otp
            ) {
                navController.navigate("LoginWithNumber")
            }
        }
    }
}

/**
 * Reusable card for each login option.
 */
@Composable
private fun LoginOptionCard(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(160.dp, 240.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun LoginWithSleeve(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val loginViewModel = hiltViewModel<LoginWithSleeveViewModel>()
    val uiState = loginViewModel.uiState.collectAsState()
    var lodeing by remember { mutableStateOf(false) }

    // Start NFC once when screen is launched
    LaunchedEffect(Unit) {
        loginViewModel.resetData()
        loginViewModel.startNfc(activity)
    }

    // Navigate to OTP on successful login
    LaunchedEffect(uiState.value.network) {

        val data = uiState.value.network
        if (data == NetworkMode.Success) {
            navController.navigate("LoginOtpCode")
        } else if (data == NetworkMode.Loading) {
            lodeing = true
        }else{
            lodeing = false
        }
    }

    // Ensure NFC is stopped when leaving this screen
    DisposableEffect(Unit) {
        loginViewModel.startNfc(activity)
        onDispose {
            loginViewModel.stopNfc(activity)
        }
    }

    var mainModifier: Modifier = Modifier
    if (lodeing == true) {
        mainModifier = Modifier
            .alpha(0.1f)
    }

    Box(Modifier.fillMaxSize()) {
        if (lodeing == true) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(1f)
            )
        }
        Box(
            modifier = mainModifier.fillMaxSize()
        ) {
            BackButton {
                navController.navigate("LoginType")
            }



            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.loginwithsleeve),
                    contentDescription = "login with sleeve",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(260.dp)
                )

                Spacer(Modifier.size(12.dp))

                Text(
                    text = "گوشی رو به آستینت نزدیک کن",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.background
                )

                Spacer(Modifier.size(8.dp))

                // Animated guiding text from ViewModel
                Text(
                    text = uiState.value.guidText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

/**
 * Phone number login screen.
 */
@Composable
fun LoginWithNumber(navController: NavController) {
    val loginViewModel = hiltViewModel<LoginWithNumberViewModel>()
    val uiState = loginViewModel.uiState.collectAsState()
    var lodeing by remember { mutableStateOf(false) }

    // Navigate to OTP on successful login
    LaunchedEffect(uiState.value.network) {

        val data = uiState.value.network
        if (data == NetworkMode.Success) {
            navController.navigate("LoginOtpCode")
        } else if (data == NetworkMode.Loading) {
            lodeing = true
        }else{
            lodeing = false

        }
    }

    var mainModifier: Modifier = Modifier
    if (lodeing == true) {
        mainModifier = Modifier
            .alpha(0.1f)
    }



    Box(Modifier.fillMaxSize()) {
        if (lodeing == true) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(1f)
            )
        }
        Box(
            mainModifier.fillMaxSize()
        ) {
            var textState by remember { mutableStateOf("") }

            BackButton {
                navController.navigate("LoginType")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(Modifier.size(80.dp))

                Text(
                    text = "شمارتان را وارد کنید",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.background
                )

                Spacer(Modifier.size(24.dp))

                TextField(
                    value = textState,
                    onValueChange = { textState = it },
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
                        unfocusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            text = "شماره ...",
                            style = MaterialTheme.typography.displaySmall,
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(Modifier.size(48.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .width(260.dp)
                        .height(70.dp)
                        .clickable {
                            // trigger login
                            loginViewModel.loginWithNumber(textState)
                        }
                ) {
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

/**
 * OTP code screen with SMS User Consent.
 * Hidden TextField is used to capture the actual input.
 */
@SuppressLint("UnspecifiedRegisterReceiverFlag")
@Composable
fun LoginOtpCode(navController: NavController , MainNavController : NavController) {
    var lodeing by remember { mutableStateOf(false) }
    val LoginViewmModel = hiltViewModel<LoginOtpViewModel>()
    val uiState = LoginViewmModel.uiState.collectAsState()
    LaunchedEffect(uiState.value.network) {

        val data = uiState.value.network
        if (data == NetworkMode.Success) {
            navController.navigate("LoginOtpCode")
        } else if (data == NetworkMode.Loading) {
            lodeing = true
        }else{
            lodeing = false
        }



    }

    var mainModifier: Modifier = Modifier
    if (lodeing == true) {
        mainModifier = Modifier
            .alpha(0.1f)
    }
    Box(Modifier.fillMaxSize()) {
        if (lodeing == true) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(1f)
            )
        }
        Box(mainModifier.fillMaxSize()) {
            BackButton {
                navController.navigate("LoginType")
            }


            val otpLength = 5
            var otpText by remember { mutableStateOf("") }
            val context = LocalContext.current
            val keyboardController = LocalSoftwareKeyboardController.current
            val hiddenFieldFocus = remember { FocusRequester() }

            val sharedPref = context.getSharedPreferences("MyPref", MODE_PRIVATE)
            val token  = sharedPref.getString("token" , null)

            LaunchedEffect(token){
                if (token != null){
                    MainNavController.navigate("main")
                }
            }
            // Launcher to handle SMS user consent
            val smsConsentLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val message = result.data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    message?.let {
                        val otp = Regex("\\d{4,6}").find(it)?.value
                        otp?.let { code ->
                            otpText = code

                            keyboardController?.hide()
                            hiddenFieldFocus.freeFocus()
                        }
                    }
                }
            }

            // Start listening to SMS consent on first composition
            LaunchedEffect(Unit) {
                hiddenFieldFocus.requestFocus()
                val client = SmsRetriever.getClient(context)
                client.startSmsUserConsent(null)
            }

            // Register broadcast receiver for SMS Retriever
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

                                CommonStatusCodes.TIMEOUT -> {
                                    // SMS not received within timeout
                                }
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
                Text(
                    text = "کد ارسالی را وارد کنید",
                    color = MaterialTheme.colorScheme.background
                )
                Spacer(Modifier.size(30.dp))

                // Hidden text field that actually receives input
                Box {
                    TextField(
                        value = otpText,
                        onValueChange = { if (it.length != 6) otpText = it },
                        singleLine = true,
                        modifier = Modifier
                            .alpha(0f),
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

                    // Visual OTP boxes
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in 0 until otpLength) {
                            val isActive = otpText.length == i

                            val borderColor by animateColorAsState(
                                targetValue = if (isActive) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                                animationSpec = tween(150)
                            )

                            val size by animateDpAsState(
                                targetValue = if (isActive) 55.dp else 50.dp,
                                animationSpec = tween(150)
                            )

                            Text(
                                text = if (otpText.length > i) otpText[i].toString() else "",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .size(size)
                                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                    .wrapContentSize(Alignment.Center)
                                    .clickable {
                                        hiddenFieldFocus.requestFocus()
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
                        .background(MaterialTheme.colorScheme.background)
                        .width(180.dp)
                        .height(100.dp)
                        .clickable {
                            LoginViewmModel.CheckOtp(code = otpText, number = "09304682860" , context = context )

                        }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = "Check OTP",
                        modifier = Modifier.size(200.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

/**
 * Top-left back button used in all login screens.
 */
@Composable
private fun BoxScope.BackButton(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .size(60.dp)
            .padding(10.dp)
            .align(Alignment.TopStart)
            .clickable { onClick() },
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "back",
        tint = MaterialTheme.colorScheme.background,
    )
}
