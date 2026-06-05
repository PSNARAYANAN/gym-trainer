package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}

@Dao
interface NutritionDao {
    @Query("SELECT * FROM nutrition_logs WHERE userEmail = :email ORDER BY timestamp DESC")
    fun getLogsForUser(email: String): Flow<List<NutritionLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: NutritionLog)

    @Query("DELETE FROM nutrition_logs WHERE id = :id")
    suspend fun deleteLogById(id: Int)
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_logs WHERE userEmail = :email ORDER BY timestamp DESC")
    fun getLogsForUser(email: String): Flow<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WorkoutLog)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteLogById(id: Int)
}

@Dao
interface CardioDao {
    @Query("SELECT * FROM cardio_logs WHERE userEmail = :email ORDER BY timestamp DESC")
    fun getLogsForUser(email: String): Flow<List<CardioLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: CardioLog)

    @Query("DELETE FROM cardio_logs WHERE id = :id")
    suspend fun deleteLogById(id: Int)
}

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_logs WHERE userEmail = :email ORDER BY timestamp DESC")
    fun getLogsForUser(email: String): Flow<List<WaterLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WaterLog)

    @Query("DELETE FROM water_logs WHERE id = :id")
    suspend fun deleteLogById(id: Int)
}

