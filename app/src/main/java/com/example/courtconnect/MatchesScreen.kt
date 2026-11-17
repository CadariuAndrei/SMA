package com.example.courtconnect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.courtconnect.model.Match
import com.example.courtconnect.viewmodel.MatchesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(viewModel: MatchesViewModel) {
    // Ascultăm lista de meciuri din ViewModel
    val matches by viewModel.matches.collectAsState()

    // Stări pentru dialogul de Adăugare/Editare
    var showDialog by remember { mutableStateOf(false) }
    var editingMatch by remember { mutableStateOf<Match?>(null) }

    // Stări pentru câmpurile din formular
    var creatorName by remember { mutableStateOf("") }
    var clubName by remember { mutableStateOf("") }
    var ntrpLevel by remember { mutableStateOf("") }
    var matchType by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    // Funcție pentru a deschide formularul
    fun openForm(match: Match? = null) {
        editingMatch = match
        creatorName = match?.creatorName ?: ""
        clubName = match?.clubName ?: ""
        ntrpLevel = match?.ntrpLevel?.toString() ?: ""
        matchType = match?.matchType ?: ""
        date = match?.date ?: ""
        showDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CourtConnect - Meciuri Deschise") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openForm(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Adaugă Meci")
            }
        }
    ) { paddingValues ->
        // Lista de meciuri (Read)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(matches) { match ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${match.matchType} la ${match.clubName}", fontWeight = FontWeight.Bold)
                            Text(text = "Organizator: ${match.creatorName}")
                            Text(text = "Nivel NTRP: ${match.ntrpLevel} | Data: ${match.date}")
                        }
                        Row {
                            // Buton Editare (Update)
                            IconButton(onClick = { openForm(match) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editează", tint = MaterialTheme.colorScheme.primary)
                            }
                            // Buton Ștergere (Delete)
                            IconButton(onClick = { match.id?.let { viewModel.deleteMatch(it) } }) {
                                Icon(Icons.Default.Delete, contentDescription = "Șterge", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        // Dialogul (Pop-up) pentru Creare/Update
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingMatch == null) "Meci Nou" else "Editează Meci") },
                text = {
                    Column {
                        OutlinedTextField(value = creatorName, onValueChange = { creatorName = it }, label = { Text("Nume Organizator") })
                        OutlinedTextField(value = clubName, onValueChange = { clubName = it }, label = { Text("Club (ex: CS Dyadora)") })
                        OutlinedTextField(value = ntrpLevel, onValueChange = { ntrpLevel = it }, label = { Text("NTRP (ex: 3.5)") })
                        OutlinedTextField(value = matchType, onValueChange = { matchType = it }, label = { Text("Tip (Simplu/Dublu)") })
                        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Data (ex: Azi, 18:00)") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val ntrp = ntrpLevel.toDoubleOrNull() ?: 1.0
                        val newMatch = Match(
                            id = editingMatch?.id,
                            creatorName = creatorName,
                            clubName = clubName,
                            ntrpLevel = ntrp,
                            matchType = matchType,
                            date = date
                        )
                        if (editingMatch == null) {
                            viewModel.addMatch(newMatch) // Create
                        } else {
                            newMatch.id?.let { viewModel.updateMatch(it, newMatch) } // Update
                        }
                        showDialog = false
                    }) {
                        Text("Salvează")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Anulează")
                    }
                }
            )
        }
    }
}