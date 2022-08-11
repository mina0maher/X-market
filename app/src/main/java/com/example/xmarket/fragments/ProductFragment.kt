package com.example.xmarket.fragments

import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.xmarket.R


class ProductFragment : BaseFragment() {

    val args:ProductFragmentArgs by navArgs()
     var productId : Int =0
    override fun init() {
        //TODO("Not yet implemented")
    }

    override fun initViews(view:View) {
        productId = args.productId
    }

    override fun getViewId(): Int = R.layout.fragment_product

}