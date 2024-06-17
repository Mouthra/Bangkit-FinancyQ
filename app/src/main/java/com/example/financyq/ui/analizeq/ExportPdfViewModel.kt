package com.example.financyq.ui.analizeq

import android.content.Context
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.financyq.data.di.Result
//import com.example.financyq.data.repo.ExportPdfRepository
import kotlinx.coroutines.Dispatchers
import java.io.File

//class ExportPdfViewModel(private val exportPdfRepository: ExportPdfRepository) : ViewModel() {
//
//    fun exportPDF(idUser: String): LiveData<Result<File>> {
//        return liveData(Dispatchers.IO) {
//            emit(Result.Loading)
//
//            val directory = getExternalStorageDirectory()
//            if (directory == null) {
//                emit(Result.Error("Failed to access external storage directory"))
//                return@liveData
//            }
//
////            val outputFile = File(directory, "exported_pdf.pdf")
////            val result = exportPdfRepository.exportPDF(idUser, outputFile)
//
////            emit(result)
//        }
//    }
//
//    private fun getExternalStorageDirectory(): File? {
//        val state = Environment.getExternalStorageState()
//        if (Environment.MEDIA_MOUNTED != state) {
//            return null
//        }
//
//        val directory = File(Environment.getExternalStorageDirectory(), "FinancyQ")
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
//
//        return directory
//    }
//}
