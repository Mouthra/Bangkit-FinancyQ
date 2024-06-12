package com.example.financyq.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financyq.data.di.Result
import com.example.financyq.data.di.ViewModelFactory
import com.example.financyq.databinding.FragmentHomeBinding
import com.example.financyq.ui.adapter.ShortcutAdapter
import com.example.financyq.ui.edufinance.EduFinanceViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val eduFinanceViewModel: EduFinanceViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private lateinit var shortcutAdapter: ShortcutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setObserver()
    }

    private fun setupRecyclerView() {
        shortcutAdapter = ShortcutAdapter()
        binding.rvScEducation.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = shortcutAdapter
        }
    }

    private fun setObserver() {
        eduFinanceViewModel.getEducationFinance().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    shortcutAdapter.submitList(result.data)
                    Log.e("fikry", "setObserver: ${result.data}", )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
