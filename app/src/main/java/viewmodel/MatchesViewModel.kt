package com.example.courtconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courtconnect.model.Match
import com.example.courtconnect.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MatchesViewModel : ViewModel() {

    // Aici stocăm lista de meciuri. Interfața va 'asculta' automat orice modificare adusă aici.
    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    init {
        // Când se deschide aplicația, aducem automat meciurile de pe server (READ)
        fetchMatches()
    }

    // READ
    fun fetchMatches() {
        viewModelScope.launch {
            try {
                _matches.value = RetrofitClient.api.getMatches()
            } catch (e: Exception) {
                e.printStackTrace()
                // Aici am putea afișa o eroare în UI dacă pică serverul
            }
        }
    }

    // CREATE
    fun addMatch(match: Match) {
        viewModelScope.launch {
            try {
                val newMatch = RetrofitClient.api.createMatch(match)
                _matches.value = _matches.value + newMatch
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // UPDATE
    fun updateMatch(id: Int, updatedMatch: Match) {
        viewModelScope.launch {
            try {
                val matchFromServer = RetrofitClient.api.updateMatch(id, updatedMatch)
                _matches.value = _matches.value.map {
                    if (it.id == id) matchFromServer else it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // DELETE
    fun deleteMatch(id: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.api.deleteMatch(id)
                _matches.value = _matches.value.filter { it.id != id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}