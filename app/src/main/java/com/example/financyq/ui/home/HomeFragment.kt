package com.example.financyq.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.financyq.R
import com.example.financyq.databinding.FragmentHomeBinding
import com.example.financyq.ui.details.DetailsExpenditureActivity
import com.example.financyq.ui.details.DetailsIncomeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetailsIncome.setOnClickListener {
            startActivity(Intent(requireContext(), DetailsIncomeActivity::class.java))
        }
        binding.btnDetailsExpenditure.setOnClickListener {
            startActivity(Intent(requireContext(), DetailsExpenditureActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}