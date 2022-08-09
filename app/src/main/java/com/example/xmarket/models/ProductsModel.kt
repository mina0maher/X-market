package com.example.xmarket.models

import com.example.xmarket.models.Data

data class ProductsModel(
    val count: Int,
    val data: ArrayList<Data>,
    val message: String,
    val status: Boolean
)

data class Data(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val quantity: Int,
    val restaurant_id: Int
)
