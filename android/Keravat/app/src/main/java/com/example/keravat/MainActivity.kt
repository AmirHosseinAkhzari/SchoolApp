package com.example.keravat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.keravat.ui.theme.KeravatTheme
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = com.google.firebase.Firebase.analytics
        super.onCreate(savedInstanceState)
        analytics.setAnalyticsCollectionEnabled(true)
        analytics.setUserProperty("debug_mode", "true")
        analytics.logEvent("page_view") {
            param("page_name", "MainActivity")
        }
        Log.d("ana" , "MainActivity")

        enableEdgeToEdge()
        setContent {

            KeravatTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KeravatApp(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }


}

