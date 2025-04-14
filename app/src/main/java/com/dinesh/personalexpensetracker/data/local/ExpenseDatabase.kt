package com.dinesh.personalexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dinesh.personalexpensetracker.data.model.Expense

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class ExpenseDatabase() : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao
}