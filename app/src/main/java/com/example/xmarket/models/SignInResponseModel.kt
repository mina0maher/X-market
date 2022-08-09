package com.example.xmarket.models


data class SignInResponseModel(
    val data: SignInData,
    val message: String,
    val status: Boolean,
    val token: String
)
data class SignInData(
    val email: String,
    val id: Int,
    val name: String
)