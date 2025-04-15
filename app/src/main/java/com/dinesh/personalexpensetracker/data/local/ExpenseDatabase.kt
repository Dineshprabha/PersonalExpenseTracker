package com.dinesh.personalexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dinesh.personalexpensetracker.data.model.Expense

@Database(entities = [Expense::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase() : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao
}