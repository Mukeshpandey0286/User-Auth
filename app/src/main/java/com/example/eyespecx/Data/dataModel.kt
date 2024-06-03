package com.example.eyespecx.Data

data class dataModel(
    val name: String,
    val email: String,
    val password: String
)

data class loginUser(
    val email: String,
    val password: String
)
