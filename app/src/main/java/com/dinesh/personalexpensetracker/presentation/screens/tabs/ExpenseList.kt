package com.dinesh.personalexpensetracker.presentation.screens.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dinesh.personalexpensetracker.data.model.Expense

@Composable
fun ExpenseList(expenses: List<Expense>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(expenses) { expense ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "₹ ${expense.amount}", style = MaterialTheme.typography.titleMedium)
                    Text(text = expense.category)
                    Text(text = expense.description)
                    Text(text = expense.date.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
