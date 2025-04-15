package com.dinesh.personalexpensetracker.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dinesh.personalexpensetracker.data.model.Expense
import com.dinesh.personalexpensetracker.presentation.viewmodel.ExpenseViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerApp(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    val filterOptions = listOf("Week", "Month", "Year")
    var selectedFilter by remember { mutableStateOf("Month") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Expense Tracker",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
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

            // Filter Dropdown Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .clickable { isDropdownExpanded = true }
            ) {
                Text(
                    text = " $selectedFilter",
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

            // Filtered Expense Summary
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
                        it.date.time > System.currentTimeMillis() - 86400000
                    }.sumOf { it.amount },
                    modifier = Modifier.weight(1f)
                )
            }

            // Chart Overview
            ExpenseChart(expenses, onViewDetailsClick = { navController.navigate("details") })

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
                            text = "Expense Chart",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

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
            expenses.filter { it.date.time >= oneDayAgo }.sumOf { it.amount }
        }

        "Week" -> {
            val oneWeekAgo = System.currentTimeMillis() - 7 * 86400000
            expenses.filter { it.date.time >= oneWeekAgo }.sumOf { it.amount }
        }

        "Month" -> {
            now.set(Calendar.DAY_OF_MONTH, 1)
            val monthStart = now.timeInMillis
            expenses.filter { it.date.time >= monthStart }.sumOf { it.amount }
        }

        "Year" -> {
            now.set(Calendar.MONTH, 0)
            now.set(Calendar.DAY_OF_MONTH, 1)
            val yearStart = now.timeInMillis
            expenses.filter { it.date.time >= yearStart }.sumOf { it.amount }
        }

        else -> expenses.sumOf { it.amount }
    }
}


fun getCategoryWiseData(expenses: List<Expense>): Map<String, Double> {
    return expenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
}
