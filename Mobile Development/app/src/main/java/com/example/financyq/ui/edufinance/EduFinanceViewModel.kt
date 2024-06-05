package com.example.financyq.ui.edufinance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EduFinanceViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is EduFinance Fragment"
    }
    val text: LiveData<String> = _text
}