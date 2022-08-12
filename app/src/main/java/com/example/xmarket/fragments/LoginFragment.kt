package com.example.xmarket.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.xmarket.R
import com.example.xmarket.models.UserSignInModel
import com.example.xmarket.utilities.Constants
import com.example.xmarket.utilities.Constants.KEY_IS_LOGIN_CLICKED
import com.example.xmarket.utilities.Constants.isOnline
import com.example.xmarket.utilities.Constants.showToast
import com.example.xmarket.utilities.PreferenceManager
import com.example.xmarket.viewmodles.ApiViewModel

class LoginFragment : BaseFragment() {
    private lateinit var buttonSignIn :Button
    private lateinit var progressBar: ProgressBar
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var textCreateNewAccount:TextView
    private lateinit var preferenceManager: PreferenceManager
    private val apiViewModel:ApiViewModel by viewModels()
    private var isLoginClicked = false
    override fun init() {
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
        }
        if(isLoginClicked){
            signIn()
        }
        buttonSignIn.setOnClickListener {
            loading(true)
            if (isValidSignInDetails()) {
                signIn()
            }else{
                loading(false)
            }
        }
        textCreateNewAccount.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState!=null){
            isLoginClicked = savedInstanceState.getBoolean(KEY_IS_LOGIN_CLICKED)
        }
    }



    override fun onDetach() {
        super.onDetach()
        apiViewModel.codesLiveData.removeObservers(requireActivity())
        apiViewModel.errorMessageLiveData.removeObservers(requireActivity())
    }

    private fun signIn(){
        if(isOnline(requireActivity())){
            isLoginClicked = true
        apiViewModel.signIn(UserSignInModel(inputEmail.text.toString(),inputPassword.text.toString()))
            apiViewModel.codesLiveData.observe(requireActivity()) {
                when (it) {
                    200 -> {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                        loading(false)
                    }
                    422 -> {
                        showToast("email & password not correct",requireActivity())
                        loading(false)
                        isLoginClicked=false
                    }
                    401 -> {
                        showToast("password not correct",requireActivity())
                        loading(false)
                        isLoginClicked=false
                    }
                    else -> {
                        showToast("error $it",requireActivity())
                        loading(false)
                        isLoginClicked=false
                    }
                }
            }
        apiViewModel.errorMessageLiveData.observe(requireActivity()) {
            showToast(it,requireContext())
            loading(false)
        }
        }else{
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Error")
            builder.setMessage("check your internet connection and try again")
            builder.setCancelable(true)
            builder.setIcon(R.drawable.ic_no_internet)
            builder.setPositiveButton("reload") { _, _ ->
                signIn()
                loading(false)
            }

            builder.setNegativeButton("exit") { _, _ ->
                requireActivity().finish()
                loading(false)
            }
            builder.setOnCancelListener { loading(false) }


            builder.show()
        }


    }

    override fun initViews(view: View) {
        preferenceManager = PreferenceManager(requireActivity())

        buttonSignIn = view.findViewById(R.id.buttonSignIn)
        inputEmail = view.findViewById(R.id.inputEmail)
        inputPassword = view.findViewById(R.id.inputPassword)
        progressBar = view.findViewById(R.id.progressBar)
        textCreateNewAccount=view.findViewById(R.id.textCreateNewAccount)
    }

    override fun getViewId(): Int =R.layout.fragment_login

    override fun onDestroy() {
        super.onDestroy()
        inputEmail.setText("")
        inputPassword.setText("")
    }

    private fun isValidSignInDetails():Boolean
    {
        return if (inputEmail.text.toString().trim().isEmpty()) {
            showToast("Enter email",requireActivity())
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString())
                .matches()
        ) {
            showToast("Enter valid email",requireActivity())
            false
        } else if (inputPassword.text.toString().trim().isEmpty()) {
            showToast("Enter password",requireActivity())
            false
        } else {
            true
        }
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            buttonSignIn.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            buttonSignIn.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LOGIN_CLICKED,isLoginClicked)
    }
}