package com.example.xmarket.models

data class SignUpResponseModel(
    val data: SignUpData,
    val message: String,
    val status: Boolean
)

data class SignUpData(
    val email: String,
    val id: Int,
    val name: String
)