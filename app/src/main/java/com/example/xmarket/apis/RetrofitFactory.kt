package com.example.xmarket.apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
//    private val headerInterceptor = Interceptor { chain ->
//        var request = chain.request()
//        val url=request.url().newBuilder().addQueryParameter("name","mina").build()
//        request = request.newBuilder().url(url)
//            .build()
//        val response = chain.proceed(request)
//        response
//    }
//    private val okHttp=OkHttpClient.Builder().addInterceptor(headerInterceptor)
    fun apiInterface(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://android-training.appssquare.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
          //  .client(okHttp.build())
            .build()
            .create(ApiInterface::class.java)
    }


}

