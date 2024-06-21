package com.example.financyq.ui.photo

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.financyq.R
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.request.AddExpenditureRequest
import com.example.financyq.databinding.ActivityResultsBinding
import com.example.financyq.ui.manual.AddExpenditureViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private val addExpenditureViewModel: AddExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences.getInstance(this)

        val item = intent.getStringExtra("item")
        val totalPrice = intent.getStringExtra("totalPrice")
        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(imageUriString)

        binding.apply {
            EditNameExpenditure.setText(item)
            EditTotalExpenditure.setText(totalPrice)
            previewImageView.setImageURI(imageUri)
            EditSource.setText(R.string.foto_text)
            btnBack.setOnClickListener { finish() }
            btnCancel.setOnClickListener { finish() }
        }

        binding.btnSave.setOnClickListener {
            val category = binding.EditCategory.text.toString()
            val name = binding.EditNameExpenditure.text.toString()
            val amountString = binding.EditTotalExpenditure.text.toString().replace("[Rp.]".toRegex(), "")
            val amount = amountString.toIntOrNull()
            val source = binding.EditSource.text.toString()

            if (category.isNotEmpty() && name.isNotEmpty() && amount != null) {
                val idUser = runBlocking { userPreferences.userIdFlow.first() }

                if (idUser != null) {
                    val addExpenditureRequest = AddExpenditureRequest(
                        idUser = idUser,
                        sumber = source,
                        jumlah = amount,
                        kategori = category,
                        deskripsi = name
                    )

                    addExpenditureViewModel.addExpenditure(addExpenditureRequest).observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {

                            }
                            is Result.Success -> {
                                showSuccessDialogAndNavigate()
                            }
                            is Result.Error -> {
                                Toast.makeText(
                                    this,
                                    R.string.failed_to_add_expenditure,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        R.string.id_user_not_found,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, R.string.please_fill_in_all_columns, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessDialogAndNavigate() {
        AlertDialog.Builder(this)
            .setTitle(R.string.congratulations)
            .setMessage(R.string.input_data_expenditure)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                clearEditTextFields(
                    binding.EditNameExpenditure,
                    binding.EditTotalExpenditure
                )
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun clearEditTextFields(vararg editTexts: EditText) {
        for (editText in editTexts) {
            editText.text.clear()
        }
    }
}
