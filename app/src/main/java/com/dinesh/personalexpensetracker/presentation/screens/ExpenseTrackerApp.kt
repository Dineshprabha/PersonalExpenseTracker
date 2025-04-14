package com.dinesh.personalexpensetracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dinesh.personalexpensetracker.presentation.viewmodel.ExpenseViewModel
import java.util.*
import com.dinesh.personalexpensetracker.data.model.Expense

@Composable
fun ExpenseTrackerApp(viewModel: ExpenseViewModel = hiltViewModel()) {
    val expenses by viewModel.expenses.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    // State for filter selection
    val filterOptions = listOf("Day", "Week", "Month", "Year")
    var selectedFilter by remember { mutableStateOf("Month") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Expense Tracker",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Filter Dropdown
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { isDropdownExpanded = true }
            ) {
                Text(
                    text = "Showing: $selectedFilter",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Filter")
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    filterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedFilter = option
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Filtered amount
            val filteredAmount = calculateFilteredAmount(expenses, selectedFilter)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExpenseCard(
                    title = "$selectedFilter Expense",
                    amount = filteredAmount,
                    modifier = Modifier.weight(1f)
                )
                ExpenseCard(
                    title = "Daily Expense",
                    amount = expenses.filter {
                        it.timestamp > System.currentTimeMillis() - 86400000
                    }.sumOf { it.amount },
                    modifier = Modifier.weight(1f)
                )
            }

            ExpenseChart(expenses)

            if (expenses.isNotEmpty()) {
                val categoryData = getCategoryWiseData(expenses)
                val pieColors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.error,
                    MaterialTheme.colorScheme.inversePrimary,
                    MaterialTheme.colorScheme.outline
                )

                val labeledData = categoryData.entries.mapIndexed { index, entry ->
                    Triple(entry.key, entry.value.toDouble(), pieColors[index % pieColors.size])
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Expense by Category",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        // Centered PieChart
                        Spacer(modifier = Modifier.height(16.dp))

                        PieChart(
                            data = labeledData.associate { it.first to it.second },
                            radiusOuter = 48.dp,
                            chartBarWidth = 25.dp,
                            animDuration = 1200
                        )


                    }
                }

            }
        }
    }

    if (showDialog.value) {
        AddExpenseBottomSheet(
            onDismiss = { showDialog.value = false },
            onSave = {
                viewModel.addExpense(it)
                showDialog.value = false
            }
        )
    }
}

fun calculateFilteredAmount(expenses: List<Expense>, filter: String): Double {
    val now = Calendar.getInstance()
    return when (filter) {
        "Day" -> {
            val oneDayAgo = System.currentTimeMillis() - 86400000
            expenses.filter { it.timestamp >= oneDayAgo }.sumOf { it.amount }
        }
        "Week" -> {
            val oneWeekAgo = System.currentTimeMillis() - 7 * 86400000
            expenses.filter { it.timestamp >= oneWeekAgo }.sumOf { it.amount }
        }
        "Month" -> {
            now.set(Calendar.DAY_OF_MONTH, 1)
            val monthStart = now.timeInMillis
            expenses.filter { it.timestamp >= monthStart }.sumOf { it.amount }
        }
        "Year" -> {
            now.set(Calendar.MONTH, 0)
            now.set(Calendar.DAY_OF_MONTH, 1)
            val yearStart = now.timeInMillis
            expenses.filter { it.timestamp >= yearStart }.sumOf { it.amount }
        }
        else -> expenses.sumOf { it.amount }
    }
}

fun getCategoryWiseData(expenses: List<Expense>): Map<String, Double> {
    return expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
}
