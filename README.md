# PersonalExpenseTracker App

This project demonstrates the use of modern Android development best practices, leveraging Jetpack Compose, MVVM architecture, and several powerful libraries. The project follows a modular structure with clean separation of concerns into `data`, `domain`, and `presentation` layers.

---

## Table of Contents

- [Libraries Used](#libraries-used)
- [Project Structure](#project-structure)
- [MVVM Architecture](#mvvm-architecture)
- [Setup Instructions](#setup-instructions)

---

## Libraries Used

### Room
- **Dependency**:
  ```
  implementation("androidx.room:room-runtime:2.6.1")
  kapt("androidx.room:room-compiler:2.6.1")
  implementation("androidx.room:room-ktx:2.6.1")
  ```
- **Purpose**: Provides an abstraction layer over SQLite to allow fluent database access while following the MVVM pattern.

### Accompanist
- **Dependency**:
  ```
  implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")
  ```
- **Purpose**: Utility library for Compose. In this project, it is used for managing system UI components like the status bar and navigation bar colors.

### Compose Navigation
- **Dependency**:
  ```
  implementation("androidx.navigation:navigation-compose:2.7.6")
  ```
- **Purpose**: Enables navigation between composable screens in a type-safe and declarative way.

### Dagger Hilt
- **Dependency**:
  ```
  implementation("com.google.dagger:hilt-android:2.50")
  kapt("com.google.dagger:hilt-compiler:2.50")
  implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
  ```
- **Purpose**: Simplifies dependency injection in Android applications. Integrated with ViewModels and Compose.

---

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture with a clean separation of concerns. Here's an overview of the structure:

### `data` Layer
- Contains all the data-related logic.
- **Components**:
  - Repositories: Handle API and database operations.
  - Data Sources: Abstract APIs and local database interactions.
  - Models: Represent API responses and database entities.

### `domain` Layer
- Acts as a bridge between `data` and `presentation` layers.

### `presentation` Layer
- Contains the UI and user interaction logic.
- **Components**:
  - Composables: Define the UI using Jetpack Compose.
  - ViewModels: Manage UI-related data and handle logic by interacting with the `domain` layer.

---

## MVVM Architecture

- **Model**: Responsible for data management (e.g., Room database, Retrofit API).
- **View**: Includes the composable functions that represent the UI.
- **ViewModel**: Acts as a mediator between the View and the Model. It retrieves data from the domain layer and provides it to the View.

---

## Setup Instructions

### Prerequisites
Ensure you have the following installed:
- Android Studio Flamingo or later
- Kotlin 1.8+
- Gradle 8.0+

### Steps to Run
1. Clone the repository:
   ```bash
   https://github.com/Dineshprabha/PersonalExpenseTracker.git
   ```
2. Open the project in Android Studio.
3. Sync the project to download dependencies.
4. Build and run the project on an emulator or a physical device.

---

## Features
- Modern Android UI using Jetpack Compose.
- Persistent data storage with Room.
- Dependency Injection with Dagger Hilt.
- Navigation using Jetpack Compose Navigation.

---

## The Complete Project Folder Structure

```
PersonalExpenseTracker/
├── data/
│   ├── local/
│   │   ├── ExpenseDao.kt
│   │   └── ExpenseDatabase.kt
│   ├── model/
│   │   └── Expense.kt
│   └── repository/
│       └── ExpenseRepository.kt
│
├── di/
│   └── AppModule.kt
│
├── presentation/
│   ├── screens/
│   │   ├── ExpenseTrackerApp.kt
│   │   ├── AddExpenseBottomSheet.kt
│   │   ├── ExpenseItem.kt
│   │   ├── DateSelector.kt	
│   │   ├── ExpenseCard.kt
│   │   ├── ExpenseChart.kt
│   │   ├── ExpenseDetailScreen.kt
│   │   └── PieChart.kt
│   └── viewmodel/
│       └── ExpenseViewModel.kt
│
├── navigation/
│   └── NavGraph.kt
│
├── ui/
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Typography.kt
│
├── MainActivity.kt
└── PersonalExpenseTrackerApplication.kt


---
