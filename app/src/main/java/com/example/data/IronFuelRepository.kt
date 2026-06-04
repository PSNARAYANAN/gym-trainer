package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class IronFuelRepository(private val db: AppDatabase) {

    private val athleteDao = db.athleteDao()
    private val loggedMealDao = db.loggedMealDao()
    private val weightRecordDao = db.weightRecordDao()
    private val exerciseDao = db.exerciseDao()

    val athlete: Flow<Athlete?> = athleteDao.getAthleteFlow()
    val allMeals: Flow<List<LoggedMeal>> = loggedMealDao.getAllMealsFlow()
    val allWeights: Flow<List<WeightRecord>> = weightRecordDao.getAllWeightRecordsFlow()
    val allExercises: Flow<List<ExerciseEntity>> = exerciseDao.getAllExercisesFlow()

    suspend fun insertAthlete(athlete: Athlete) {
        athleteDao.insertAthlete(athlete)
    }

    suspend fun updateLoggedState(isLoggedIn: Boolean) {
        athleteDao.updateLoggedState(isLoggedIn)
    }

    suspend fun insertMeal(meal: LoggedMeal) {
        loggedMealDao.insertMeal(meal)
    }

    suspend fun deleteMeal(meal: LoggedMeal) {
        loggedMealDao.deleteMeal(meal)
    }

    suspend fun insertWeightRecord(record: WeightRecord) {
        weightRecordDao.insertWeightRecord(record)
    }

    suspend fun updateExerciseBookmark(id: Int, isBookmarked: Boolean) {
        exerciseDao.updateBookmark(id, isBookmarked)
    }

    suspend fun insertExercise(exercise: ExerciseEntity) {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun clearMeals() {
        loggedMealDao.clearAllMeals()
    }

    suspend fun initializeDefaultDataIfNeeded() {
        // 1. Seed Athlete profile
        val existingAthlete = athleteDao.getAthlete()
        if (existingAthlete == null) {
            athleteDao.insertAthlete(
                Athlete(
                    id = 1,
                    fullName = "Alex Mercer",
                    email = "athlete@ironfuel.com",
                    objective = "GAIN_MUSCLE",
                    targetKcal = 2800,
                    streak = 7,
                    isLoggedIn = false // Screen opens with register/login, can login to dashboard!
                )
            )
        }

        // 2. Seed Default Exercises (matching screenshot)
        val existingExercises = exerciseDao.getAllExercisesFlow().firstOrNull() ?: emptyList()
        if (existingExercises.isEmpty()) {
            val defaultExercises = listOf(
                ExerciseEntity(
                    id = 1,
                    name = "Barbell Bench Press",
                    category = "CHEST",
                    difficulty = "INTERMEDIATE",
                    equipment = "BARBELL",
                    imageUrl = "bench",
                    isBookmarked = true
                ),
                ExerciseEntity(
                    id = 2,
                    name = "Back Squat",
                    category = "LEGS",
                    difficulty = "ADVANCED",
                    equipment = "BARBELL",
                    imageUrl = "squat",
                    isBookmarked = false
                ),
                ExerciseEntity(
                    id = 3,
                    name = "Weighted Pull Ups",
                    category = "BACK",
                    difficulty = "ADVANCED",
                    equipment = "BODYWEIGHT",
                    imageUrl = "pullup",
                    isBookmarked = true
                ),
                ExerciseEntity(
                    id = 4,
                    name = "Dumbbell Lateral Raise",
                    category = "SHOULDERS",
                    difficulty = "BEGINNER",
                    equipment = "DUMBBELL",
                    imageUrl = "raise",
                    isBookmarked = false
                )
            )
            exerciseDao.insertExercises(defaultExercises)
        }

        // 3. Seed Logged Meals to sum up perfectly:
        // Consumed = 1850 kcal
        // Target = 2800 kcal
        // Protein = 120g / 180g
        // Carbs = 200g / 350g
        // Fat = 60g / 80g
        val existingMeals = loggedMealDao.getAllMealsFlow().firstOrNull() ?: emptyList()
        if (existingMeals.isEmpty()) {
            loggedMealDao.insertMeal(
                LoggedMeal(
                    name = "Post-Workout Whey Shake",
                    calories = 350,
                    protein = 35,
                    carbs = 45,
                    fat = 3,
                    portion = "1 serving"
                )
            )
            loggedMealDao.insertMeal(
                LoggedMeal(
                    name = "Breakfast Oatmeal & Blueberries",
                    calories = 550,
                    protein = 40,
                    carbs = 65,
                    fat = 12,
                    portion = "150g"
                )
            )
            loggedMealDao.insertMeal(
                LoggedMeal(
                    name = "Beef Teriyaki with Sweet Potato",
                    calories = 900,
                    protein = 45,
                    carbs = 90,
                    fat = 45,
                    portion = "400g"
                )
            )
        }

        // 4. Seed weight records (Monday to Sunday weight trend, culminating at 76.5 kg)
        // Saturday: 76.2 kg, Friday: 75.8 kg, Thursday: 75.5 kg, Wednesday: 75.2 kg, Tuesday: 75.0 kg, Monday: 75.0 kg, Sunday: 76.5 kg.
        val existingWeights = weightRecordDao.getAllWeightRecordsFlow().firstOrNull() ?: emptyList()
        if (existingWeights.isEmpty()) {
            val calendar = Calendar.getInstance()
            // Set scale weights for last 7 days starting from Monday
            val weights = listOf(75.0f, 75.0f, 75.2f, 75.5f, 75.8f, 76.2f, 76.5f)
            weights.forEachIndexed { index, weightVal ->
                val dayCal = calendar.clone() as Calendar
                dayCal.add(Calendar.DAY_OF_YEAR, - (6 - index))
                weightRecordDao.insertWeightRecord(
                    WeightRecord(
                        timestamp = dayCal.timeInMillis,
                        weight = weightVal
                    )
                )
            }
        }
    }
}
