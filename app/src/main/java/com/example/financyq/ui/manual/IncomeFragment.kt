package com.example.financyq.ui.manual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.financyq.R
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.request.AddIncomeRequest
import com.example.financyq.databinding.FragmentIncomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class IncomeFragment : Fragment() {

    private var _binding: FragmentIncomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddIncomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences.getInstance(requireContext())
        binding.SourceEditText.setText(R.string.manual_text)
        binding.saveIncome.setOnClickListener {
            val category = binding.CategoryEditText.text.toString()
            val name = binding.NameIncomeEditText.text.toString()
            val amountString = binding.TotalIncomeEditText.text.toString().replace("[Rp.]".toRegex(), "")
            val amount = amountString.toIntOrNull()
            val source = binding.SourceEditText.text.toString()

            if (category.isNotEmpty() && name.isNotEmpty() && amount != null) {
                val idUser = runBlocking { userPreferences.userIdFlow.first() }

                if (idUser != null ) {
                    val addIncomeRequest = AddIncomeRequest(
                        idUser = idUser,
                        sumber = source,
                        jumlah = amount,
                        kategori = category,
                        deskripsi = name
                    )

                    viewModel.addIncome(addIncomeRequest).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                            }
                            is Result.Success -> {
                                showSuccessDialogAndNavigate()
                            }
                            is Result.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal menambah pemasukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "ID Pengguna tidak tersedia",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessDialogAndNavigate() {
        AlertDialog.Builder(requireContext())
            .setTitle("Selamat")
            .setMessage("Data pemasukan berhasil disimpan")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                clearEditTextFields(
                    binding.CategoryEditText,
                    binding.NameIncomeEditText,
                    binding.TotalIncomeEditText,
                    binding.SourceEditText
                )
                activity?.onBackPressed()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
