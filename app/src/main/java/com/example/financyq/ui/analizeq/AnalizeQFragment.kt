package com.example.financyq.ui.analizeq

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.response.TotalResponse
import com.example.financyq.databinding.FragmentAnalizeQBinding
import com.example.financyq.ui.details.DetailsExpenditureActivity
import com.example.financyq.ui.details.DetailsIncomeActivity
import com.example.financyq.ui.manual.ManualActivity
import com.example.financyq.ui.photo.PhotoActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale

class AnalizeQFragment : Fragment() {

    private var _binding: FragmentAnalizeQBinding? = null
    private val binding get() = _binding!!

    private val totalIncomeViewModel: TotalIncomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val totalExpenditureViewModel: TotalExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val exportPdfViewModel: ExportPdfViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalizeQBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences.getInstance(requireContext())

        observeTotalIncome()
        observeTotalExpenditure()
        setupAction()
    }

    private fun observeTotalIncome() {
        val userId = runBlocking { userPreferences.userIdFlow.first() }

        userId?.let {
            totalIncomeViewModel.getTotalIncome(it).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val totalIncomeResponse = result.data
                        displayTotalIncome(totalIncomeResponse)
                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }

    private fun observeTotalExpenditure() {
        val userId = runBlocking { userPreferences.userIdFlow.first() }

        userId?.let {
            totalExpenditureViewModel.getTotalExpenditure(it).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val totalExpenditureResponse = result.data
                        displayTotalExpenditure(totalExpenditureResponse)
                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }

    private fun displayTotalIncome(totalIncomeResponse: TotalResponse) {
        val totalIncome = totalIncomeResponse.data?.total ?: 0
        val formattedIncome = formatToRupiah(totalIncome)
        binding.tvValueIncome.text = formattedIncome
    }

    private fun displayTotalExpenditure(totalExpenditureResponse: TotalResponse) {
        val totalExpenditure = totalExpenditureResponse.data?.total ?: 0
        val formattedExpenditure = formatToRupiah(totalExpenditure)
        binding.tvValueExpenditure.text = formattedExpenditure
    }

    private fun formatToRupiah(value: Int): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(value)
    }

    private fun setupAction() {
        binding.btnFoto.setOnClickListener {
            startActivity(Intent(requireContext(), PhotoActivity::class.java))
        }
        binding.btnManual.setOnClickListener {
            startActivity(Intent(requireContext(), ManualActivity::class.java))
        }
        binding.tvDetailsIncome.setOnClickListener {
            startActivity(Intent(requireContext(), DetailsIncomeActivity::class.java))
        }
        binding.tvDetailsExpenditure.setOnClickListener {
            startActivity(Intent(requireContext(), DetailsExpenditureActivity::class.java))
        }
        binding.btnExportPdf.setOnClickListener {
            exportPdf()
        }
    }

    private fun exportPdf() {
        val userId = runBlocking { userPreferences.userIdFlow.first() }
        userId?.let {
            exportPdfViewModel.exportPdf(it).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        val responseBody = result.data
                        responseBody.let {
                            val filePath = savePDFToFile(responseBody)
                            filePath?.let { path ->
                                openPDF(path)
                            }
                        }
                    }
                    is Result.Error -> {

                    }
                }
            }
        }
    }

    private fun savePDFToFile(body: ResponseBody): String? {
        return try {
            val filePath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/exported_pdf.pdf"
            val file = File(filePath)
            val outputStream = FileOutputStream(file)
            outputStream.use {
                it.write(body.bytes())
            }
            filePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun openPDF(filePath: String) {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
