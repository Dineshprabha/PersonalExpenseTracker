package com.dinesh.personalexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dinesh.personalexpensetracker.presentation.screens.ExpenseTrackerApp
import com.dinesh.personalexpensetracker.ui.theme.PersonalExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalExpenseTrackerTheme {
                ExpenseTrackerApp()
            }
        }
    }
}
