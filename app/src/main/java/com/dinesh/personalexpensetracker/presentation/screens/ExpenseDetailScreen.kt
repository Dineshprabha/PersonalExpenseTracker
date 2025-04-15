package com.dinesh.personalexpensetracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dinesh.personalexpensetracker.presentation.screens.tabs.DailyExpensesView
import com.dinesh.personalexpensetracker.presentation.screens.tabs.WeeklyExpensesView
import com.dinesh.personalexpensetracker.presentation.screens.tabs.MonthlyExpenseView
import com.dinesh.personalexpensetracker.presentation.viewmodel.ExpenseViewModel

@Composable
fun ExpenseDetailScreen(
    navController: NavController,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val tabOptions = listOf("Day", "Week", "Month")
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Expense Summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Tab Row in a styled Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                divider = {}, // Removes default divider for a cleaner look
                modifier = Modifier.clip(MaterialTheme.shapes.medium)
            ) {
                tabOptions.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        // Tab Content
        when (selectedTab) {
            0 -> DailyExpensesView(viewModel)
            1 -> WeeklyExpensesView(viewModel)
            2 -> MonthlyExpenseView(viewModel)
        }
    }
}
