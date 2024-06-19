package com.example.financyq.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.request.UpdateIncomeRequest
import com.example.financyq.data.response.TransactionsItem
import com.example.financyq.databinding.ActivityDetailsIncomeBinding
import com.example.financyq.ui.adapter.DetailsIncomeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DetailsIncomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsIncomeBinding

    private val viewModel: DetailsIncomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val updateViewModel: UpdateIncomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private val deleteViewModel: DeleteIncomeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userPreferences: UserPreferences
    private lateinit var adapter: DetailsIncomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsIncomeBinding.inflate(layoutInflater)
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
        adapter = DetailsIncomeAdapter { item ->
            showBottomSheetDialog(item)
        }
        binding.rvDetailIncome.layoutManager = LinearLayoutManager(this)
        binding.rvDetailIncome.adapter = adapter
    }

    private fun initUserPreferences() {
        userPreferences = UserPreferences.getInstance(this)
    }

    private fun observeViewModel() {
        val idUser = runBlocking { userPreferences.userIdFlow.first() }
        if (idUser != null) {
            viewModel.getDetailIncome(idUser).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
//                        Log.d("DetailsIncomeActivity", "Loading data...")
                    }
                    is Result.Success -> {
                        result.data.transactions?.let {
                            val sortedTransactions =
                                it.sortedByDescending { transaction -> transaction?.tanggal }
                            adapter.submitList(sortedTransactions)
//                            Log.d("DetailsIncomeActivity", "Data loaded successfully")
                        }
                    }
                    is Result.Error -> {
//                        Log.e("DetailsIncomeActivity", "Error loading data: ${result.error}")
                    }
                }
            }
        } else {
//            Log.e("DetailsIncomeActivity", "User ID is not available")
        }
    }

    private fun showBottomSheetDialog(item: TransactionsItem) {
        val dialog = BottomSheetDialog(this)
        val bindingSheet = com.example.financyq.databinding.BottomSheetDetailIncomeBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(bindingSheet.root)

        bindingSheet.EditCategory.setText(item.kategori)
        bindingSheet.EditNameIncome.setText(item.deskripsi)
        bindingSheet.EditTotalIncome.setText(item.jumlah.toString())
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
                        deleteViewModel.deleteIncome(idTransaksi).observe(this@DetailsIncomeActivity) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    // Tampilkan indikator loading jika diperlukan
                                }
                                is Result.Success -> {
                                    dialog.dismiss()
                                    observeViewModel()
//                                    Log.d("DetailsIncomeActivity", result.data.message ?: "Transaction removed")
                                }
                                is Result.Error -> {
//                                    Log.e("DetailsIncomeActivity", "Error deleting income: ${result.error}")
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
                setTitle("Confirmation")
                setMessage("Are you sure about this change?")
                setPositiveButton("Yes") { _, _ ->
                    val updateIncomeRequest = UpdateIncomeRequest(
                        jumlah = bindingSheet.EditTotalIncome.text.toString().replace("[Rp.]".toRegex(), "").toInt(),
                        sumber = bindingSheet.EditSource.text.toString(),
                        kategori = bindingSheet.EditCategory.text.toString(),
                        deskripsi = bindingSheet.EditNameIncome.text.toString()
                    )

                    item.idTransaksi?.let { it1 ->
                        updateViewModel.updateIncome(it1, updateIncomeRequest).observe(this@DetailsIncomeActivity) { result ->
                            when (result) {
                                is Result.Loading -> {

                                }
                                is Result.Success -> {

                                    dialog.dismiss()
                                    observeViewModel()
                                }
                                is Result.Error -> {
//                                    Log.e("DetailsIncomeActivity", "Error updating income: ${result.error}")
                                }
                            }
                        }
                    }
                }
                setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                setCancelable(false)
                show()
            }
        }
        dialog.show()
    }
}
