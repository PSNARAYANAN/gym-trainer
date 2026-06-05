package com.example.data

import kotlinx.coroutines.flow.Flow

class IronFuelRepository(private val db: AppDatabase) {
    private val userDao = db.userDao()
    private val nutritionDao = db.nutritionDao()
    private val workoutDao = db.workoutDao()
    private val cardioDao = db.cardioDao()
    private val waterDao = db.waterDao()

    // Users
    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    // Nutrition
    fun getNutritionLogs(email: String): Flow<List<NutritionLog>> {
        return nutritionDao.getLogsForUser(email)
    }

    suspend fun insertNutrition(log: NutritionLog) {
        nutritionDao.insertLog(log)
    }

    suspend fun deleteNutrition(id: Int) {
        nutritionDao.deleteLogById(id)
    }

    // Workouts
    fun getWorkoutLogs(email: String): Flow<List<WorkoutLog>> {
        return workoutDao.getLogsForUser(email)
    }

    suspend fun insertWorkout(log: WorkoutLog) {
        workoutDao.insertLog(log)
    }

    suspend fun deleteWorkout(id: Int) {
        workoutDao.deleteLogById(id)
    }

    // Cardio
    fun getCardioLogs(email: String): Flow<List<CardioLog>> {
        return cardioDao.getLogsForUser(email)
    }

    suspend fun insertCardio(log: CardioLog) {
        cardioDao.insertLog(log)
    }

    suspend fun deleteCardio(id: Int) {
        cardioDao.deleteLogById(id)
    }

    // Water
    fun getWaterLogs(email: String): Flow<List<WaterLog>> {
        return waterDao.getLogsForUser(email)
    }

    suspend fun insertWater(log: WaterLog) {
        waterDao.insertLog(log)
    }

    suspend fun deleteWater(id: Int) {
        waterDao.deleteLogById(id)
    }
}
