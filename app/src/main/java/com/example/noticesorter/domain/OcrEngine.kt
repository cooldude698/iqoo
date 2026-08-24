package com.example.noticesorter.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

class OcrEngine {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun extractText(context: Context, uri: Uri): String {
        return try {
            val mimeType = context.contentResolver.getType(uri) ?: ""
            val image = if (mimeType == "application/pdf" || uri.toString().endsWith(".pdf", true)) {
                getBitmapFromPdf(context, uri)?.let { InputImage.fromBitmap(it, 0) }
            } else {
                getBitmapFromImage(context, uri)?.let { InputImage.fromBitmap(it, 0) }
            }

            if (image == null) {
                Log.d("NoticeSorter-OCR", "Failed to load image or PDF.")
                return ""
            }

            val result = recognizer.process(image).await()
            val text = result.text
            Log.d("NoticeSorter-OCR", "Extracted text (${text.length} chars): ${text.take(200).replace("\n", " ")}...")
            text
        } catch (e: Exception) {
            Log.e("NoticeSorter-OCR", "Error extracting text", e)
            ""
        }
    }

    private fun getBitmapFromImage(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (bitmap == null) return null

            // Downscale if wider than 1500px
            val maxDimension = 1500
            if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
                val scale = maxDimension.toFloat() / max(bitmap.width, bitmap.height)
                val newWidth = (bitmap.width * scale).toInt()
                val newHeight = (bitmap.height * scale).toInt()
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
                if (scaledBitmap != bitmap) {
                    bitmap.recycle()
                    bitmap = scaledBitmap
                }
            }

            // EXIF rotation
            context.contentResolver.openInputStream(uri)?.use { exifInputStream ->
                val exif = ExifInterface(exifInputStream)
                val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                val matrix = Matrix()
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                    ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1.0f, 1.0f)
                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                        matrix.preScale(1.0f, -1.0f)
                        matrix.postRotate(180f)
                    }
                    ExifInterface.ORIENTATION_TRANSPOSE -> {
                        matrix.preScale(-1.0f, 1.0f)
                        matrix.postRotate(90f)
                    }
                    ExifInterface.ORIENTATION_TRANSVERSE -> {
                        matrix.preScale(-1.0f, 1.0f)
                        matrix.postRotate(270f)
                    }
                }
                
                if (!matrix.isIdentity) {
                    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    if (rotatedBitmap != bitmap) {
                        bitmap.recycle()
                        bitmap = rotatedBitmap
                    }
                }
            }

            bitmap
        } catch (e: Exception) {
            Log.e("NoticeSorter-OCR", "Error processing image", e)
            null
        }
    }

    private fun getBitmapFromPdf(context: Context, uri: Uri): Bitmap? {
        return try {
            val tempFile = File(context.cacheDir, "temp_pdf.pdf")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            val fd = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = try {
                PdfRenderer(fd)
            } catch (e: SecurityException) {
                Log.w("NoticeSorter-OCR", "Password protected PDF cannot be rendered: ${e.message}")
                fd.close()
                tempFile.delete()
                return null
            } catch (e: Exception) {
                Log.e("NoticeSorter-OCR", "Failed to initialize PdfRenderer", e)
                fd.close()
                tempFile.delete()
                return null
            }
            
            if (renderer.pageCount > 0) {
                val page = renderer.openPage(0)
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
            Log.e("NoticeSorter-OCR", "Error rendering PDF", e)
            null
        }
    }
}
