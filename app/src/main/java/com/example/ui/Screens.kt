package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.ui.theme.*

// ---------------------------------------------------------------------
// LOGIN SCREEN
// ---------------------------------------------------------------------
@Composable
fun LoginScreen(viewModel: IronFuelViewModel) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDim)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(IronOrangeContainer.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.8f, size.height * 0.1f),
                        radius = size.width * 0.8f
                    )
                )
            }
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 450.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(IronOrangeContainer)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Fitness Center Icon",
                    tint = TextWhite,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "IRONFUEL",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = IronOrange,
                fontSize = 32.sp,
                letterSpacing = 2.sp
            )

            Text(
                text = "PRECISION PERFORMANCE",
                style = MaterialTheme.typography.labelLarge,
                color = WarmOnSurfaceVariant,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp),
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Log in to fuel your progress.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmOnSurfaceVariant
                    )

                    viewModel.authError?.let {
                        Text(
                            text = it,
                            color = ErrorText,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "EMAIL ADDRESS",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmOnSurfaceVariant,
                            fontSize = 11.sp
                        )
                        OutlinedTextField(
                            value = viewModel.emailInput,
                            onValueChange = { viewModel.emailInput = it },
                            placeholder = { Text("name@ironfuel.com", color = CarbonLight) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = "Mail Icon",
                                    tint = WarmOnSurfaceVariant
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = SteelSurfaceLowest,
                                unfocusedContainerColor = SteelSurfaceLowest
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "PASSWORD",
                                style = MaterialTheme.typography.labelLarge,
                                color = WarmOnSurfaceVariant,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "FORGOT?",
                                style = MaterialTheme.typography.labelLarge,
                                color = IronOrange,
                                fontSize = 11.sp,
                                modifier = Modifier.clickable { }
                            )
                        }
                        OutlinedTextField(
                            value = viewModel.passwordInput,
                            onValueChange = { viewModel.passwordInput = it },
                            placeholder = { Text("••••••••", color = CarbonLight) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock Icon",
                                    tint = WarmOnSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Visibility",
                                        tint = WarmOnSurfaceVariant
                                    )
                                }
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = SteelSurfaceLowest,
                                unfocusedContainerColor = SteelSurfaceLowest
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(
                        onClick = { viewModel.login() },
                        colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "LOGIN TO DASHBOARD",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Arrow right icon",
                                tint = TextWhite
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = CharcoalOutline)
                        Text(
                            text = "OR CONTINUE WITH",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 10.sp,
                            color = WarmOnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Divider(modifier = Modifier.weight(1f), color = CharcoalOutline)
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.emailInput = "alex@ironfuel.com"
                                viewModel.passwordInput = "123456"
                                viewModel.login()
                            },
                            border = BorderStroke(1.dp, CharcoalOutline),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("GOOGLE", style = MaterialTheme.typography.labelLarge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                        OutlinedButton(
                            onClick = {
                                viewModel.emailInput = "athlete@ironfuel.com"
                                viewModel.passwordInput = "123456"
                                viewModel.login()
                            },
                            border = BorderStroke(1.dp, CharcoalOutline),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("APPLE", style = MaterialTheme.typography.labelLarge, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmOnSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyLarge,
                    color = IronOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.navigateTo(Screen.Signup) }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ---------------------------------------------------------------------
// SIGNUP SCREEN
// ---------------------------------------------------------------------
@Composable
fun SignupScreen(viewModel: IronFuelViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDim)
            .drawBehind {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(FuelGreen.copy(alpha = 0.05f), Color.Transparent),
                        center = Offset(size.width * 0.1f, size.height * 0.8f),
                        radius = size.width * 0.7f
                    )
                )
            }
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 450.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Bolt decoration",
                    tint = IronOrange,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "IRONFUEL",
                    style = MaterialTheme.typography.headlineMedium,
                    color = IronOrange,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Text(
                text = "Join the Tribe",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Precision engineering for your physique.",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmOnSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    viewModel.authError?.let {
                        Text(
                            text = it,
                            color = ErrorText,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "FULL NAME",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmOnSurfaceVariant,
                            fontSize = 11.sp
                        )
                        OutlinedTextField(
                            value = viewModel.fullNameInput,
                            onValueChange = { viewModel.fullNameInput = it },
                            placeholder = { Text("John Doe", color = CarbonLight) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person icon",
                                    tint = WarmOnSurfaceVariant
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = SteelSurfaceLowest,
                                unfocusedContainerColor = SteelSurfaceLowest
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "EMAIL ADDRESS",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmOnSurfaceVariant,
                            fontSize = 11.sp
                        )
                        OutlinedTextField(
                            value = viewModel.emailInput,
                            onValueChange = { viewModel.emailInput = it },
                            placeholder = { Text("athlete@ironfuel.com", color = CarbonLight) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = "Mail icon",
                                    tint = WarmOnSurfaceVariant
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = SteelSurfaceLowest,
                                unfocusedContainerColor = SteelSurfaceLowest
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "PRIMARY OBJECTIVE",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmOnSurfaceVariant,
                            fontSize = 11.sp
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SteelSurfaceLowest, RoundedCornerShape(10.dp))
                                .border(BorderStroke(1.dp, CharcoalOutline), RoundedCornerShape(10.dp))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val activeLose = viewModel.selectedGoalInput == "LOSE_WEIGHT"
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (activeLose) IronOrangeContainer else Color.Transparent)
                                    .clickable { viewModel.selectedGoalInput = "LOSE_WEIGHT" }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = "Decrease icon",
                                    tint = if (activeLose) TextWhite else WarmOnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LOSE WEIGHT",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 11.sp,
                                    color = if (activeLose) TextWhite else WarmOnSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            val activeGain = viewModel.selectedGoalInput == "GAIN_MUSCLE"
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (activeGain) IronOrangeContainer else Color.Transparent)
                                    .clickable { viewModel.selectedGoalInput = "GAIN_MUSCLE" }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = "Dumbbell icon",
                                    tint = if (activeGain) TextWhite else WarmOnSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GAIN MUSCLE",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 11.sp,
                                    color = if (activeGain) TextWhite else WarmOnSurfaceVariant,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "PASSWORD",
                                style = MaterialTheme.typography.labelLarge,
                                color = WarmOnSurfaceVariant,
                                fontSize = 11.sp
                            )
                            OutlinedTextField(
                                value = viewModel.passwordInput,
                                onValueChange = { viewModel.passwordInput = it },
                                placeholder = { Text("••••••••", color = CarbonLight) },
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedBorderColor = IronOrange,
                                    unfocusedBorderColor = CharcoalOutline,
                                    focusedContainerColor = SteelSurfaceLowest,
                                    unfocusedContainerColor = SteelSurfaceLowest
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "CONFIRM PASSWORD",
                                style = MaterialTheme.typography.labelLarge,
                                color = WarmOnSurfaceVariant,
                                fontSize = 11.sp
                            )
                            OutlinedTextField(
                                value = viewModel.confirmPasswordInput,
                                onValueChange = { viewModel.confirmPasswordInput = it },
                                placeholder = { Text("••••••••", color = CarbonLight) },
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedBorderColor = IronOrange,
                                    unfocusedBorderColor = CharcoalOutline,
                                    focusedContainerColor = SteelSurfaceLowest,
                                    unfocusedContainerColor = SteelSurfaceLowest
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.signUp() },
                        colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "CREATE ACCOUNT",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Forward arrow",
                                tint = TextWhite
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WarmOnSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodyLarge,
                    color = IronOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.navigateTo(Screen.Login) }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// ---------------------------------------------------------------------
// APP BOTTOM NAVIGATION BAR
// ---------------------------------------------------------------------
@Composable
fun AppBottomBar(current: Screen, onNavigate: (Screen) -> Unit) {
    NavigationBar(
        containerColor = SteelSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavigationBarItem(
            selected = current is Screen.Home,
            onClick = { onNavigate(Screen.Home) },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBlack,
                selectedTextColor = FuelGreen,
                indicatorColor = FuelGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = current is Screen.Exercises,
            onClick = { onNavigate(Screen.Exercises) },
            icon = { Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = "Exercises") },
            label = { Text("Exercises", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBlack,
                selectedTextColor = FuelGreen,
                indicatorColor = FuelGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = current is Screen.ScanLabel || current is Screen.ScanFood,
            onClick = { onNavigate(Screen.ScanLabel) },
            icon = { Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "Scan") },
            label = { Text("Scan", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBlack,
                selectedTextColor = FuelGreen,
                indicatorColor = FuelGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )

        NavigationBarItem(
            selected = current is Screen.Profile,
            onClick = { onNavigate(Screen.Profile) },
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CarbonBlack,
                selectedTextColor = FuelGreen,
                indicatorColor = FuelGreen,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            )
        )
    }
}

// ---------------------------------------------------------------------
// CUSTOM PROGRESS RING CANVAS
// ---------------------------------------------------------------------
@Composable
fun CalorieCircularChart(consumedKcal: Int, targetKcal: Int) {
    val remaining = (targetKcal - consumedKcal).coerceAtLeast(0)
    val percentage = (consumedKcal.toFloat() / targetKcal.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier.size(170.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 10.dp.toPx()
            val innerSize = size.width - strokeWidth
            val rectSize = Size(innerSize, innerSize)
            val offset = strokeWidth / 2

            drawArc(
                color = SteelSurfaceHigh,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(offset, offset),
                size = rectSize,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = FuelGreen,
                startAngle = -90f,
                sweepAngle = percentage * 360f,
                useCenter = false,
                topLeft = Offset(offset, offset),
                size = rectSize,
                style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = remaining.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = TextWhite
            )
            Text(
                text = "KCAL LEFT",
                style = MaterialTheme.typography.labelLarge,
                color = TextWhite.copy(alpha = 0.5f),
                fontSize = 9.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------
// CUSTOM WEIGHT TREND CANVAS GRAPH
// ---------------------------------------------------------------------
@Composable
fun WeightTrendLineGraph(weightData: List<WeightRecord>) {
    val records = weightData.takeLast(7)
    if (records.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No weight records recorded.", color = WarmOnSurfaceVariant)
        }
        return
    }

    val maxWeight = (records.maxOfOrNull { it.weight } ?: 80.0f) + 1.0f
    val minWeight = (records.minOfOrNull { it.weight } ?: 70.0f) - 1.0f
    val weightDelta = (maxWeight - minWeight).coerceAtLeast(1.0f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(top = 10.dp, bottom = 4.dp)
    ) {
        val width = size.width
        val height = size.height
        val totalPoints = records.size

        val spacingX = width / (totalPoints - 1).coerceAtLeast(1)

        val points = records.mapIndexed { idx, item ->
            val x = idx * spacingX
            val ratio = (item.weight - minWeight) / weightDelta
            val y = height - (ratio * height)
            Offset(x, y)
        }

        if (points.isNotEmpty()) {
            val fillPath = Path().apply {
                moveTo(points.first().x, height)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, height)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(IronOrange.copy(alpha = 0.35f), Color.Transparent),
                    startY = 0f,
                    endY = height
                )
            )

            val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val controlX = (prev.x + curr.x) / 2
                    cubicTo(controlX, prev.y, controlX, curr.y, curr.x, curr.y)
                }
            }
            drawPath(
                path = strokePath,
                color = IronOrange,
                style = Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            points.forEachIndexed { i, offsetPoint ->
                val isLast = i == points.size - 1
                if (isLast) {
                    drawCircle(
                        color = IronOrange.copy(alpha = 0.4f),
                        radius = 8.dp.toPx(),
                        center = offsetPoint
                    )
                }
                drawCircle(
                    color = if (isLast) FuelGreen else IronOrange,
                    radius = 4.dp.toPx(),
                    center = offsetPoint
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// HOME SCREEN (DASHBOARD)
// ---------------------------------------------------------------------
@Composable
fun HomeScreen(viewModel: IronFuelViewModel) {
    val athleteState by viewModel.athlete.collectAsState()
    val mealsState by viewModel.meals.collectAsState()
    val weightsState by viewModel.weights.collectAsState()

    val currentAthlete = athleteState ?: Athlete()

    val targetKcal = currentAthlete.targetKcal
    val consumedKcal = mealsState.sumOf { it.calories }

    val consumedProtein = mealsState.sumOf { it.protein }
    val consumedCarbs = mealsState.sumOf { it.carbs }
    val consumedFat = mealsState.sumOf { it.fat }

    val targetProtein = if (currentAthlete.objective == "GAIN_MUSCLE") 180 else 130
    val targetCarbs = if (currentAthlete.objective == "GAIN_MUSCLE") 350 else 220
    val targetFat = if (currentAthlete.objective == "GAIN_MUSCLE") 80 else 60

    val currentWeight = weightsState.lastOrNull()?.weight ?: 76.5f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDim)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "WELCOME BACK",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmOnSurfaceVariant,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "Hello, ${currentAthlete.fullName.split(" ").first()}!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(IronOrangeContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentAthlete.objective.replace("_", " "),
                                style = MaterialTheme.typography.labelLarge,
                                color = TextWhite,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${currentAthlete.streak} Day Strain",
                        style = MaterialTheme.typography.titleLarge,
                        color = IronOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "STREAK \ud83d\udd25",
                        style = MaterialTheme.typography.labelLarge,
                        color = FuelGreen,
                        fontSize = 10.sp
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawLine(
                            color = IronOrange,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CalorieCircularChart(consumedKcal = consumedKcal, targetKcal = targetKcal)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Daily Fuel Intake",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Consumed", color = WarmOnSurfaceVariant, fontSize = 13.sp)
                                Text("$consumedKcal kcal", style = MaterialTheme.typography.labelSmall, color = IronOrange, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Diet Target", color = WarmOnSurfaceVariant, fontSize = 13.sp)
                                Text("$targetKcal kcal", style = MaterialTheme.typography.labelSmall, color = TextWhite, fontSize = 13.sp)
                            }
                        }

                        val ratioFraction = (consumedKcal.toFloat() / targetKcal.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = ratioFraction,
                            trackColor = SteelSurfaceHigh,
                            color = IronOrange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "MACRONUTRIENTS",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmOnSurfaceVariant,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )

                    MacroRowBar(
                        label = "Protein",
                        color = IronOrange,
                        current = consumedProtein,
                        target = targetProtein
                    )

                    MacroRowBar(
                        label = "Carbolite",
                        color = FuelGreen,
                        current = consumedCarbs,
                        target = targetCarbs
                    )

                    MacroRowBar(
                        label = "Fats",
                        color = MutedSlate,
                        current = consumedFat,
                        target = targetFat
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = IronOrangeContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(Screen.Exercises) }
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NEXT SUGGESTED SESSION",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextWhite.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "Push Day (Chest/Shoulders/Triceps)",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Exercises right",
                        tint = TextWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "QUICK ATHLETE ACTIONS",
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmOnSurfaceVariant,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    HomeActionKey(
                        icon = Icons.Default.Restaurant,
                        label = "Log Meal",
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.showAddMealDialog = true
                    }
                    HomeActionKey(
                        icon = Icons.Default.PhotoCamera,
                        label = "Scan Food",
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.navigateTo(Screen.ScanFood)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    HomeActionKey(
                        icon = Icons.Default.QrCodeScanner,
                        label = "Scan Label",
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.navigateTo(Screen.ScanLabel)
                    }
                    HomeActionKey(
                        icon = Icons.Default.FitnessCenter,
                        label = "Exercises",
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.navigateTo(Screen.Exercises)
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Weight Indices Trend",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "+1.5kg logged this week",
                                style = MaterialTheme.typography.bodySmall,
                                color = FuelGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.showAddWeightDialog = true }
                        ) {
                            Text(
                                text = "$currentWeight kg",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Add Weight Icon",
                                tint = IronOrange,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    WeightTrendLineGraph(weightData = weightsState)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val lbls = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
                        lbls.forEachIndexed { i, l ->
                            Text(
                                text = l,
                                fontSize = 10.sp,
                                color = if (i == 6) IronOrange else WarmOnSurfaceVariant,
                                fontWeight = if (i == 6) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S FUEL DIARY",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmOnSurfaceVariant,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "CLEAR ALL",
                            style = MaterialTheme.typography.labelLarge,
                            color = ErrorText,
                            fontSize = 10.sp,
                            modifier = Modifier.clickable { viewModel.clearAllMeals() }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (mealsState.isEmpty()) {
                        Text(
                            text = "No calorie entries logged yet today.",
                            color = WarmOnSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    } else {
                        mealsState.forEach { logged ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = "Meal Indicator",
                                        tint = FuelGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = logged.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${logged.portion} | P: ${logged.protein}g C: ${logged.carbs}g F: ${logged.fat}g",
                                            fontSize = 12.sp,
                                            color = WarmOnSurfaceVariant
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "+${logged.calories} kcal",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = IronOrange,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete item",
                                        tint = ErrorText,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clickable { viewModel.deleteMeal(logged) }
                                    )
                                }
                            }
                            Divider(color = CharcoalOutline.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

// ---------------------------------------------------------------------
// HELPER COMPONENTS
// ---------------------------------------------------------------------
@Composable
fun MacroRowBar(label: String, color: Color, current: Int, target: Int) {
    val fractionRatio = (current.toFloat() / target.toFloat()).coerceIn(0f, 1f)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextWhite)
            }

            Text(
                text = "$current / ${target}g",
                style = MaterialTheme.typography.labelSmall,
                color = WarmOnSurfaceVariant,
                fontSize = 13.sp
            )
        }

        LinearProgressIndicator(
            progress = fractionRatio,
            trackColor = SteelSurfaceHigh,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun HomeActionKey(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SteelSurface),
        border = BorderStroke(1.dp, CharcoalOutline),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(72.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = IronOrange,
                modifier = Modifier.size(26.dp)
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

// ---------------------------------------------------------------------
// SCAN LABEL SCREEN
// ---------------------------------------------------------------------
@Composable
fun ScanLabelScreen(viewModel: IronFuelViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "Ocr transition")
    val borderOffset by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanner line bounce"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDim)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Food Label Scanner",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Position a nutritional facts label inside the AI viewport.",
                color = WarmOnSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SteelSurfaceLowest)
                    .border(BorderStroke(1.dp, CharcoalOutline), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Nutrition label graphic",
                        tint = CarbonLight,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Nutrition Facts\nServing Size 1 Bottle\nCalories 220\nProtein 25g\nCarbs 8g\nFat 2g",
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = CarbonLight,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val lineY = size.height * borderOffset
                    drawLine(
                        color = FuelGreen,
                        start = Offset(24.dp.toPx(), lineY),
                        end = Offset(size.width - 24.dp.toPx(), lineY),
                        strokeWidth = 3.dp.toPx()
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .size(16.dp)
                        .drawBehind {
                            drawLine(color = IronOrange, start = Offset.Zero, end = Offset(size.width, 0f), strokeWidth = 3.dp.toPx())
                            drawLine(color = IronOrange, start = Offset.Zero, end = Offset(0f, size.height), strokeWidth = 3.dp.toPx())
                        })
                    Box(modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(16.dp)
                        .drawBehind {
                            drawLine(color = IronOrange, start = Offset(size.width, 0f), end = Offset.Zero, strokeWidth = 3.dp.toPx())
                            drawLine(color = IronOrange, start = Offset(size.width, 0f), end = Offset(size.width, size.height), strokeWidth = 3.dp.toPx())
                        })
                    Box(modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .size(16.dp)
                        .drawBehind {
                            drawLine(color = IronOrange, start = Offset(0f, size.height), end = Offset.Zero, strokeWidth = 3.dp.toPx())
                            drawLine(color = IronOrange, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = 3.dp.toPx())
                        })
                    Box(modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(16.dp)
                        .drawBehind {
                            drawLine(color = IronOrange, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = 3.dp.toPx())
                            drawLine(color = IronOrange, start = Offset(size.width, 0f), end = Offset(size.width, size.height), strokeWidth = 3.dp.toPx())
                        })
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(SteelSurface.copy(alpha = 0.9f))
                        .border(BorderStroke(1.dp, CharcoalOutline), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (viewModel.isScanningLabelActive) FuelGreen else IronOrange)
                        )
                        Text(
                            text = if (viewModel.isScanningLabelActive) "SCANNING REAL-TIME..." else "READY FOR VIEWPORT",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 10.sp,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.startScanLabelSimulation() },
                colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                shape = RoundedCornerShape(8.dp),
                enabled = !viewModel.isScanningLabelActive,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera Scan Button")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (viewModel.isScanningLabelActive) "SCANNING LABEL..." else "LOG REAL-TIME ANALYZER",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = viewModel.scanLabelComplete,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SteelSurface),
                    border = BorderStroke(1.dp, CharcoalOutline),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Precision Grade",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "High nutrient dense formula detected.",
                                    color = WarmOnSurfaceVariant,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .drawBehind {
                                        drawCircle(
                                            color = SteelSurfaceHigh,
                                            radius = size.width / 2,
                                            style = Stroke(width = 4.dp.toPx())
                                        )
                                        drawArc(
                                            color = FuelGreen,
                                            startAngle = -90f,
                                            sweepAngle = 290f,
                                            useCenter = false,
                                            style = Stroke(width = 4.dp.toPx())
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "82",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = FuelGreen
                                    )
                                    Text(
                                        text = "EXCELLENT",
                                        fontSize = 8.sp,
                                        color = WarmOnSurfaceVariant,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Divider(color = CharcoalOutline.copy(alpha = 0.5f))

                        Text(
                            text = "CRITICAL METRIC ANALYSIS",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 11.sp,
                            color = WarmOnSurfaceVariant,
                            letterSpacing = 1.sp
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            LogCritRow(
                                label = "High Protein Intake formula",
                                value = "+15",
                                isPostitive = true
                            )
                            LogCritRow(
                                label = "Extremely Low sugar density",
                                value = "+10",
                                isPostitive = true
                            )
                            LogCritRow(
                                label = "Negligible preservative additives",
                                value = "-5",
                                isPostitive = false
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(IronOrangeContainer.copy(alpha = 0.12f))
                                .border(BorderStroke(1.dp, IronOrangeContainer.copy(alpha = 0.25f)), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(IronOrangeContainer)
                                        .padding(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "Idea icon",
                                        tint = TextWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "Personal Athlete Advice",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = IronOrange,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Perfect for your Muscle Gain plan. The targeted amino-acid density promotes muscle synthesis after straining.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = WarmOnSurface,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { viewModel.logLabelMacrosState() },
                            colors = ButtonDefaults.buttonColors(containerColor = FuelGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = "SAVE & LOG MACROS DIARY",
                                style = MaterialTheme.typography.labelLarge,
                                color = CarbonBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.ScanFood) },
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Camera icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SWITCH TO AI MEAL SCANNER",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

// ---------------------------------------------------------------------
// SCAN FOOD SCREEN
// ---------------------------------------------------------------------
@Composable
fun ScanFoodScreen(viewModel: IronFuelViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "Laser scanner bounce")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanner overlay"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDim)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AI Vision Meal Analyzer",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Snap a photo of your workout plate for calorie mapping.",
                color = WarmOnSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SteelSurfaceLowest)
                    .border(BorderStroke(1.dp, CharcoalOutline), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    drawCircle(
                        color = SteelSurfaceHigh,
                        radius = w * 0.38f,
                        center = Offset(w / 2, h / 2)
                    )

                    val chickenPath = Path().apply {
                        moveTo(w * 0.45f, h * 0.35f)
                        quadraticTo(w * 0.65f, h * 0.38f, w * 0.62f, h * 0.6f)
                        quadraticTo(w * 0.5f, h * 0.68f, w * 0.35f, h * 0.55f)
                        quadraticTo(w * 0.33f, h * 0.4f, w * 0.45f, h * 0.35f)
                        close()
                    }
                    drawPath(
                        path = chickenPath,
                        color = MutedSlate
                    )

                    drawPath(
                        path = Path().apply {
                            moveTo(w * 0.42f, h * 0.4f)
                            lineTo(w * 0.52f, h * 0.37f)
                            moveTo(w * 0.44f, h * 0.47f)
                            lineTo(w * 0.56f, h * 0.43f)
                            moveTo(w * 0.46f, h * 0.55f)
                            lineTo(w * 0.58f, h * 0.51f)
                        },
                        color = CharcoalOutline,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    val quinoaPath = Path().apply {
                        moveTo(w * 0.65f, h * 0.45f)
                        quadraticTo(w * 0.72f, h * 0.4f, w * 0.78f, h * 0.48f)
                        quadraticTo(w * 0.74f, h * 0.63f, w * 0.63f, h * 0.58f)
                        close()
                    }
                    drawPath(
                        path = quinoaPath,
                        color = IronOrange
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .border(BorderStroke(1.5.dp, IronOrange.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val ly = size.height * laserY
                        drawLine(
                            color = IronOrange,
                            start = Offset(0f, ly),
                            end = Offset(size.width, ly),
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CarbonDim.copy(alpha = 0.7f))
                            .border(BorderStroke(1.dp, FuelGreen), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FilterCenterFocus,
                                contentDescription = "Focus mark",
                                tint = FuelGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PLATE DETECTED: 250g",
                                color = FuelGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(FuelGreen)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "AI VISION ACTIVE",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 9.sp,
                        color = CarbonBlack,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.startScanFoodSimulation() },
                colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                shape = RoundedCornerShape(8.dp),
                enabled = !viewModel.isScanningFoodActive,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Restaurant, contentDescription = "Camera fork icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (viewModel.isScanningFoodActive) "ANALYZING MEAL PLAN..." else "ANALYZE ACTIVE MEAL PLAN",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = viewModel.scanFoodComplete,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SteelSurface),
                    border = BorderStroke(1.dp, CharcoalOutline),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = viewModel.foodScanItemName,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Estimated Portion: ${viewModel.foodScanPortion}",
                                    color = IronOrange,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified tag",
                                tint = FuelGreen,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FoodScanMacroItem(
                                title = "CALORIES",
                                value = viewModel.foodScanKcal.toString(),
                                isMain = true,
                                modifier = Modifier.weight(1f)
                            )
                            FoodScanMacroItem(
                                title = "PROTEIN",
                                value = "${viewModel.foodScanProtein}g",
                                isMain = false,
                                modifier = Modifier.weight(1f)
                            )
                            FoodScanMacroItem(
                                title = "CARBS",
                                value = "${viewModel.foodScanCarbs}g",
                                isMain = false,
                                modifier = Modifier.weight(1f)
                            )
                            FoodScanMacroItem(
                                title = "FAT",
                                value = "${viewModel.foodScanFat}g",
                                isMain = false,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(FuelGreen.copy(alpha = 0.12f))
                                .border(BorderStroke(1.dp, FuelGreen.copy(alpha = 0.25f)), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success tick",
                                    tint = FuelGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Great protein source for Muscle Gain! High bio-availability, low fat density.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 13.sp,
                                    color = WarmOnSurface
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { viewModel.logFoodMacrosState() },
                                colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = "LOG TO DIARY",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            OutlinedButton(
                                onClick = { viewModel.scanFoodComplete = false },
                                border = BorderStroke(1.dp, CharcoalOutline),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = "RETAKE PHOTO",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = TextWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = { viewModel.navigateTo(Screen.ScanLabel) },
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = "Nutrition label")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SWITCH TO NUTRITION FACTS SCANNED",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

@Composable
fun FoodScanMacroItem(
    title: String,
    value: String,
    isMain: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SteelSurfaceHigh)
            .border(
                BorderStroke(1.dp, if (isMain) IronOrangeContainer.copy(alpha = 0.5f) else Color.Transparent),
                RoundedCornerShape(8.dp)
            )
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 8.sp,
                color = WarmOnSurfaceVariant,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = value,
                color = if (isMain) IronOrange else FuelGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------
// EXERCISE LIBRARY SCREEN
// ---------------------------------------------------------------------
@Composable
fun ExerciseLibraryScreen(viewModel: IronFuelViewModel) {
    val rawList by viewModel.exerciseList.collectAsState()

    val filteredList = remember(rawList, viewModel.exerciseSearchQuery, viewModel.selectedCategoryFilter) {
        rawList.filter { exercise ->
            val matchesCategory = viewModel.selectedCategoryFilter == "ALL" || exercise.category == viewModel.selectedCategoryFilter
            val matchesSearch = exercise.name.contains(viewModel.exerciseSearchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        containerColor = CarbonDim,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddExerciseDialog = true },
                containerColor = FuelGreen,
                contentColor = CarbonBlack,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Exercise FAB"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Exercise Library",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Discover structured lifts engineered for loading.",
                color = WarmOnSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.exerciseSearchQuery,
                onValueChange = { viewModel.exerciseSearchQuery = it },
                placeholder = { Text("Find an exercise...", color = CarbonLight) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = WarmOnSurfaceVariant
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedBorderColor = FuelGreen,
                    unfocusedBorderColor = CharcoalOutline,
                    focusedContainerColor = SteelSurface,
                    unfocusedContainerColor = SteelSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            val categories = listOf("ALL", "CHEST", "BACK", "LEGS", "SHOULDERS", "ARMS", "CORE")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = viewModel.selectedCategoryFilter == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) FuelGreen else SteelSurface)
                            .border(BorderStroke(1.dp, CharcoalOutline), RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectedCategoryFilter = cat }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) CarbonBlack else WarmOnSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No movements matches your criteria.",
                        color = WarmOnSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredList) { exercise ->
                        ExerciseCard(exercise = exercise) {
                            viewModel.toggleBookmark(exercise.id, exercise.isBookmarked)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(110.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseCard(exercise: ExerciseEntity, onBookmarkToggle: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SteelSurface),
        border = BorderStroke(1.dp, CharcoalOutline),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = FuelGreen,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = 4.dp.toPx()
                )
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SteelSurfaceHigh),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (exercise.category == "CHEST" || exercise.category == "SHOULDERS") Icons.Default.Accessibility
                        else Icons.Default.DirectionsRun,
                        contentDescription = "Lift display",
                        tint = IronOrange,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SteelSurfaceHigh)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = exercise.category,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = FuelGreen
                            )
                        }

                        if (exercise.isCustom) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(IronOrangeContainer.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "CUSTOM",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IronOrange
                                )
                            }
                        }
                    }

                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                IconButton(onClick = onBookmarkToggle) {
                    Icon(
                        imageVector = if (exercise.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark Stat indicator",
                        tint = if (exercise.isBookmarked) IronOrange else WarmOnSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CharcoalOutline.copy(alpha = 0.4f))
                        .border(BorderStroke(1.dp, CharcoalOutline.copy(alpha = 0.8f)), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = exercise.difficulty,
                        fontSize = 10.sp,
                        color = IronOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "Hardware equipment",
                        tint = WarmOnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = exercise.equipment,
                        fontSize = 11.sp,
                        color = WarmOnSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// PROFILE & GOALS SETTING SCREEN
// ---------------------------------------------------------------------
@Composable
fun ProfileScreen(viewModel: IronFuelViewModel) {
    val athleteState by viewModel.athlete.collectAsState()
    val rawAthlete = athleteState ?: Athlete()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CarbonDim)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Performance Profile",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Precision manage target parameters of your profile.",
                color = WarmOnSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SteelSurfaceHigh)
                            .border(BorderStroke(2.dp, FuelGreen), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User default profile",
                            tint = FuelGreen,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text(
                        text = rawAthlete.fullName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    Text(
                        text = rawAthlete.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmOnSurfaceVariant
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(IronOrangeContainer)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = rawAthlete.objective.replace("_", " "),
                            style = MaterialTheme.typography.labelLarge,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "ADJUST DIETARY PARAMETERS",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmOnSurfaceVariant,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Current Objective focus", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val activeLose = rawAthlete.objective == "LOSE_WEIGHT"
                            OutlinedButton(
                                onClick = { viewModel.updateObjective("LOSE_WEIGHT") },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (activeLose) IronOrangeContainer else Color.Transparent,
                                    contentColor = if (activeLose) TextWhite else WarmOnSurfaceVariant
                                ),
                                border = BorderStroke(1.dp, if (activeLose) Color.Transparent else CharcoalOutline),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("LOSE WEIGHT", style = MaterialTheme.typography.labelLarge, fontSize = 11.sp)
                            }

                            val activeGain = rawAthlete.objective == "GAIN_MUSCLE"
                            OutlinedButton(
                                onClick = { viewModel.updateObjective("GAIN_MUSCLE") },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (activeGain) IronOrangeContainer else Color.Transparent,
                                    contentColor = if (activeGain) TextWhite else WarmOnSurfaceVariant
                                ),
                                border = BorderStroke(1.dp, if (activeGain) Color.Transparent else CharcoalOutline),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("GAIN MUSCLE", style = MaterialTheme.typography.labelLarge, fontSize = 11.sp)
                            }
                        }
                    }

                    Divider(color = CharcoalOutline.copy(alpha = 0.5f))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Dietary Target Calories", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.updateTargetKcal(rawAthlete.targetKcal - 100) }) {
                                Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = "Decrement", tint = IronOrange)
                            }
                            Text(
                                text = "${rawAthlete.targetKcal} kcal",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            IconButton(onClick = { viewModel.updateTargetKcal(rawAthlete.targetKcal + 100) }) {
                                Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "Increment", tint = IronOrange)
                            }
                        }
                    }

                    Divider(color = CharcoalOutline.copy(alpha = 0.5f))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Strain Streak (Days)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.updateStreak(-1) }) {
                                Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = "Decrement streak", tint = FuelGreen)
                            }
                            Text(
                                text = "${rawAthlete.streak} Days",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            IconButton(onClick = { viewModel.updateStreak(1) }) {
                                Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "Increment streak", tint = FuelGreen)
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.buttonColors(containerColor = ErrorContainerRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Exit App Icon")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LOGOUT ACCOUNT",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

// ---------------------------------------------------------------------
// DYNAMIC APP DIALOGS
// ---------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDialogsContainer(viewModel: IronFuelViewModel) {
    if (viewModel.showAddMealDialog) {
        Dialog(onDismissRequest = { viewModel.showAddMealDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Log Custom Plate",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    OutlinedTextField(
                        value = viewModel.addMealName,
                        onValueChange = { viewModel.addMealName = it },
                        label = { Text("Meal Name", color = WarmOnSurfaceVariant) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = IronOrange,
                            unfocusedBorderColor = CharcoalOutline,
                            focusedContainerColor = CarbonDim,
                            unfocusedContainerColor = CarbonDim
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = viewModel.addMealKcal,
                        onValueChange = { viewModel.addMealKcal = it },
                        label = { Text("Calories (kcal)", color = WarmOnSurfaceVariant) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = IronOrange,
                            unfocusedBorderColor = CharcoalOutline,
                            focusedContainerColor = CarbonDim,
                            unfocusedContainerColor = CarbonDim
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = viewModel.addMealProtein,
                            onValueChange = { viewModel.addMealProtein = it },
                            label = { Text("Protein (g)", color = WarmOnSurfaceVariant) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = CarbonDim,
                                unfocusedContainerColor = CarbonDim
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = viewModel.addMealCarbs,
                            onValueChange = { viewModel.addMealCarbs = it },
                            label = { Text("Carbs (g)", color = WarmOnSurfaceVariant) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = CarbonDim,
                                unfocusedContainerColor = CarbonDim
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = viewModel.addMealFat,
                            onValueChange = { viewModel.addMealFat = it },
                            label = { Text("Fat (g)", color = WarmOnSurfaceVariant) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = IronOrange,
                                unfocusedBorderColor = CharcoalOutline,
                                focusedContainerColor = CarbonDim,
                                unfocusedContainerColor = CarbonDim
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = viewModel.addMealPortion,
                        onValueChange = { viewModel.addMealPortion = it },
                        label = { Text("Portion size (e.g. 250g)", color = WarmOnSurfaceVariant) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = IronOrange,
                            unfocusedBorderColor = CharcoalOutline,
                            focusedContainerColor = CarbonDim,
                            unfocusedContainerColor = CarbonDim
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.logCustomMeal() },
                            colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("SAVE DIARY", color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { viewModel.showAddMealDialog = false },
                            border = BorderStroke(1.dp, CharcoalOutline),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("CANCEL", color = TextWhite)
                        }
                    }
                }
            }
        }
    }

    if (viewModel.showAddWeightDialog) {
        Dialog(onDismissRequest = { viewModel.showAddWeightDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Log Body Weight",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    OutlinedTextField(
                        value = viewModel.addWeightValue,
                        onValueChange = { viewModel.addWeightValue = it },
                        label = { Text("Weight (kg)", color = WarmOnSurfaceVariant) },
                        placeholder = { Text("76.5", color = CarbonLight) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = IronOrange,
                            unfocusedBorderColor = CharcoalOutline,
                            focusedContainerColor = CarbonDim,
                            unfocusedContainerColor = CarbonDim
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.addNewWeightRecord() },
                            colors = ButtonDefaults.buttonColors(containerColor = FuelGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("LOG WEIGHT", color = CarbonBlack, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { viewModel.showAddWeightDialog = false },
                            border = BorderStroke(1.dp, CharcoalOutline),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("CANCEL", color = TextWhite)
                        }
                    }
                }
            }
        }
    }

    if (viewModel.showAddExerciseDialog) {
        Dialog(onDismissRequest = { viewModel.showAddExerciseDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CharcoalOutline),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Track Brand Move",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    OutlinedTextField(
                        value = viewModel.addExerciseName,
                        onValueChange = { viewModel.addExerciseName = it },
                        label = { Text("Movement Name", color = WarmOnSurfaceVariant) },
                        placeholder = { Text("Incline Dumbbell Flyes", color = CarbonLight) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = IronOrange,
                            unfocusedBorderColor = CharcoalOutline,
                            focusedContainerColor = CarbonDim,
                            unfocusedContainerColor = CarbonDim
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Select Muscle category", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val cats = listOf("CHEST", "BACK", "LEGS", "SHOULDERS", "ARMS", "CORE")
                        cats.forEach { ct ->
                            val s = viewModel.addExerciseCategory == ct
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (s) FuelGreen else SteelSurfaceHigh)
                                    .clickable { viewModel.addExerciseCategory = ct }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = ct,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (s) CarbonBlack else WarmOnSurfaceVariant
                                )
                            }
                        }
                    }

                    Text("Selection Difficulty level", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val diffs = listOf("BEGINNER", "INTERMEDIATE", "ADVANCED")
                        diffs.forEach { d ->
                            val active = viewModel.addExerciseDifficulty == d
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) IronOrangeContainer else SteelSurfaceHigh)
                                    .clickable { viewModel.addExerciseDifficulty = d }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = d,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                        }
                    }

                    Text("Equipment type", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val equips = listOf("BARBELL", "DUMBBELL", "BODYWEIGHT")
                        equips.forEach { eq ->
                            val active = viewModel.addExerciseEquipment == eq
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (active) FuelGreen else SteelSurfaceHigh)
                                    .clickable { viewModel.addExerciseEquipment = eq }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = eq,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (active) CarbonBlack else WarmOnSurfaceVariant
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.addNewExercise() },
                            colors = ButtonDefaults.buttonColors(containerColor = IronOrangeContainer),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("SAVE MOVE", color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { viewModel.showAddExerciseDialog = false },
                            border = BorderStroke(1.dp, CharcoalOutline),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("CANCEL", color = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogCritRow(label: String, value: String, isPostitive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SteelSurfaceHigh.copy(alpha = 0.5f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isPostitive) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (isPostitive) FuelGreen else ErrorText,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = label, color = TextWhite, style = MaterialTheme.typography.bodyMedium, fontSize = 13.sp)
        }
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = if (isPostitive) FuelGreen else ErrorText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
