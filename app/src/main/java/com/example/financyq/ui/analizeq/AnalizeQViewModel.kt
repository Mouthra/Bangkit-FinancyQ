package com.example.financyq.ui.analizeq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalizeQViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is AnalizeQ Fragment"
    }
    val text: LiveData<String> = _text
}