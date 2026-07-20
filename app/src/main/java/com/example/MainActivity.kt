package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.SchoolMainUi
import com.example.ui.SchoolViewModel
import com.example.ui.SchoolViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retrieve database and repository from the Application class
        val app = application as SchoolApplication
        val repository = app.repository
        
        // Instantiate our custom ViewModel
        val viewModel: SchoolViewModel by viewModels {
            SchoolViewModelFactory(application, repository)
        }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Wrapping inside a simple container with proper window inset padding
                    SchoolMainUi(viewModel)
                }
            }
        }
    }
}
