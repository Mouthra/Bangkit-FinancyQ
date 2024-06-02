package com.example.financyq.ui.edufinance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.financyq.databinding.FragmentEduFinanceBinding

class EduFinanceFragment : Fragment() {

    private var _binding: FragmentEduFinanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val eduFinanceViewModel =
            ViewModelProvider(this).get(EduFinanceViewModel::class.java)

        _binding = FragmentEduFinanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textEduFinance
        eduFinanceViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}