package com.dinesh.personalexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
