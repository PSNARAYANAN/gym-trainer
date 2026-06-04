package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Login : Screen()
    object Signup : Screen()
    object Home : Screen()
    object Exercises : Screen()
    object ScanLabel : Screen()
    object ScanFood : Screen()
    object Profile : Screen()
}

class IronFuelViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = IronFuelRepository(db)

    // Current Screen flow
    var currentScreen by mutableStateOf<Screen>(Screen.Login)
        private set

    // Active Athlete Profile
    val athlete: StateFlow<Athlete?> = repository.athlete.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Logged meals list
    val meals: StateFlow<List<LoggedMeal>> = repository.allMeals.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Historical weights list
    val weights: StateFlow<List<WeightRecord>> = repository.allWeights.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Exercises state
    val exerciseList: StateFlow<List<ExerciseEntity>> = repository.allExercises.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Form states
    var fullNameInput by mutableStateOf("")
    var emailInput by mutableStateOf("")
    var passwordInput by mutableStateOf("")
    var confirmPasswordInput by mutableStateOf("")
    var selectedGoalInput by mutableStateOf("GAIN_MUSCLE") // GAIN_MUSCLE or LOSE_WEIGHT

    // Auth error messages or states
    var authError by mutableStateOf<String?>(null)

    // Search & Filter state
    var exerciseSearchQuery by mutableStateOf("")
    var selectedCategoryFilter by mutableStateOf("ALL")

    // Dynamic scanner states
    var scannerResultLabelName by mutableStateOf("Protein Performance Shake")
    var scannerGrade by mutableStateOf(82)
    var scannerGradeLabel by mutableStateOf("EXCELLENT")
    var scannerAnalysisText by mutableStateOf("High nutritional density detected")
    var isScanningLabelActive by mutableStateOf(false)
    var scanLabelComplete by mutableStateOf(false)

    // Food Scanner states
    var isScanningFoodActive by mutableStateOf(false)
    var scanFoodComplete by mutableStateOf(false)
    var foodScanItemName by mutableStateOf("Grilled Chicken Breast & Quinoa")
    var foodScanPortion by mutableStateOf("250g")
    var foodScanKcal by mutableStateOf(420)
    var foodScanProtein by mutableStateOf(45)
    var foodScanCarbs by mutableStateOf(35)
    var foodScanFat by mutableStateOf(8)

    // Custom data input triggers
    var showAddMealDialog by mutableStateOf(false)
    var addMealName by mutableStateOf("")
    var addMealKcal by mutableStateOf("")
    var addMealProtein by mutableStateOf("")
    var addMealCarbs by mutableStateOf("")
    var addMealFat by mutableStateOf("")
    var addMealPortion by mutableStateOf("300g")

    var showAddWeightDialog by mutableStateOf(false)
    var addWeightValue by mutableStateOf("")

    var showAddExerciseDialog by mutableStateOf(false)
    var addExerciseName by mutableStateOf("")
    var addExerciseCategory by mutableStateOf("CHEST")
    var addExerciseDifficulty by mutableStateOf("BEGINNER")
    var addExerciseEquipment by mutableStateOf("BARBELL")

    init {
        viewModelScope.launch {
            repository.initializeDefaultDataIfNeeded()
            // Check if current athlete is marked as logged in
            val current = repository.athlete.firstOrNull()
            if (current != null && current.isLoggedIn) {
                currentScreen = Screen.Home
            }
        }
    }

    fun navigateTo(screen: Screen) {
        currentScreen = screen
        authError = null
    }

    // AUTH ACTIONS
    fun login() {
        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            authError = "Please enter email and password"
            return
        }
        viewModelScope.launch {
            val current = athlete.value
            if (current != null) {
                // Pre-fill input
                repository.insertAthlete(
                    current.copy(
                        fullName = if (current.fullName.isEmpty()) "Alex Mercer" else current.fullName,
                        email = emailInput,
                        isLoggedIn = true
                    )
                )
            } else {
                repository.insertAthlete(
                    Athlete(
                        fullName = "Alex Mercer",
                        email = emailInput,
                        isLoggedIn = true
                    )
                )
            }
            navigateTo(Screen.Home)
        }
    }

    fun signUp() {
        if (fullNameInput.isEmpty() || emailInput.isEmpty() || passwordInput.isEmpty()) {
            authError = "Please fill all fields"
            return
        }
        if (passwordInput != confirmPasswordInput) {
            authError = "Passwords do not match"
            return
        }
        viewModelScope.launch {
            repository.insertAthlete(
                Athlete(
                    fullName = fullNameInput,
                    email = emailInput,
                    objective = selectedGoalInput,
                    isLoggedIn = true
                )
            )
            navigateTo(Screen.Home)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.updateLoggedState(false)
            fullNameInput = ""
            emailInput = ""
            passwordInput = ""
            confirmPasswordInput = ""
            navigateTo(Screen.Login)
        }
    }

    // PROFILE ACTIONS
    fun updateObjective(goal: String) {
        viewModelScope.launch {
            athlete.value?.let {
                repository.insertAthlete(it.copy(objective = goal))
            }
        }
    }

    fun updateStreak(delta: Int) {
        viewModelScope.launch {
            athlete.value?.let {
                val newStreak = (it.streak + delta).coerceAtLeast(0)
                repository.insertAthlete(it.copy(streak = newStreak))
            }
        }
    }

    fun updateTargetKcal(target: Int) {
        viewModelScope.launch {
            athlete.value?.let {
                repository.insertAthlete(it.copy(targetKcal = target))
            }
        }
    }

    // MEAL LOGGER ACTIONS
    fun logCustomMeal() {
        val kcal = addMealKcal.toIntOrNull() ?: 0
        val prot = addMealProtein.toIntOrNull() ?: 0
        val carb = addMealCarbs.toIntOrNull() ?: 0
        val fatVal = addMealFat.toIntOrNull() ?: 0
        if (addMealName.isEmpty() || kcal <= 0) return

        viewModelScope.launch {
            repository.insertMeal(
                LoggedMeal(
                    name = addMealName,
                    calories = kcal,
                    protein = prot,
                    carbs = carb,
                    fat = fatVal,
                    portion = addMealPortion
                )
            )
            // Reset fields
            addMealName = ""
            addMealKcal = ""
            addMealProtein = ""
            addMealCarbs = ""
            addMealFat = ""
            addMealPortion = "1 serving"
            showAddMealDialog = false
        }
    }

    fun deleteMeal(meal: LoggedMeal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }

    fun clearAllMeals() {
        viewModelScope.launch {
            repository.clearMeals()
        }
    }

    // TRIGGER SCANS
    fun startScanLabelSimulation() {
        isScanningLabelActive = true
        scanLabelComplete = false
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // 2s scanning animation delay
            isScanningLabelActive = false
            scanLabelComplete = true
        }
    }

    fun logLabelMacrosState() {
        viewModelScope.launch {
            // High protein shake stats mapped
            repository.insertMeal(
                LoggedMeal(
                    name = scannerResultLabelName,
                    calories = 220,
                    protein = 25,
                    carbs = 8,
                    fat = 2,
                    portion = "350ml"
                )
            )
            scanLabelComplete = false
            navigateTo(Screen.Home)
        }
    }

    fun startScanFoodSimulation() {
        isScanningFoodActive = true
        scanFoodComplete = false
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // 2s food overlay scan
            isScanningFoodActive = false
            scanFoodComplete = true
        }
    }

    fun logFoodMacrosState() {
        viewModelScope.launch {
            repository.insertMeal(
                LoggedMeal(
                    name = foodScanItemName,
                    calories = foodScanKcal,
                    protein = foodScanProtein,
                    carbs = foodScanCarbs,
                    fat = foodScanFat,
                    portion = foodScanPortion
                )
            )
            scanFoodComplete = false
            navigateTo(Screen.Home)
        }
    }

    // EXERCISE ACTIONS
    fun toggleBookmark(id: Int, isCurrentlyBookmarked: Boolean) {
        viewModelScope.launch {
            repository.updateExerciseBookmark(id, !isCurrentlyBookmarked)
        }
    }

    fun addNewExercise() {
        if (addExerciseName.isEmpty()) return
        viewModelScope.launch {
            val randomId = (100..100000).random()
            repository.insertExercise(
                ExerciseEntity(
                    id = randomId,
                    name = addExerciseName,
                    category = addExerciseCategory,
                    difficulty = addExerciseDifficulty,
                    equipment = addExerciseEquipment,
                    imageUrl = "",
                    isBookmarked = false,
                    isCustom = true
                )
            )
            addExerciseName = ""
            showAddExerciseDialog = false
        }
    }

    // WEIGHT INDICES ACTIONS
    fun addNewWeightRecord() {
        val wVal = addWeightValue.toFloatOrNull() ?: 0.0f
        if (wVal <= 0.0f) return
        viewModelScope.launch {
            repository.insertWeightRecord(
                WeightRecord(
                    timestamp = System.currentTimeMillis(),
                    weight = wVal
                )
            )
            addWeightValue = ""
            showAddWeightDialog = false
        }
    }
}
