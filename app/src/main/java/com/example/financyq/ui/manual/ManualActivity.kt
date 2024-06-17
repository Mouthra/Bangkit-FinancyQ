package com.example.financyq.ui.manual

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.financyq.R
import com.example.financyq.databinding.ActivityManualBinding

class ManualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManualBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
//        setupView()
        binding.btnBack.setOnClickListener{
            finish()
        }
    }

//    private fun setupView() {
//
//        switchFragment(IncomeFragment())
//        setButtonState(binding.btnIncome, true)
//        setButtonState(binding.btnExpenditure, false)
//
//        binding.btnIncome.setOnClickListener {
//            switchFragment(IncomeFragment())
//            setButtonState(binding.btnIncome, true)
//            setButtonState(binding.btnExpenditure, false)
//        }
//
//        binding.btnExpenditure.setOnClickListener {
//            switchFragment(ExpenditureFragment())
//            setButtonState(binding.btnExpenditure, true)
//            setButtonState(binding.btnIncome, false)
//        }
//    }
//
//    private fun setButtonState(button: Button, isSelected: Boolean) {
//        if (isSelected) {
//            button.setTextColor(ContextCompat.getColor(this, R.color.white))
//            button.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
//        } else {
//            button.setTextColor(ContextCompat.getColor(this, R.color.orange))
//            button.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
//        }
//    }
//
//    private fun switchFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .commit()
//    }

    private fun setupViews() {
        binding.apply {
            // Setup your views here using binding
            btnIncome.isSelected = true
            replaceFragment(IncomeFragment())

            btnIncome.setOnClickListener {
                btnIncome.isSelected = true
                btnExpenditure.isSelected = false
                replaceFragment(IncomeFragment())
            }

            btnExpenditure.setOnClickListener {
                btnExpenditure.isSelected = true
                btnIncome.isSelected = false
                replaceFragment(ExpenditureFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}