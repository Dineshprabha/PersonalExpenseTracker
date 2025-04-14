package com.dinesh.personalexpensetracker.di

import android.app.Application
import androidx.room.Room
import com.dinesh.personalexpensetracker.data.local.ExpenseDao
import com.dinesh.personalexpensetracker.data.local.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ExpenseDatabase =
        Room.databaseBuilder(app, ExpenseDatabase::class.java, "expenses.db").build()

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDatabase): ExpenseDao = db.expenseDao()

}