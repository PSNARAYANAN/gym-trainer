package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.CarbonBlack
import com.example.ui.theme.CharcoalOutline
import com.example.ui.theme.IronOrange
import com.example.ui.theme.SteelSurface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: IronFuelViewModel = viewModel()
                val currentScreen = viewModel.currentScreen

                // For Auth pages (Login/Signup), paint full bleed content without navigation bars
                if (currentScreen is Screen.Login || currentScreen is Screen.Signup) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (currentScreen) {
                            is Screen.Login -> LoginScreen(viewModel)
                            is Screen.Signup -> SignupScreen(viewModel)
                            else -> {}
                        }
                    }
                } else {
                    // Logged-in central journey scaffolding
                    Scaffold(
                        topBar = {
                            IronFuelTopBar()
                        },
                        bottomBar = {
                            AppBottomBar(current = currentScreen) { target ->
                                viewModel.navigateTo(target)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentScreen) {
                                is Screen.Home -> HomeScreen(viewModel)
                                is Screen.Exercises -> ExerciseLibraryScreen(viewModel)
                                is Screen.ScanLabel -> ScanLabelScreen(viewModel)
                                is Screen.ScanFood -> ScanFoodScreen(viewModel)
                                is Screen.Profile -> ProfileScreen(viewModel)
                                else -> {}
                            }

                            // Centered dialog insertions
                            AppDialogsContainer(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IronFuelTopBar() {
    Surface(
        color = CarbonBlack,
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(64.dp)
            .drawBehindOnlyBottomBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Circular profile mock icon
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SteelSurface)
                        .border(BorderStroke(1.dp, IronOrange), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = IronOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "IRONFUEL",
                    style = MaterialTheme.typography.titleLarge,
                    color = IronOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MilitaryTech,
                    contentDescription = "Achievements",
                    tint = IronOrange,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

// Custom bottom border on top bar representation
@Composable
fun Modifier.drawBehindOnlyBottomBorder(): Modifier {
    return this.drawBehind {
        val strokeWidth = 1.dp.toPx()
        val y = size.height - strokeWidth / 2
        drawLine(
            color = CharcoalOutline,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
    }
}
