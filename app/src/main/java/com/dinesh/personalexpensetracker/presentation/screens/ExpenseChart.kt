package com.dinesh.personalexpensetracker.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dinesh.personalexpensetracker.data.model.Expense

@Composable
fun ExpenseChart(expenses: List<Expense>) {
    Card(Modifier.fillMaxWidth().height(200.dp)) {
        val grouped = expenses.groupBy { it.category }
        Column(Modifier.padding(16.dp)) {
            Text("Expenses by Category", fontWeight = FontWeight.Bold)
            grouped.forEach { (category, items) ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(category)
                    Text("₹${items.sumOf { it.amount }}")
                }
            }
        }
    }
}