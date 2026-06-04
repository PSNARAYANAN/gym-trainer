package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val passwordHash: String,
    val displayName: String,
    val dailyCalorieTarget: Int = 2800,
    val dailyProteinTarget: Int = 180,
    val gender: String = "Male",
    val bodyWeightKg: Double = 80.0
)

@Entity(tableName = "nutrition_logs")
data class NutritionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val itemName: String,
    val calories: Int,
    val proteinGrams: Int,
    val carbsGrams: Int,
    val fatGrams: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weightKg: Double,
    val timestamp: Long = System.currentTimeMillis()
)
