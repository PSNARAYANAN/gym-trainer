package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athletes")
data class Athlete(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "Alex Mercer",
    val email: String = "alex@ironfuel.com",
    val objective: String = "GAIN_MUSCLE", // "GAIN_MUSCLE" or "LOSE_WEIGHT"
    val targetKcal: Int = 2800,
    val streak: Int = 7,
    val isLoggedIn: Boolean = false
)

@Entity(tableName = "logged_meals")
data class LoggedMeal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val portion: String = "250g"
)

@Entity(tableName = "weight_records")
data class WeightRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val weight: Float
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val category: String, // CHEST, BACK, SHOULDERS, LEGS, ARMS, CORE
    val difficulty: String, // BEGINNER, INTERMEDIATE, ADVANCED
    val equipment: String, // BARBELL, DUMBBELL, BODYWEIGHT
    val imageUrl: String = "",
    val isBookmarked: Boolean = false,
    val isCustom: Boolean = false
)
