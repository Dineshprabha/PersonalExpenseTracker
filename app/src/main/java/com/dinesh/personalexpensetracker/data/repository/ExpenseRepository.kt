package com.dinesh.personalexpensetracker.data.repository

import com.dinesh.personalexpensetracker.data.local.ExpenseDao
import com.dinesh.personalexpensetracker.data.model.Expense
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao
) {

    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()
    suspend fun insertExpense(expense: Expense) = dao.insertExpense(expense)
}