package com.dinesh.personalexpensetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinesh.personalexpensetracker.data.model.Expense
import com.dinesh.personalexpensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses = _expenses.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allExpenses.collect { _expenses.value = it }
        }
    }

    fun addExpense(expense: Expense) = viewModelScope.launch {
        repository.insertExpense(expense)
    }
}