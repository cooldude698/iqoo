package com.example.noticesorter.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream

class OcrEngine {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractText(context: Context, uri: Uri): String {
        return try {
            val mimeType = context.contentResolver.getType(uri) ?: ""
            val image = if (mimeType == "application/pdf" || uri.toString().endsWith(".pdf", true)) {
                getBitmapFromPdf(context, uri)?.let { InputImage.fromBitmap(it, 0) }
            } else {
                InputImage.fromFilePath(context, uri)
            }

            if (image == null) return ""

            val result = recognizer.process(image).await()
            result.text
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun getBitmapFromPdf(context: Context, uri: Uri): Bitmap? {
        return try {
            // Copy uri to a temp file because PdfRenderer requires a seekable file descriptor
            val tempFile = File(context.cacheDir, "temp_pdf.pdf")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            val fd = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(fd)
            if (renderer.pageCount > 0) {
                val page = renderer.openPage(0)
                // Double resolution for better OCR accuracy
                val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
                renderer.close()
                fd.close()
                tempFile.delete()
                bitmap
            } else {
                renderer.close()
                fd.close()
                tempFile.delete()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
