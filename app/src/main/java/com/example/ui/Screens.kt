package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NutritionLog
import com.example.data.WorkoutLog
import com.example.data.CardioLog
import com.example.data.WaterLog
import com.example.ui.theme.*

enum class AppScreen {
    LOGIN,
    SIGNUP,
    DASHBOARD
}

@Composable
fun IronFuelAppContent(
    viewModel: IronFuelViewModel,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }
    val user by viewModel.currentUser.collectAsState()

    // Redirect to Dashboard if logged in
    LaunchedEffect(user) {
        if (user != null && currentScreen != AppScreen.DASHBOARD) {
            currentScreen = AppScreen.DASHBOARD
        } else if (user == null && currentScreen == AppScreen.DASHBOARD) {
            currentScreen = AppScreen.LOGIN
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkSlateBg)
    ) {
        Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
            when (screen) {
                AppScreen.LOGIN -> LoginScreen(
                    viewModel = viewModel,
                    onNavigateToSignup = {
                        viewModel.clearAuthErrors()
                        currentScreen = AppScreen.SIGNUP
                    }
                )
                AppScreen.SIGNUP -> SignupScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = {
                        viewModel.clearAuthErrors()
                        currentScreen = AppScreen.LOGIN
                    }
                )
                AppScreen.DASHBOARD -> DashboardScreen(
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout()
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
        }
    }
}

// ==========================================
// 1. LOGIN SCREEN
// ==========================================
@Composable
fun LoginScreen(
    viewModel: IronFuelViewModel,
    onNavigateToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginError by viewModel.loginError.collectAsState()
    val isLoading by viewModel.isAuthLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState()), // Avoid keyboard clipping
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo / Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(FuelOrange, IronCoreRed))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow, // Matches geometric Hub center
                    contentDescription = "Fuel Core",
                    modifier = Modifier.size(44.dp),
                    tint = CleanWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "IRONFUEL",
                style = Typography.displayLarge,
                color = CleanWhite,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Precision Performance & Nutrition Tracker",
                style = Typography.bodyMedium,
                color = PaleSlateText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In",
                        style = Typography.titleLarge,
                        color = CleanWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email field
                    val isEmailError = loginError != null && loginError?.contains("email", ignoreCase = true) == true
                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            viewModel.clearAuthErrors()
                        },
                        label = { Text("Email Address") },
                        isError = isEmailError,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = FuelOrange) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FuelOrange,
                            unfocusedBorderColor = BorderSlate,
                            focusedLabelColor = FuelOrange,
                            unfocusedLabelColor = PaleSlateText,
                            focusedTextColor = CleanWhite,
                            unfocusedTextColor = CleanWhite,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password field
                    val isPasswordError = loginError != null && loginError?.contains("password", ignoreCase = true) == true
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            viewModel.clearAuthErrors()
                        },
                        label = { Text("Password") },
                        isError = isPasswordError,
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FuelOrange) },
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Default.List else Icons.Default.Lock
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(image, contentDescription = "Toggle password visibility")
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FuelOrange,
                            unfocusedBorderColor = BorderSlate,
                            focusedLabelColor = FuelOrange,
                            unfocusedLabelColor = PaleSlateText,
                            focusedTextColor = CleanWhite,
                            unfocusedTextColor = CleanWhite,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        ),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input")
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (loginError != null) {
                        Text(
                            text = loginError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Box(modifier = Modifier.size(width = 1.dp, height = 1.dp).testTag("error_view"))

                    // Login Button
                    Button(
                        onClick = { viewModel.doLogin(email, password, {}) },
                        colors = ButtonDefaults.buttonColors(containerColor = FuelOrange),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
 {
                        if (isLoading) {
                            CircularProgressIndicator(color = CleanWhite, modifier = Modifier.size(24.dp))
                        } else {
                            Text("LOG IN", style = Typography.bodyLarge, fontWeight = FontWeight.Bold, color = CleanWhite)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("New Athlete?", color = PaleSlateText, style = Typography.bodyMedium)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Register Account",
                            color = FuelOrange,
                            fontWeight = FontWeight.Bold,
                            style = Typography.bodyMedium,
                            modifier = Modifier
                                .clickable { onNavigateToSignup() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Demo Account Tapper
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        email = "coach@ironfuel.fit"
                        password = "password123"
                    },
                colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface.copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, BorderSlate)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = AmberGlow, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Demo Account Available", color = CleanWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text("Tap to autofill: coach@ironfuel.fit / password123", color = PaleSlateText, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. SIGNUP SCREEN
// ==========================================
@Composable
fun SignupScreen(
    viewModel: IronFuelViewModel,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var calorieTarget by remember { mutableStateOf("2800") }
    var proteinTarget by remember { mutableStateOf("180") }

    val signupError by viewModel.signupError.collectAsState()
    val isLoading by viewModel.isAuthLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "IRONFUEL",
                style = Typography.headlineMedium,
                color = FuelOrange,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Setup your Athlete Profile",
                style = Typography.bodyMedium,
                color = PaleSlateText
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Register",
                        style = Typography.titleLarge,
                        color = CleanWhite,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display Name
                    val isNameError = signupError != null && signupError?.contains("display name", ignoreCase = true) == true
                    OutlinedTextField(
                        value = name,
                        onValueChange = { 
                            name = it
                            viewModel.clearAuthErrors()
                        },
                        label = { Text("Display Name") },
                        isError = isNameError,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = FuelOrange) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate,
                            focusedLabelColor = FuelOrange, unfocusedLabelColor = PaleSlateText,
                            focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    val isEmailError = signupError != null && signupError?.contains("email", ignoreCase = true) == true
                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            viewModel.clearAuthErrors()
                        },
                        label = { Text("Email Address") },
                        isError = isEmailError,
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = FuelOrange) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate,
                            focusedLabelColor = FuelOrange, unfocusedLabelColor = PaleSlateText,
                            focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password
                    val isPasswordError = signupError != null && signupError?.contains("password", ignoreCase = true) == true
                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            viewModel.clearAuthErrors()
                        },
                        label = { Text("Password") },
                        isError = isPasswordError,
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = FuelOrange) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate,
                            focusedLabelColor = FuelOrange, unfocusedLabelColor = PaleSlateText,
                            focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                            errorBorderColor = MaterialTheme.colorScheme.error,
                            errorLabelColor = MaterialTheme.colorScheme.error
                        ),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val isCalorieError = signupError != null && signupError?.contains("calorie", ignoreCase = true) == true
                    val isProteinError = signupError != null && signupError?.contains("protein", ignoreCase = true) == true

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = calorieTarget,
                            onValueChange = { 
                                calorieTarget = it
                                viewModel.clearAuthErrors()
                            },
                            label = { Text("Daily kcal Target") },
                            isError = isCalorieError,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate,
                                focusedLabelColor = FuelOrange, unfocusedLabelColor = PaleSlateText,
                                focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                errorLabelColor = MaterialTheme.colorScheme.error
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        )

                        OutlinedTextField(
                            value = proteinTarget,
                            onValueChange = { 
                                proteinTarget = it
                                viewModel.clearAuthErrors()
                            },
                            label = { Text("Daily Protein (g)") },
                            isError = isProteinError,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate,
                                focusedLabelColor = FuelOrange, unfocusedLabelColor = PaleSlateText,
                                focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                errorLabelColor = MaterialTheme.colorScheme.error
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (signupError != null) {
                        Text(
                            text = signupError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val cals = calorieTarget.toIntOrNull() ?: 2800
                            val prot = proteinTarget.toIntOrNull() ?: 180
                            viewModel.doSignup(email, password, name, cals, prot, {})
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FuelOrange),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("SIGN UP", style = Typography.bodyLarge, fontWeight = FontWeight.Bold, color = CleanWhite)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Already registered?", color = PaleSlateText, style = Typography.bodyMedium)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Log In",
                            color = FuelOrange,
                            fontWeight = FontWeight.Bold,
                            style = Typography.bodyMedium,
                            modifier = Modifier.clickable { onNavigateToLogin() }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. DASHBOARD MAIN SCREEN (With Navigation Tabs)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: IronFuelViewModel,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val nutritionLogs by viewModel.nutritionLogs.collectAsState()
    val workoutLogs by viewModel.workoutLogs.collectAsState()
    val cardioLogs by viewModel.cardioLogs.collectAsState()
    val waterLogs by viewModel.waterLogs.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Budget & Food Logger, 1 = Workouts, 2 = AI Scanner, 3 = Profile

    val totalCaloriesConsumed = nutritionLogs.sumOf { it.calories }
    val totalCaloriesBurned = cardioLogs.sumOf { it.caloriesBurned }
    val totalProtein = nutritionLogs.sumOf { it.proteinGrams }
    val totalWaterMl = waterLogs.sumOf { it.amountMl }
    val targetCalories = user?.dailyCalorieTarget ?: 2800
    val targetProtein = user?.dailyProteinTarget ?: 180

    // Modals
    var showAddMealDialog by remember { mutableStateOf(false) }
    var showAddWorkoutDialog by remember { mutableStateOf(false) }
    var showAddCardioDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(FuelOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(20.dp), tint = CleanWhite)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "IronFuel",
                            fontWeight = FontWeight.ExtraBold,
                            style = Typography.titleLarge,
                            color = CleanWhite
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(BorderSlate)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = FuelOrange)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user?.displayName ?: "Athlete",
                            color = CleanWhite,
                            style = Typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 100.dp)
                        )
                    }

                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out", tint = IronCoreRed)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = DarkSlateBg)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DeepCarbonSurface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Nutrition Tracker") },
                    label = { Text("Log Diet") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FuelOrange,
                        selectedTextColor = FuelOrange,
                        unselectedIconColor = PaleSlateText,
                        unselectedTextColor = PaleSlateText,
                        indicatorColor = BorderSlate
                    )
                )

                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    icon = { Icon(Icons.Default.ThumbUp, contentDescription = "Barbell Tracker") },
                    label = { Text("Train") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FuelOrange,
                        selectedTextColor = FuelOrange,
                        unselectedIconColor = PaleSlateText,
                        unselectedTextColor = PaleSlateText,
                        indicatorColor = BorderSlate
                    )
                )

                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Gemini Scanner") },
                    label = { Text("AI Scan") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FuelOrange,
                        selectedTextColor = FuelOrange,
                        unselectedIconColor = PaleSlateText,
                        unselectedTextColor = PaleSlateText,
                        indicatorColor = BorderSlate
                    )
                )

                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { activeTab = 3 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Profile Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FuelOrange,
                        selectedTextColor = FuelOrange,
                        unselectedIconColor = PaleSlateText,
                        unselectedTextColor = PaleSlateText,
                        indicatorColor = BorderSlate
                    )
                )
            }
        },
        floatingActionButton = {
            if (activeTab == 0 || activeTab == 1) {
                FloatingActionButton(
                    onClick = {
                        if (activeTab == 0) showAddMealDialog = true
                        else if (activeTab == 1) showAddWorkoutDialog = true
                    },
                    containerColor = FuelOrange,
                    contentColor = CleanWhite
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add log"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkSlateBg)
        ) {
            // High-Impact Banner Summary (Calories & Protein budget)
            SummaryBannerCard(
                totalCaloriesConsumed = totalCaloriesConsumed,
                totalCaloriesBurned = totalCaloriesBurned,
                targetCalories = targetCalories,
                totalProtein = totalProtein,
                targetProtein = targetProtein,
                totalWaterMl = totalWaterMl
            )

            HorizontalDivider(color = BorderSlate, thickness = 1.dp)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (activeTab) {
                    0 -> NutritionTabContent(
                        logs = nutritionLogs,
                        waterLogs = waterLogs,
                        onDeleteMeal = { id -> viewModel.deleteMealLog(id) },
                        onAddWater = { amount -> viewModel.addWaterLog(amount) },
                        onDeleteWater = { id -> viewModel.deleteWaterLog(id) }
                    )
                    1 -> WorkoutTabContent(
                        logs = workoutLogs,
                        cardioLogs = cardioLogs,
                        onDeleteWorkout = { id -> viewModel.deleteExerciseLog(id) },
                        onDeleteCardio = { id -> viewModel.deleteCardioLog(id) },
                        onAddCardioClick = { showAddCardioDialog = true }
                    )
                    2 -> ScannerTabContent(
                        viewModel = viewModel
                    )
                    3 -> SettingsTabContent(
                        user = user,
                        onSave = { name, cals, prot, wt ->
                            viewModel.updateUserProfile(name, cals, prot, wt)
                        }
                    )
                }
            }
        }
    }

    // Modal Forms
    if (showAddMealDialog) {
        MealLogDialog(
            onDismiss = { showAddMealDialog = false },
            onConfirm = { name, cal, prot, carb, fat ->
                viewModel.addMealLog(name, cal, prot, carb, fat)
                showAddMealDialog = false
            }
        )
    }

    if (showAddWorkoutDialog) {
        WorkoutLogDialog(
            onDismiss = { showAddWorkoutDialog = false },
            onConfirm = { name, sets, reps, wt ->
                viewModel.addExerciseLog(name, sets, reps, wt)
                showAddWorkoutDialog = false
            }
        )
    }

    if (showAddCardioDialog) {
        CardioLogDialog(
            onDismiss = { showAddCardioDialog = false },
            onConfirm = { activityType, intensity, durationMinutes ->
                viewModel.addCardioLog(activityType, intensity, durationMinutes)
                showAddCardioDialog = false
            }
        )
    }
}

// ==========================================
// METRICS BANNER CARD
// ==========================================
@Composable
fun SummaryBannerCard(
    totalCaloriesConsumed: Int,
    totalCaloriesBurned: Int,
    targetCalories: Int,
    totalProtein: Int,
    targetProtein: Int,
    totalWaterMl: Int
) {
    val netCalories = totalCaloriesConsumed - totalCaloriesBurned
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BorderSlate)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "METRIC STATUS CENTER",
                    style = Typography.labelMedium,
                    color = PaleSlateText,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Water Tracked",
                        tint = AccentTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Water: ${totalWaterMl}ml / 3000ml",
                        style = Typography.labelSmall,
                        color = CleanWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.1f)) {
                    Text(
                        text = "NET ENERGY BALANCE",
                        style = Typography.labelSmall,
                        color = PaleSlateText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$netCalories",
                            style = Typography.headlineMedium,
                            color = if (netCalories <= targetCalories) FuelOrange else IronCoreRed,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "/ $targetCalories kcal",
                            style = Typography.bodyMedium,
                            color = PaleSlateText,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text("Diet Input", style = Typography.labelSmall, color = PaleSlateText, fontSize = 9.sp)
                            Text("${totalCaloriesConsumed} kcal", style = Typography.bodySmall, color = FuelOrange, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Active Burn", style = Typography.labelSmall, color = PaleSlateText, fontSize = 9.sp)
                            Text("${totalCaloriesBurned} kcal", style = Typography.bodySmall, color = IronCoreRed, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val netProgress = (netCalories.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = netProgress,
                        color = FuelOrange,
                        trackColor = BorderSlate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(modifier = Modifier.weight(0.9f)) {
                    Text(
                        text = "ANABOLIC PROTEIN",
                        style = Typography.labelSmall,
                        color = PaleSlateText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${totalProtein}g",
                            style = Typography.headlineMedium,
                            color = AccentTeal,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "/ ${targetProtein}g",
                            style = Typography.bodyMedium,
                            color = PaleSlateText,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val protProgress = (totalProtein.toFloat() / targetProtein.toFloat()).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = protProgress,
                        color = AccentTeal,
                        trackColor = BorderSlate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}

// ==========================================
// TABS CONTENT
// ==========================================

// --- NUTRITION DIET LOG ---
@Composable
fun NutritionTabContent(
    logs: List<NutritionLog>,
    waterLogs: List<WaterLog>,
    onDeleteMeal: (Int) -> Unit,
    onAddWater: (Int) -> Unit,
    onDeleteWater: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                border = BorderStroke(1.dp, BorderSlate)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentTeal.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Info, contentDescription = "Water Info", tint = AccentTeal, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "FLUID HYDRATION TRACKER",
                                    style = Typography.labelMedium,
                                    color = PaleSlateText,
                                    fontWeight = FontWeight.Bold
                                )
                                val currentWater = waterLogs.sumOf { it.amountMl }
                                Text(
                                    text = "$currentWater / 3000 ml",
                                    style = Typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CleanWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { onAddWater(250) },
                            colors = ButtonDefaults.buttonColors(containerColor = BorderSlate),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+250ml Cup", style = Typography.labelSmall, color = AccentTeal, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onAddWater(500) },
                            colors = ButtonDefaults.buttonColors(containerColor = BorderSlate),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+500ml Bottle", style = Typography.labelSmall, color = AccentTeal, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onAddWater(750) },
                            colors = ButtonDefaults.buttonColors(containerColor = BorderSlate),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+750ml Shaker", style = Typography.labelSmall, color = AccentTeal, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (waterLogs.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = BorderSlate, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Today's Loged Fluid Entries:",
                            style = Typography.labelSmall,
                            color = PaleSlateText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        waterLogs.forEach { waterLog ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "💧 Water log: ${waterLog.amountMl} ml",
                                    style = Typography.bodySmall,
                                    color = CleanWhite
                                )
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Delete log",
                                    tint = IronCoreRed,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { onDeleteWater(waterLog.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "TODAY'S FUEL CONSUMED",
                style = Typography.labelMedium,
                color = PaleSlateText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        if (logs.isEmpty()) {
            item {
                EmptyLogPlaceholder(
                    message = "No meals logged yet.\nClick '+' to load your fuel!",
                    icon = Icons.Default.List
                )
            }
        } else {
            items(logs) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                    border = BorderStroke(1.dp, BorderSlate)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = log.itemName,
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = CleanWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                MacroIndicator(label = "kcal", value = log.calories.toString(), color = FuelOrange)
                                MacroIndicator(label = "P", value = "${log.proteinGrams}g", color = AccentTeal)
                                MacroIndicator(label = "C", value = "${log.carbsGrams}g", color = AmberGlow)
                                MacroIndicator(label = "F", value = "${log.fatGrams}g", color = IronCoreRed)
                            }
                        }

                        IconButton(onClick = { onDeleteMeal(log.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete log", tint = IronCoreRed.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}

// --- WORKOUT ATHLETIC LOG ---
@Composable
fun WorkoutTabContent(
    logs: List<WorkoutLog>,
    cardioLogs: List<CardioLog>,
    onDeleteWorkout: (Int) -> Unit,
    onDeleteCardio: (Int) -> Unit,
    onAddCardioClick: () -> Unit
) {
    var subTab by remember { mutableStateOf(0) } // 0 = Lifts, 1 = Cardio

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(DeepCarbonSurface, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (subTab == 0) FuelOrange else Color.Transparent)
                    .clickable { subTab = 0 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "STRENGTH LIFTS",
                    style = Typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (subTab == 0) CleanWhite else PaleSlateText
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (subTab == 1) FuelOrange else Color.Transparent)
                    .clickable { subTab = 1 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CARDIO ENDURANCE",
                    style = Typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (subTab == 1) CleanWhite else PaleSlateText
                )
            }
        }

        if (subTab == 0) {
            if (logs.isEmpty()) {
                EmptyLogPlaceholder(
                    message = "No anaerobic lifts logged today.\nClick '+' to log weights lifted!",
                    icon = Icons.Default.ThumbUp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(logs) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                            border = BorderStroke(1.dp, BorderSlate)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(IronCoreRed.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = IronCoreRed)
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = log.exerciseName,
                                            style = Typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = CleanWhite
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${log.sets} sets x ${log.reps} reps @ ${log.weightKg} kg",
                                            style = Typography.bodyMedium,
                                            color = PaleSlateText,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                IconButton(onClick = { onDeleteWorkout(log.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete lift log", tint = IronCoreRed.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = onAddCardioClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = CleanWhite)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("LOG ACTIVE ENDURANCE (MET)", fontWeight = FontWeight.Bold, color = CleanWhite)
                }

                if (cardioLogs.isEmpty()) {
                    EmptyLogPlaceholder(
                        message = "No outdoor/aerobic cardio logs today.\nTrack aerobic efforts to balance net calories!",
                        icon = Icons.Default.Favorite
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(cardioLogs) { log ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                                border = BorderStroke(1.dp, BorderSlate)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(AccentTeal.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Favorite, contentDescription = null, tint = AccentTeal)
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = "${log.activityType} (${log.intensity})",
                                                style = Typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = CleanWhite
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "⏱️ ${log.durationMinutes} mins | 🔥 ${log.caloriesBurned} kcal burned",
                                                style = Typography.bodyMedium,
                                                color = PaleSlateText,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    IconButton(onClick = { onDeleteCardio(log.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete cardio log", tint = IronCoreRed.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- INTELLIGENT AI SCANNER TAB ---
@Composable
fun ScannerTabContent(
    viewModel: IronFuelViewModel
) {
    var rawInputText by remember { mutableStateOf("") }
    val isScannerLoading by viewModel.isScannerLoading.collectAsState()
    val scanResult by viewModel.scannedFoodResult.collectAsState()
    val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "GEMINI HEALTH SCANNER (AI)",
                    fontWeight = FontWeight.Bold,
                    color = CleanWhite,
                    style = Typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Describe your meal below in plain text, and IronFuel's intelligence layer will compute individual nutritional specifications.",
                    color = PaleSlateText,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = rawInputText,
                    onValueChange = { rawInputText = it },
                    placeholder = { Text("e.g. 2 fried eggs, a small handful of fresh almonds, and double cups of whole fat milk ...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate,
                        focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.scanFoodInput(rawInputText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FuelOrange),
                    shape = RoundedCornerShape(8.dp),
                    enabled = rawInputText.isNotBlank() && !isScannerLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isScannerLoading) {
                        CircularProgressIndicator(color = CleanWhite, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("COMPUTING WITH AI...", color = CleanWhite)
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null, tint = CleanWhite, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ANALYZE MEAL", fontWeight = FontWeight.Bold, color = CleanWhite)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Presentation Scanned Result Card
        if (scanResult != null) {
            val resObj = scanResult!!
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface),
                border = BorderStroke(1.dp, AccentTeal)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AI SPECTRAL ANALYSIS",
                            style = Typography.labelMedium,
                            color = AccentTeal,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.Star, contentDescription = null, tint = AccentTeal, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = resObj.itemName,
                        style = Typography.titleLarge,
                        color = CleanWhite,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Estimated stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MacroScannerChip("Calories", "${resObj.calories} kcal", FuelOrange)
                        MacroScannerChip("Protein", "${resObj.proteinGrams}g", AccentTeal)
                        MacroScannerChip("Carbs", "${resObj.carbsGrams}g", AmberGlow)
                        MacroScannerChip("Fat", "${resObj.fatGrams}g", IronCoreRed)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.addMealLog(resObj.itemName, resObj.calories, resObj.proteinGrams, resObj.carbsGrams, resObj.fatGrams)
                            rawInputText = ""
                            viewModel.scanFoodInput("") // Clear scan results after logging
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("LOG TO TODAY'S DIET", fontWeight = FontWeight.Bold, color = CleanWhite)
                    }
                }
            }
        }
    }
}

// --- SETTINGS / PROFILE TAB ---
@Composable
fun SettingsTabContent(
    user: com.example.data.UserEntity?,
    onSave: (String, Int, Int, Double) -> Unit
) {
    if (user == null) return

    var name by remember { mutableStateOf(user.displayName) }
    var cals by remember { mutableStateOf(user.dailyCalorieTarget.toString()) }
    var prot by remember { mutableStateOf(user.dailyProteinTarget.toString()) }
    var wt by remember { mutableStateOf(user.bodyWeightKg.toString()) }

    var saveSuccessfulMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("ATHLETE SETTINGS", fontWeight = FontWeight.Bold, color = CleanWhite, style = Typography.titleLarge)

        Card(colors = CardDefaults.cardColors(containerColor = DeepCarbonSurface)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Display Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                        focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cals,
                    onValueChange = { cals = it },
                    label = { Text("Daily Calorie Target (kcal)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                        focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = prot,
                    onValueChange = { prot = it },
                    label = { Text("Daily Protein Target (grams)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                        focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = wt,
                    onValueChange = { wt = it },
                    label = { Text("Profile Weight (kg)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite,
                        focusedBorderColor = FuelOrange, unfocusedBorderColor = BorderSlate
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val caloriesInt = cals.toIntOrNull() ?: 2800
                        val proteinInt = prot.toIntOrNull() ?: 180
                        val weightDouble = wt.toDoubleOrNull() ?: 80.0
                        onSave(name, caloriesInt, proteinInt, weightDouble)
                        saveSuccessfulMsg = "Profile targets saved successfully."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FuelOrange),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SAVE ADJUSTMENTS", fontWeight = FontWeight.Bold, color = CleanWhite)
                }

                if (saveSuccessfulMsg != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = saveSuccessfulMsg!!,
                        color = AccentTeal,
                        style = Typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ==========================================
// UTILITY CHIPS & PLACEHOLDERS
// ==========================================

@Composable
fun MacroIndicator(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$label: ", style = Typography.labelMedium, color = color, fontWeight = FontWeight.Bold)
        Text(text = value, style = Typography.labelMedium, color = CleanWhite)
    }
}

@Composable
fun MacroScannerChip(label: String, value: String, color: Color) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(BorderSlate)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label.uppercase(), fontSize = 10.sp, color = PaleSlateText, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 13.sp, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EmptyLogPlaceholder(message: String, icon: ImageVector) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PaleSlateText.copy(alpha = 0.4f),
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = PaleSlateText,
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

// ==========================================
// MODAL FORMS DIALOGS
// ==========================================

@Composable
fun MealLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, cal: Int, prot: Int, carb: Int, fat: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var cal by remember { mutableStateOf("") }
    var prot by remember { mutableStateOf("") }
    var carb by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Food Intake", color = CleanWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Food Item / Meal Name") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = cal,
                        onValueChange = { cal = it },
                        label = { Text("kcal") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    OutlinedTextField(
                        value = prot,
                        onValueChange = { prot = it },
                        label = { Text("Protein (g)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = carb,
                        onValueChange = { carb = it },
                        label = { Text("Carbohydrates (g)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("Fats (g)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val calories = cal.toIntOrNull() ?: 0
                    val proteinGrams = prot.toIntOrNull() ?: 0
                    val carbsGrams = carb.toIntOrNull() ?: 0
                    val fatGrams = fat.toIntOrNull() ?: 0
                    onConfirm(name, calories, proteinGrams, carbsGrams, fatGrams)
                },
                colors = ButtonDefaults.buttonColors(containerColor = FuelOrange)
            ) {
                Text("LOG FUEL", color = CleanWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = PaleSlateText)
            }
        },
        containerColor = DeepCarbonSurface
    )
}

@Composable
fun WorkoutLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, sets: Int, reps: Int, weight: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Workout Lift", color = CleanWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Exercise Name (e.g. Bench Press)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = sets,
                        onValueChange = { sets = it },
                        label = { Text("Sets") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    OutlinedTextField(
                        value = reps,
                        onValueChange = { reps = it },
                        label = { Text("Reps") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val s = sets.toIntOrNull() ?: 1
                    val r = reps.toIntOrNull() ?: 1
                    val w = weight.toDoubleOrNull() ?: 0.0
                    onConfirm(name, s, r, w)
                },
                colors = ButtonDefaults.buttonColors(containerColor = FuelOrange)
            ) {
                Text("LOG LIFT", color = CleanWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = PaleSlateText)
            }
        },
        containerColor = DeepCarbonSurface
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardioLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (activityType: String, intensity: String, durationMinutes: Int) -> Unit
) {
    val activities = listOf("Running", "Cycling", "Swimming", "Walking / Rucking", "HIIT")
    val intensities = listOf("Low", "Moderate", "High")

    var selectedActivity by remember { mutableStateOf(activities[0]) }
    var selectedIntensity by remember { mutableStateOf(intensities[1]) }
    var durationText by remember { mutableStateOf("") }

    var activityMenuExpanded by remember { mutableStateOf(false) }
    var intensityMenuExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Cardio Activity (MET Based)", color = CleanWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Select Activity:", color = PaleSlateText, style = Typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = activityMenuExpanded,
                    onExpandedChange = { activityMenuExpanded = !activityMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedActivity,
                        onValueChange = {},
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = activityMenuExpanded,
                        onDismissRequest = { activityMenuExpanded = false }
                    ) {
                        activities.forEach { act ->
                            DropdownMenuItem(
                                text = { Text(act) },
                                onClick = {
                                    selectedActivity = act
                                    activityMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Text("Select Intensity:", color = PaleSlateText, style = Typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = intensityMenuExpanded,
                    onExpandedChange = { intensityMenuExpanded = !intensityMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedIntensity,
                        onValueChange = {},
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = intensityMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = intensityMenuExpanded,
                        onDismissRequest = { intensityMenuExpanded = false }
                    ) {
                        intensities.forEach { intensity ->
                            DropdownMenuItem(
                                text = { Text(intensity) },
                                onClick = {
                                    selectedIntensity = intensity
                                    intensityMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it },
                    label = { Text("Duration (minutes)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = CleanWhite, unfocusedTextColor = CleanWhite),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val minutes = durationText.toIntOrNull() ?: 1
                    onConfirm(selectedActivity, selectedIntensity, minutes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = FuelOrange),
                enabled = durationText.isNotBlank() && durationText.toIntOrNull() != null
            ) {
                Text("LOG CARDIO", color = CleanWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = PaleSlateText)
            }
        },
        containerColor = DeepCarbonSurface
    )
}
