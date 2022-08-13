package com.example.xmarket.viewmodles


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.xmarket.apis.RetrofitFactory
import com.example.xmarket.models.*
import retrofit2.Call
import retrofit2.Response

class ApiViewModel : ViewModel() {

    var productsMD:SingleLiveEvent<ProductsModel> = SingleLiveEvent()
    val productsLiveData:LiveData<ProductsModel>
        get()=productsMD

    var productMD:SingleLiveEvent<ProductModel> = SingleLiveEvent()
    val productLiveData:LiveData<ProductModel>
        get()=productMD

     var codesMD: SingleLiveEvent<Int> = SingleLiveEvent()
     val codesLiveData:LiveData<Int>
          get() = codesMD

    var bodyMD: SingleLiveEvent<SignInResponseModel> = SingleLiveEvent()
    val bodyLiveData:LiveData<SignInResponseModel>
        get() = bodyMD

    var errorMessageMD: SingleLiveEvent<String> = SingleLiveEvent()
    val errorMessageLiveData:LiveData<String>
        get() = errorMessageMD

    var signUpBodyMD: SingleLiveEvent<SignUpResponseModel> = SingleLiveEvent()
    val signUpBodyLiveData:LiveData<SignUpResponseModel>
        get() = signUpBodyMD

    fun signIn(userSignInModel: UserSignInModel){
         RetrofitFactory.apiInterface().logIn(userSignInModel)
             .enqueue(object :retrofit2.Callback<SignInResponseModel>{
            override fun onResponse(
                call: Call<SignInResponseModel>,
                response: Response<SignInResponseModel>
            ) {
                codesMD.postValue(response.code())
                if(response.code()==200) {
                    bodyMD.postValue(response.body())
                }
            }
            override fun onFailure(call: Call<SignInResponseModel>, t: Throwable) {
                errorMessageMD.postValue(t.message.toString())
            }

        })
    }
    fun getProductsData(){
       RetrofitFactory.apiInterface().getData()
           .enqueue(object : retrofit2.Callback<ProductsModel>{
                override fun onResponse(call: Call<ProductsModel>, response: Response<ProductsModel>) {
                    productsMD.postValue(response.body())
                }

                override fun onFailure(call: Call<ProductsModel>, t: Throwable) {
                    errorMessageMD.postValue(t.message.toString())
                }

            })
    }
    fun getProductData(productId:Int){
         RetrofitFactory.apiInterface().getProduct(productId)
             .enqueue(object : retrofit2.Callback<ProductModel>{
                override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                    productMD.postValue(response.body())
                }

                override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                    errorMessageMD.postValue(t.message.toString())
                }

            })

    }
    fun signUp(userSignUpModel: UserSignUpModel){
        RetrofitFactory.apiInterface().signUp(userSignUpModel)
            .enqueue(object :retrofit2.Callback<SignUpResponseModel>{
                override fun onResponse(
                    call: Call<SignUpResponseModel>,
                    response: Response<SignUpResponseModel>
                ) {
                    codesMD.postValue(response.code())
                    if(response.code()==200){
                        signUpBodyMD.postValue(response.body())
                    }
                }
                override fun onFailure(call: Call<SignUpResponseModel>, t: Throwable) {
                    errorMessageMD.postValue(t.message.toString())
                }

            })
    }
}

