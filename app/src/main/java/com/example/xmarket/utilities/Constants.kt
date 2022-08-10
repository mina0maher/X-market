package com.example.xmarket.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.xmarket.R

object Constants {
    const val KEY_IS_SIGNED_IN = "isSignedIn"
    const val KEY_PRODUCT_ID="productId"
    const val KEY_PREFERENCE_NAME = "chatAppPreference"

    private var toast: Toast? = null
     fun showToast(message: String,context: Context) {
        if(toast !=null){
            toast!!.cancel()

        }
        toast =  Toast.makeText(context,message,Toast.LENGTH_SHORT)
        toast!!.show()
    }

    fun isOnline(context: Context): Boolean {
         val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         val n = cm.activeNetwork
         if (n != null) {
             val nc = cm.getNetworkCapabilities(n)
             return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
         }
         return false
     }

}