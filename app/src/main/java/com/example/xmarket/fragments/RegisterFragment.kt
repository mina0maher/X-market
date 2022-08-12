package com.example.xmarket.fragments

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.xmarket.R

class RegisterFragment : BaseFragment() {
    private lateinit var textSignIn: TextView

    override fun init() {
       textSignIn.setOnClickListener {
           Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
       }
    }

    override fun initViews(view:View) {
       textSignIn = view.findViewById(R.id.textSignIn)
    }

    override fun getViewId(): Int = R.layout.fragment_register

}