package com.example.financyq.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financyq.R
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.data.local.UserPreferences
import com.example.financyq.data.response.TotalResponse
import com.example.financyq.databinding.FragmentHomeBinding
import com.example.financyq.ui.adapter.ShortcutAdapter
import com.example.financyq.ui.analizeq.TotalExpenditureViewModel
import com.example.financyq.ui.analizeq.TotalIncomeViewModel
import com.example.financyq.ui.details.DetailsExpenditureActivity
import com.example.financyq.ui.details.DetailsIncomeActivity
import com.example.financyq.ui.edufinance.EduFinanceViewModel
import com.example.financyq.ui.profile.UsernameViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val eduFinanceViewModel: EduFinanceViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val totalIncomeViewModel: TotalIncomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val totalExpenditureViewModel: TotalExpenditureViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val usernameViewModel: UsernameViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var shortcutAdapter: ShortcutAdapter
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreferences = UserPreferences.getInstance(requireContext())
        setupRecyclerView()
        setObserver()
        setupAction()
        observeTotalExpenditure()
        observeTotalIncome()
        loadUser()
    }

    private fun loadUser() {
        val username = runBlocking { userPreferences.userNameFlow.first() }
        if (username != null) {
            usernameViewModel.getUsername(username).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        binding.tvUsername.text = result.data.username
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), R.string.id_user_not_found, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        shortcutAdapter = ShortcutAdapter()
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvScEducation.layoutManager = layoutManager
        binding.rvScEducation.setHasFixedSize(true)
        binding.rvScEducation.adapter = shortcutAdapter
    }

    private fun setObserver() {
        eduFinanceViewModel.getEducationFinance().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    shortcutAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupAction(){
        binding.apply {
            btnDetailsIncome.setOnClickListener {
                val intent = Intent(requireContext(), DetailsIncomeActivity::class.java)
                startActivity(intent)
            }
            btnDetailsExpenditure.setOnClickListener {
                val intent = Intent(requireContext(), DetailsExpenditureActivity::class.java)
                startActivity(intent)
            }
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
