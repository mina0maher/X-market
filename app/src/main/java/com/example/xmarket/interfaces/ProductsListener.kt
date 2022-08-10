package com.example.xmarket.interfaces

import android.widget.ImageView

interface ProductsListener {
    fun onProductClicked(position:Int,productImage:ImageView)
}