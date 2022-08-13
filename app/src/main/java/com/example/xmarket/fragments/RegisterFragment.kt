package com.example.xmarket.fragments

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
import com.example.xmarket.models.UserSignUpModel
import com.example.xmarket.utilities.Constants
import com.example.xmarket.utilities.Constants.KEY_IS_SIGNUP_CLICKED
import com.example.xmarket.utilities.Constants.showToast
import com.example.xmarket.utilities.PreferenceManager
import com.example.xmarket.viewmodles.ApiViewModel

class RegisterFragment : BaseFragment() {
    private lateinit var inputName:EditText
    private lateinit var inputEmail:EditText
    private lateinit var inputPassword:EditText
    private lateinit var inputConfirmPassword:EditText
    private lateinit var buttonSignUp:Button
    private lateinit var textSignIn: TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var preferenceManager: PreferenceManager
    private val apiViewModel: ApiViewModel by viewModels()
    private var isSignUpClicked = false

    override fun init() {
        textSignIn.setOnClickListener {
           Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment)
        }
        if(isSignUpClicked){
            signUp()
        }
        buttonSignUp.setOnClickListener {
            loading(true)
            if (isValidSignUpDetails()) {
                signUp()
            }else{
                loading(false)
            }
        }
    }

    override fun initViews(view:View) {
        preferenceManager = PreferenceManager(requireActivity())
        inputName = view.findViewById(R.id.inputName)
        inputEmail = view.findViewById(R.id.inputEmail)
        inputPassword = view.findViewById(R.id.inputPassword)
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword)
        buttonSignUp = view.findViewById(R.id.buttonSignUp)
        textSignIn = view.findViewById(R.id.textSignIn)
        progressBar =view.findViewById(R.id.progressBar)
    }

    override fun getViewId(): Int = R.layout.fragment_register


    private fun signUp(){
        if(Constants.isOnline(requireActivity())){
            isSignUpClicked = true
            loading(true)


            apiViewModel.signUp(UserSignUpModel(inputName.text.toString(),inputEmail.text.toString(),inputPassword.text.toString()))
            apiViewModel.codesLiveData.observe(requireActivity()) {
                when (it) {
                    200 -> {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_homeFragment)
                        loading(false)
                    }
                    422 -> {
                        showToast("email & password not correct",requireActivity())
                        loading(false)
                        isSignUpClicked=false
                    }
                    401 -> {
                        showToast("password not correct",requireActivity())
                        loading(false)
                        isSignUpClicked=false
                    }
                    else -> {
                        showToast("error $it",requireActivity())
                        loading(false)
                        isSignUpClicked=false
                    }
                }
            }
            apiViewModel.errorMessageLiveData.observe(requireActivity()) {
                showToast(it,requireContext())
                loading(false)
            }
            apiViewModel.signUpBodyLiveData.observe(requireActivity()){
                preferenceManager.putString(Constants.KEY_USER_NAME, it.data.name)
            }
        }else{
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Error")
            builder.setMessage("check your internet connection and try again")
            builder.setCancelable(true)
            builder.setIcon(R.drawable.ic_no_internet)
            builder.setPositiveButton("reload") { _, _ ->
                signUp()
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
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            buttonSignUp.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            buttonSignUp.visibility = View.VISIBLE
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState!=null){
            isSignUpClicked = savedInstanceState.getBoolean(KEY_IS_SIGNUP_CLICKED)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_SIGNUP_CLICKED,isSignUpClicked)
    }

    override fun onDestroy() {
        super.onDestroy()
        inputEmail.setText("")
        inputName.setText("")
        inputPassword.setText("")
        inputConfirmPassword.setText("")
        apiViewModel.codesLiveData.removeObservers(requireActivity())
        apiViewModel.errorMessageLiveData.removeObservers(requireActivity())
        apiViewModel.signUpBodyLiveData.removeObservers(requireActivity())
    }
}