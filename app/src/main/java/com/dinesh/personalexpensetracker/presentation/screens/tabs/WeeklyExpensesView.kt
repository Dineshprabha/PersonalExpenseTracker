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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeeklyExpensesView(viewModel: ExpenseViewModel) {
    val calendar = Calendar.getInstance()
    val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
    val currentYear = calendar.get(Calendar.YEAR)

    var selectedWeek by remember { mutableStateOf(currentWeek) }

    val expenses by viewModel.expenses.collectAsState()

    val weekList = remember {
        (1..currentWeek).map { week ->
            val weekCalendar = Calendar.getInstance().apply {
                set(Calendar.WEEK_OF_YEAR, week)
                set(Calendar.YEAR, currentYear)
                set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            }

            val startDate = weekCalendar.time
            weekCalendar.add(Calendar.DAY_OF_WEEK, 6)
            val endDate = weekCalendar.time

            val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
            week to "${formatter.format(startDate)} - ${formatter.format(endDate)}"
        }
    }

    // Formatter and range for the selected week
    val formatter = remember { java.text.SimpleDateFormat("MMM dd", Locale.getDefault()) }
    val weekRange = remember(selectedWeek) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.WEEK_OF_YEAR, selectedWeek)
            set(Calendar.YEAR, currentYear)
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }
        val startDate = cal.time
        cal.add(Calendar.DAY_OF_WEEK, 6)
        val endDate = cal.time
        "${formatter.format(startDate)} - ${formatter.format(endDate)}"
    }

    // LazyRow to display week ranges
    LazyRow(modifier = Modifier.padding(8.dp)) {
        items(weekList.size) { index ->
            val (week, label) = weekList[index]
            val isSelected = week == selectedWeek

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { selectedWeek = week }
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = label,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }

    // Filter and total expenses for selected week
    val filtered = expenses.filter {
        val cal = Calendar.getInstance().apply { time = it.date }
        cal.get(Calendar.WEEK_OF_YEAR) == selectedWeek && cal.get(Calendar.YEAR) == currentYear
    }

    val totalExpense = filtered.sumOf { it.amount }

    // Weekly Total Card
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Expense ($weekRange)",
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
