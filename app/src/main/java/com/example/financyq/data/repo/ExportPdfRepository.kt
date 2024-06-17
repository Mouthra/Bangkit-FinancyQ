package com.example.financyq.data.repo

import android.content.Context
import com.example.financyq.data.api.ApiService
import com.example.financyq.data.di.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.InputStream

//class ExportPdfRepository(private val apiService: ApiService) {
//
//    suspend fun exportPDF(idUser: String, context: File): Result<File> = withContext(Dispatchers.IO) {
//        try {
//            val response = apiService.exportPDF(idUser)
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                if (responseBody != null) {
//                    val pdfFile = savePDFLocally(context, responseBody.byteStream(), "exported_pdf.pdf")
//                    pdfFile?.let {
//                        Result.Success(it)
//                    } ?: Result.Error("Failed to save PDF locally")
//                } else {
//                    Result.Error("Response body is null")
//                }
//            } else {
//                val errorBody = response.errorBody()?.string()
//                val errorMessage = errorBody ?: "Unsuccessful response"
//                Result.Error(errorMessage)
//            }
//        } catch (e: HttpException) {
//            Result.Error("HTTP Exception: ${e.message}")
//        }
//    }
//
//    private fun savePDFLocally(context: Context, inputStream: InputStream, fileName: String): File? {
//        return try {
//            val filePath = context.externalCacheDir?.absolutePath ?: context.cacheDir.absolutePath
//            val file = inputStream.saveToFile(filePath, fileName)
//            file
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun InputStream.saveToFile(filePath: String, fileName: String): File = use { input ->
//        val file = File(filePath, fileName)
//        file.outputStream().use { output ->
//            input.copyTo(output)
//        }
//        file
//    }
//
//    companion object {
//        @Volatile
//        private var instance: ExportPdfRepository? = null
//
//        fun getInstance(apiService: ApiService): ExportPdfRepository =
//            instance ?: synchronized(this) {
//                instance ?: ExportPdfRepository(apiService).also { instance = it }
//            }
//    }
//}
