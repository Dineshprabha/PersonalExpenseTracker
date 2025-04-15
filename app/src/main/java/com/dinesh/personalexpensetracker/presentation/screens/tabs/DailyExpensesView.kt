package com.dinesh.personalexpensetracker.presentation.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dinesh.personalexpensetracker.presentation.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyExpensesView(viewModel: ExpenseViewModel) {
    val today = remember { Calendar.getInstance() }
    val currentMonth = today.get(Calendar.MONTH)

    val formatter = remember { SimpleDateFormat("EEE d", Locale.getDefault()) }

    val daysInMonth = remember {
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val days = mutableListOf<Date>()
        while (cal.get(Calendar.MONTH) == currentMonth) {
            days.add(cal.time)
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        days
    }

    var selectedDate by remember { mutableStateOf(today.time) }

    val expenses by viewModel.expenses.collectAsState()

    val filtered = expenses.filter {
        isSameDay(it.date, selectedDate)
    }

    val totalExpense = filtered.sumOf { it.amount }

    // LazyRow for days
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(daysInMonth) { date ->
            val isSelected = isSameDay(date, selectedDate)

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { selectedDate = date }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = formatter.format(date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    // Total Expense Card
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Total Expense",
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

// Helper to check if two dates are the same day
fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
}
