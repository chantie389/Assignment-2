package com.example.a2imad
// data class
data class Question(
    val statement: String,
    val isHack: Boolean,// true = Hack, false = Myth
    val explanation: String
)