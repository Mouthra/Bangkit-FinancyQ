package com.example.financyq.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.request.UpdateExpenditureRequest
import com.example.financyq.data.response.TransactionsItem
import com.example.financyq.databinding.ActivityDetailsExpenditureBinding
import com.example.financyq.ui.adapter.DetailsExpenditureAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DetailsExpenditureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsExpenditureBinding

    private val viewModel: DetailsExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val updateViewModel: UpdateExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val deleteViewModel: DeleteExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userPreferences: UserPreferences
    private lateinit var adapter: DetailsExpenditureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsExpenditureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initUserPreferences()
        observeViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        adapter = DetailsExpenditureAdapter { item ->
            showBottomSheetDialog(item)
        }
        binding.rvDetailExpenditure.layoutManager = LinearLayoutManager(this)
        binding.rvDetailExpenditure.adapter = adapter
    }

    private fun initUserPreferences() {
        userPreferences = UserPreferences.getInstance(this)
    }

    private fun observeViewModel() {
        val idUser = runBlocking { userPreferences.userIdFlow.first() }
        if (idUser != null) {
            viewModel.getDetailExpenditure(idUser).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        Log.d("DetailsExpenditureActivity", "Loading data...")
                    }
                    is Result.Success -> {
                        result.data.transactions?.let {
                            val sortedTransactions =
                                it.sortedByDescending { transaction -> transaction?.tanggal }
                            adapter.submitList(sortedTransactions)
                            Log.d("DetailsExpenditureActivity", "Data loaded successfully")
                        }
                    }
                    is Result.Error -> {
                        Log.e("DetailsExpenditureActivity", "Error loading data: ${result.error}")
                    }
                }
            }
        } else {
            Log.e("DetailsExpenditureActivity", "User ID is not available")
        }
    }

    private fun showBottomSheetDialog(item: TransactionsItem) {
        val dialog = BottomSheetDialog(this)
        val bindingSheet = com.example.financyq.databinding.BottomSheetDetailExpenditureBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(bindingSheet.root)

        bindingSheet.EditCategory.setText(item.kategori)
        bindingSheet.EditNameExpenditure.setText(item.deskripsi)
        bindingSheet.EditTotalExpenditure.setText(item.jumlah.toString())
        bindingSheet.EditSource.setText(item.sumber)

        bindingSheet.btnClose.setOnClickListener {
            dialog.dismiss()
        }

        bindingSheet.btnDelete.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi Hapus")
                setMessage("Apakah Anda yakin ingin menghapusnya?")
                setPositiveButton("Ya") { _, _ ->
                    item.idTransaksi?.let { idTransaksi ->
                        deleteViewModel.deleteExpenditure(idTransaksi).observe(this@DetailsExpenditureActivity) { result ->
                            when (result) {
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    dialog.dismiss()
                                    observeViewModel()
                                    Log.d("DetailsExpenditureActivity", result.data.message ?: "Transaction removed")
                                }
                                is Result.Error -> {
                                    Log.e("DetailsExpenditureActivity", "Error deleting expenditure: ${result.error}")
                                }
                            }
                        }
                    }
                }
                setNegativeButton("Tidak") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                setCancelable(false)
                show()
            }
        }

        bindingSheet.btnSave.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Konfirmasi Simpan")
                setMessage("Apakah Anda yakin dengan perubahan ini?")
                setPositiveButton("Ya") { _, _ ->
                    val updateExpenditureRequest = UpdateExpenditureRequest(
                        jumlah = bindingSheet.EditTotalExpenditure.text.toString().replace("[Rp.]".toRegex(), "").toInt(),
                        sumber = bindingSheet.EditSource.text.toString(),
                        kategori = bindingSheet.EditCategory.text.toString(),
                        deskripsi = bindingSheet.EditNameExpenditure.text.toString()
                    )

                    item.idTransaksi?.let { it1 ->
                        updateViewModel.updateExpenditure(it1, updateExpenditureRequest).observe(this@DetailsExpenditureActivity) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    // Tampilkan indikator loading
                                }
                                is Result.Success -> {
                                    // Tangani sukses
                                    dialog.dismiss()
                                    observeViewModel()
                                }
                                is Result.Error -> {
                                    // Tangani error
                                    Log.e("DetailsExpenditureActivity", "Error updating expenditure: ${result.error}")
                                }
                            }
                        }
                    }
                }
                setNegativeButton("Tidak") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                setCancelable(false)
                show()
            }
        }

        dialog.show()
    }
}
