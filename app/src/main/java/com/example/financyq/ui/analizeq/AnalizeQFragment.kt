package com.example.financyq.ui.analizeq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.financyq.databinding.FragmentAnalizeQBinding

class AnalizeQFragment : Fragment() {

    private var _binding: FragmentAnalizeQBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val analizeqViewModel =
            ViewModelProvider(this)[AnalizeQViewModel::class.java]

        _binding = FragmentAnalizeQBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textAnalizeQ
        analizeqViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}