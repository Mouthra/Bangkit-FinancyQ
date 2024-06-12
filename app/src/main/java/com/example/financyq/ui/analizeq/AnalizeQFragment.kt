package com.example.financyq.ui.analizeq

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.financyq.databinding.FragmentAnalizeQBinding
import com.example.financyq.ui.details.DetailsExpenditureActivity
import com.example.financyq.ui.details.DetailsIncomeActivity
import com.example.financyq.ui.manual.ManualActivity
import com.example.financyq.ui.photo.PhotoActivity

class AnalizeQFragment : Fragment() {

    private var _binding: FragmentAnalizeQBinding? = null
    private val binding get() = _binding!!

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

        setupAction()
    }

    private fun setupAction() {
        binding.btnFoto.setOnClickListener {
            startActivity(Intent(requireContext(), PhotoActivity::class.java))
        }
        binding.btnManual.setOnClickListener {
            startActivity(Intent(requireContext(), ManualActivity::class.java))
        }
        binding.tvDetailsIncome.setOnClickListener{
            startActivity(Intent(requireContext(), DetailsIncomeActivity::class.java))
        }
        binding.tvDetailsExpenditure.setOnClickListener{
            startActivity(Intent(requireContext(), DetailsExpenditureActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}