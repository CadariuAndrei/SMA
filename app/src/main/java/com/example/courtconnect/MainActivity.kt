package com.example.courtconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel // Importul magic pentru Compose
import com.example.courtconnect.ui.theme.CourtConnectTheme
// Dacă apar erori la următoarele 2, dă click pe ele și apasă Alt + Enter -> Import
import com.example.courtconnect.viewmodel.MatchesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourtConnectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Inițializăm ViewModel-ul direct aici
                    val viewModel: MatchesViewModel = viewModel()
                    MatchesScreen(viewModel = viewModel)
                }
            }
        }
    }
}