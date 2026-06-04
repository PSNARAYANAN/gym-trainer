package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athletes WHERE id = 1")
    fun getAthleteFlow(): Flow<Athlete?>

    @Query("SELECT * FROM athletes WHERE id = 1")
    suspend fun getAthlete(): Athlete?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: Athlete)

    @Query("UPDATE athletes SET isLoggedIn = :isLoggedIn WHERE id = 1")
    suspend fun updateLoggedState(isLoggedIn: Boolean)
}

@Dao
interface LoggedMealDao {
    @Query("SELECT * FROM logged_meals ORDER BY timestamp DESC")
    fun getAllMealsFlow(): Flow<List<LoggedMeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: LoggedMeal)

    @Delete
    suspend fun deleteMeal(meal: LoggedMeal)

    @Query("DELETE FROM logged_meals")
    suspend fun clearAllMeals()
}

@Dao
interface WeightRecordDao {
    @Query("SELECT * FROM weight_records ORDER BY timestamp ASC")
    fun getAllWeightRecordsFlow(): Flow<List<WeightRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecord)

    @Query("DELETE FROM weight_records")
    suspend fun clearWeightRecords()
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY id ASC")
    fun getAllExercisesFlow(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Query("UPDATE exercises SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Int, isBookmarked: Boolean)
}
