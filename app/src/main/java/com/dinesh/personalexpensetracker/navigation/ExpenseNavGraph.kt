package com.dinesh.personalexpensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dinesh.personalexpensetracker.presentation.screens.ExpenseDetailScreen
import com.dinesh.personalexpensetracker.presentation.screens.ExpenseTrackerApp

@Composable
fun ExpenseNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            ExpenseTrackerApp(navController)
        }
        composable("details") {
            ExpenseDetailScreen(navController)
        }
    }
}
