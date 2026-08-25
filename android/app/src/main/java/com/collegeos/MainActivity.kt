package com.collegeos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.IntentCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.collegeos.core.ui.CollegeOsSplash
import com.collegeos.core.ui.MainAppScreen
import com.collegeos.core.ui.theme.CollegeOsTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedUri = handleIncomingShareIntent(intent)

        setContent {
            CollegeOsTheme {
                var showSplash by remember { mutableStateOf(true) }

                if (showSplash) {
                    CollegeOsSplash(
                        onSplashFinished = { showSplash = false }
                    )
                } else {
                    MainAppScreen(initialSharedUri = sharedUri)
                }
            }
        }
    }

    private fun handleIncomingShareIntent(intent: Intent?): String? {
        if (intent == null) return null

        return try {
            val action = intent.action
            val type = intent.type

            if (Intent.ACTION_SEND == action && type != null) {
                if (type.startsWith("image/") || type == "application/pdf") {
                    val uri = IntentCompat.getParcelableExtra(intent, Intent.EXTRA_STREAM, Uri::class.java)
                    uri?.toString()
                } else null
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
