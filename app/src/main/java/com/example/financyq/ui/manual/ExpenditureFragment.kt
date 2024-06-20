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
import com.example.financyq.data.request.AddExpenditureRequest
import com.example.financyq.databinding.FragmentExpenditureBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ExpenditureFragment : Fragment() {

    private var _binding: FragmentExpenditureBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenditureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences.getInstance(requireContext())
        binding.SourceEditText.setText(R.string.manual_text)

        binding.saveExpenditure.setOnClickListener {
            val category = binding.CategoryEditText.text.toString()
            val name = binding.NameExpenditureEditText.text.toString()
            val amountString = binding.TotalExpenditureEditText.text.toString().replace("[Rp.]".toRegex(), "")
            val amount = amountString.toIntOrNull()
            val source = binding.SourceEditText.text.toString()

            if (category.isNotEmpty() && name.isNotEmpty() && amount != null) {
                val idUser = runBlocking { userPreferences.userIdFlow.first() }

                if (idUser != null ) {
                    val addExpenditureRequest = AddExpenditureRequest(
                        idUser = idUser,
                        sumber = source,
                        jumlah = amount,
                        kategori = category,
                        deskripsi = name
                    )

                    viewModel.addExpenditure(addExpenditureRequest).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                            }
                            is Result.Success -> {
                                showSuccessDialogAndNavigate()
                            }
                            is Result.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    R.string.failed_to_add_expenditure,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.id_user_not_found,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), R.string.please_fill_in_all_columns, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccessDialogAndNavigate() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(R.string.input_data_expenditure)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                clearEditTextFields(
                    binding.CategoryEditText,
                    binding.NameExpenditureEditText,
                    binding.TotalExpenditureEditText,
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
