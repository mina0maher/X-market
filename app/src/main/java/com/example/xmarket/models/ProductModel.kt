package com.example.xmarket.models


data class ProductModel(
    val data: ProductData,
    val message: String,
    val status: Boolean
)

data class ProductData(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val quantity: Int,
    val restaurant_id: Int
)