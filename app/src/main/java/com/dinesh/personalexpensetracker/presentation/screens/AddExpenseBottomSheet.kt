package com.dinesh.personalexpensetracker.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dinesh.personalexpensetracker.data.model.Expense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseBottomSheet(onDismiss: () -> Unit, onSave: (Expense) -> Unit) {
    val categories = listOf("Food", "Entertainment", "Travel", "Shopping", "Others")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("Add Expense", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow {
                items(categories) {
                    AssistChip(
                        onClick = { selectedCategory = it },
                        label = { Text(it) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }
            )
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = onDismiss) { Text("Cancel") }
                Button(onClick = {
                    onSave(
                        Expense(
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            category = selectedCategory,
                            description = description
                        )
                    )
                }) { Text("Add") }
            }
        }
    }
}