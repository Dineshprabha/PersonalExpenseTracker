package com.dinesh.personalexpensetracker.presentation.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dinesh.personalexpensetracker.presentation.viewmodel.ExpenseViewModel
import java.util.*

@Composable
fun MonthlyExpenseView(viewModel: ExpenseViewModel) {
    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    var selectedMonth by remember { mutableStateOf(currentMonth) }

    val expenses by viewModel.expenses.collectAsState()

    // Month selection row
    LazyRow(modifier = Modifier.padding(8.dp)) {
        items(months.size) { index ->
            val isSelected = index == selectedMonth

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { selectedMonth = index }
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = months[index],
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }

    // Filter expenses for selected month and year
    val filtered = expenses.filter {
        val cal = Calendar.getInstance().apply { time = it.date }
        cal.get(Calendar.MONTH) == selectedMonth && cal.get(Calendar.YEAR) == currentYear
    }

    val totalExpense = filtered.sumOf { it.amount }

    // Monthly Total Card
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Expense (${months[selectedMonth]})",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹%.2f".format(totalExpense),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    ExpenseList(filtered)
}
