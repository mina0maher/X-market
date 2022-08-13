package com.example.xmarket.fragments

import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.xmarket.R
import com.example.xmarket.utilities.Constants.showToast

class RegisterFragment : BaseFragment() {
    private lateinit var inputName:EditText
    private lateinit var inputEmail:EditText
    private lateinit var inputPassword:EditText
    private lateinit var inputConfirmPassword:EditText
    private lateinit var buttonSignUp:Button
    private lateinit var textSignIn: TextView

    override fun init() {
       textSignIn.setOnClickListener {
           Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
       }
    }

    override fun initViews(view:View) {
        inputName = view.findViewById(R.id.inputName)
        inputEmail = view.findViewById(R.id.inputEmail)
        inputPassword = view.findViewById(R.id.inputPassword)
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword)
        buttonSignUp = view.findViewById(R.id.buttonSignIn)
        textSignIn = view.findViewById(R.id.textSignIn)
    }

    override fun getViewId(): Int = R.layout.fragment_register


    private fun isValidSignUpDetails(): Boolean {
        return if (inputName.text.toString().trim().isEmpty()) {
            showToast("Enter name",requireContext())
            false
        } else if (inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email",requireContext())
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString())
                .matches()
        ) {
            showToast("Enter valid email",requireContext())
            false
        } else if (inputPassword.text.toString().trim().isEmpty()) {
            showToast("Enter Password",requireContext())
            false
        } else if (inputConfirmPassword.text.toString().trim().isEmpty()) {
            showToast("Confirm your Password",requireContext())
            false
        } else if (inputPassword.text.toString() != inputConfirmPassword.text.toString()) {
            showToast("Password & confirm password must be same",requireContext())
            false
        } else {
            true
        }
    }
}