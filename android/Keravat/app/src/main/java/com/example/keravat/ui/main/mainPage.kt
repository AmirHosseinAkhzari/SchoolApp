package com.example.keravat.ui.main

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keravat.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Returns a color mapping based on the system theme.
 */
@Composable
fun appColors(): Map<String, Color> {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        mapOf(
            "attendance" to Color(0xFF3FAF46),
            "library" to Color(0xFF8D7B75),
            "cafeteria" to Color(0xFFB08968),
            "poll" to Color(0xFF2196F3),
            "important" to Color(0xFFCF2C3E),
            "score" to Color(0xFFE1B12C),
        )
    } else {
        mapOf(
            "attendance" to Color(0xFF89EC8D),
            "library" to Color(0xFFD7CCC8),
            "cafeteria" to Color(0xFFF5E6CC),
            "poll" to Color(0xFF74C7FF),
            "important" to Color(0xFFF53046),
            "score" to Color(0xFFF8C01F),
        )
    }
}

/**
 * The dynamic Header component that expands/collapses based on the [mode].
 */
@Composable
fun Header(mode: String, exitOnClick: () -> Unit = {} , name : String ) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Animation state for the header height
    val animatedHeight = remember { Animatable(screenHeight.value) }
    val scope = rememberCoroutineScope()

    // Controls whether to show the expanded profile view and exit button
    var showProfileAndExit by remember { mutableStateOf(mode == "openAfterFullClose") }

    LaunchedEffect(mode) {
        when (mode) {
            "firstTime" -> {
                delay(500)
                animatedHeight.animateTo(150f, spring(0.8f, 30f))
            }
            "close" -> {
                animatedHeight.animateTo(150f, spring(0.9f, 10f))
            }
            "fullClose" -> {
                showProfileAndExit = true
                animatedHeight.snapTo(150f)
                animatedHeight.animateTo(screenHeight.value, spring(0.9f, 10f))
            }
            "openAfterFullClose" -> {
                showProfileAndExit = true
                delay(1000)
                showProfileAndExit = false
                animatedHeight.animateTo(150f, spring(0.8f, 30f))
            }
            else -> animatedHeight.snapTo(150f)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp))
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
            .height(animatedHeight.value.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        // Title shown when header is small
        AnimatedVisibility(
            visible = !showProfileAndExit,
            exit = fadeOut(tween(300))
        ) {
            Text(
                text = "کراوات",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
            )
        }

        // Profile details and logout shown when header is full-screen
        AnimatedVisibility(
            visible = showProfileAndExit,
            exit = fadeOut(tween(1000))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ولی ${name}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 400.dp)
                )

                Spacer(Modifier.height(200.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.Red)
                        .clickable { exitOnClick() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.exit),
                            contentDescription = "exit",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp),
                        )
                        Text(
                            text = "خروج",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontSize = 30.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun mainPage(modifier: Modifier = Modifier, navController: NavController, mode: String) {
    val view = LocalView.current
    val colors = appColors()
    val viewModel = hiltViewModel<MainPageViewModel>()
    val context = LocalContext.current
    val activity = context as? Activity
    var name by remember { mutableStateOf("") }
    var headerMode by remember { mutableStateOf(mode) }
    var showDetails by remember { mutableStateOf(false) }
    val token  = viewModel.GetMainToken(context)
    // Request Notification Permissions for Android 13+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        SideEffect {
            activity?.let {
                ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }
    }

    // Handle system back press to return from full profile view
    BackHandler(enabled = headerMode == "fullClose") {
        headerMode = "openAfterFullClose"
    }

    LaunchedEffect(Unit) {
        if (mode != "firstTime") delay(200)
        showDetails = true
        name = viewModel.getName(context ,token!!)!!
    }
    // Define the color here, inside the Composable function
    val statusBarColor = MaterialTheme.colorScheme.onBackground.toArgb()

    // Update Status Bar Appearance
    SideEffect {
        activity?.window?.let { window ->
            window.statusBarColor = statusBarColor
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {

        // Main Grid of App Icons
        if (showDetails) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .size(400.dp, 450.dp)
                        .padding(30.dp)
                        .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(40.dp))
                ) {
                    // Row 1
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 15.dp)) {
                        AppIcon(colors["attendance"]!!, "حضور غیاب", painterResource(R.drawable.attendance), true, { navController.navigate("attendance") }, Modifier.weight(1f))
                        AppIcon(colors["library"]!!, "به زودی", painterResource(R.drawable.library), false, {}, Modifier.weight(1f))
                        AppIcon(colors["cafeteria"]!!, "به زودی", painterResource(R.drawable.cafeteria), false, {}, Modifier.weight(1f))
                    }
                    // Row 2
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)) {
                        AppIcon(colors["poll"]!!, "به زودی", painterResource(R.drawable.poll), false, {}, Modifier.weight(1f))
                        AppIcon(colors["important"]!!, "به زودی", painterResource(R.drawable.important), false, {}, Modifier.weight(1f))
                        AppIcon(colors["score"]!!, "به زودی", painterResource(R.drawable.score), false, {}, Modifier.weight(1f))
                    }
                }
                // Label for the grid
                Text(
                    text = "گزینه ها",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background).align(Alignment.TopCenter).padding(horizontal = 8.dp)
                )
            }
        }

        // Header and Profile Icon Logic
        when {
            headerMode == mode -> {
                Header(mode = mode  , name = name)
                Profile(onClick = { headerMode = "fullClose" }, isExpanded = false, isReversed = false)
            }
            headerMode == "openAfterFullClose" -> {
                Header(headerMode , name = name)
                Profile(onClick = { headerMode = "fullClose" }, isExpanded = false, isReversed = true)
            }
            else -> {
                Header(headerMode, exitOnClick = {
                    viewModel.ClearAllPreferences(context)
                    navController.navigate("login")
                } ,name = name)
                Profile(onClick = {}, isExpanded = true)
            }
        }
    }
}

/**
 * A square icon button used in the main grid. Blurs content if [isAvailable] is false.
 */
@Composable
fun AppIcon(color: Color, name: String, painter: Painter, isAvailable: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(5.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color)
                .clickable { onClick() }
        ) {
            Box(modifier = Modifier.fillMaxSize().blur(if (isAvailable) 0.dp else 10.dp)) {
                Icon(
                    painter = painter,
                    contentDescription = name,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(75.dp).align(Alignment.Center)
                )
            }
            if (!isAvailable) {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "locked",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        AutoSizeText(name)
    }
}

/**
 * Text that scales down if it overflows its width.
 */
@Composable
fun AutoSizeText(text: String, modifier: Modifier = Modifier) {
    var fontSize by remember { mutableStateOf(16.sp) }
    Text(
        text = text,
        maxLines = 1,
        softWrap = false,
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSize),
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier,
        onTextLayout = { result ->
            if (result.didOverflowWidth && fontSize > 8.sp) {
                fontSize = (fontSize.value - 1f).sp
            }
        }
    )
}

/**
 * Animated Profile Icon that moves and scales when the header expands.
 */
@Composable
private fun BoxScope.Profile(
    onClick: () -> Unit,
    isExpanded: Boolean = false,
    isReversed: Boolean = false
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val startPadding = 20.dp
    val centerX = (screenWidth / 2) - (300.dp / 2) - startPadding
    val animDuration = if (isReversed) 600 else 1000

    val size = remember { Animatable(if (isReversed) 300f else 60f) }
    val offsetX = remember { Animatable(if (isReversed) centerX.value else 0f) }
    val offsetY = remember { Animatable(if (isExpanded == isReversed) screenHeight.value else 0f) }

    LaunchedEffect(isExpanded) {
        launch {
            offsetX.animateTo(
                targetValue = if (isExpanded) centerX.value else 0f,
                animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
            )
        }
        launch {
            if (isExpanded == isReversed) {
                offsetY.animateTo(0f, tween(500, easing = FastOutSlowInEasing))
            }
        }
        launch {
            size.animateTo(
                targetValue = if (isExpanded) 300f else 60f,
                animationSpec = tween(animDuration, easing = FastOutSlowInEasing)
            )
        }
    }

    Icon(
        modifier = Modifier
            .offset(x = offsetX.value.dp, y = offsetY.value.dp)
            .padding(top = 80.dp, start = startPadding)
            .size(size.value.dp)
            .align(Alignment.TopStart)
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .clickable { onClick() },
        painter = painterResource(R.drawable.ghoust),
        contentDescription = "profile_icon",
        tint = Color.Black
    )
}