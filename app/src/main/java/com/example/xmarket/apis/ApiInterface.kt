package com.example.xmarket.apis

import com.example.xmarket.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST("sign_up")
    fun signUp(@Body userSignUpModel: UserSignUpModel):Call<SignUpResponseModel>

    @POST("login")
    fun logIn(@Body userSignInModel: UserSignInModel): Call<SignInResponseModel>

    @GET("products")
    fun getData(): Call<ProductsModel>

    @GET("products/{id}")
    fun getProduct(@Path("id")productId:Int):Call<ProductModel>
}