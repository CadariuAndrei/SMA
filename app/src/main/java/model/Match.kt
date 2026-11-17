package com.example.courtconnect.model

data class Match(
    val id: Int? = null,
    val creatorName: String,
    val clubName: String,
    val ntrpLevel: Double,
    val matchType: String,
    val date: String
)