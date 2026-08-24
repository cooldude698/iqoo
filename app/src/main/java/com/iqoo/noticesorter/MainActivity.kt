package com.iqoo.noticesorter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.iqoo.noticesorter.data.MockNoticeProcessor
import com.iqoo.noticesorter.ui.screens.NoticeSorterApp
import com.iqoo.noticesorter.ui.theme.NoticeSorterTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Extract shared file Uri from ACTION_SEND intent (WhatsApp/Gallery/Files share)
        val sharedUriString = handleIncomingShareIntent(intent)

        val processor = MockNoticeProcessor()

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

        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if (type.startsWith("image/") || type == "application/pdf") {
                val imageUri = intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri
                return imageUri?.toString()
            }
        }
        return null
    }
}
