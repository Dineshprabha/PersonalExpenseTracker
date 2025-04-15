package com.dinesh.personalexpensetracker.presentation.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateSelector(
    selectedDate: Date,
    onDateSelected: (Date) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { time = selectedDate }

    val dateFormatter = remember {
        java.text.SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

    val dateText = remember(selectedDate) {
        dateFormatter.format(selectedDate)
    }

    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        enabled = false,
        label = { Text("Select Date") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Pick Date"
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val newDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }.time
                        onDateSelected(newDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
    )
}
