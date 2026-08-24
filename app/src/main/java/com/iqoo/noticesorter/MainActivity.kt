package com.iqoo.noticesorter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.content.IntentCompat
import com.iqoo.noticesorter.data.RealNoticeProcessor
import com.iqoo.noticesorter.ui.screens.NoticeSorterApp
import com.iqoo.noticesorter.ui.theme.NoticeSorterTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Safely extract shared file Uri from ACTION_SEND intent
        val sharedUriString = handleIncomingShareIntent(intent)

        val processor = RealNoticeProcessor()

        setContent {
            NoticeSorterTheme {
                NoticeSorterApp(
                    sharedImageUri = sharedUriString,
                    processor = processor
                )
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
