package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.ui.IronFuelAppContent
import com.example.ui.IronFuelViewModel
import com.example.ui.theme.IronFuelTheme

class MainActivity : ComponentActivity() {
    private val viewModel: IronFuelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContent {
            IronFuelTheme {
                IronFuelAppContent(viewModel = viewModel)
            }
        }
    }
}
