package com.example.xmarket.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.example.xmarket.R
import com.example.xmarket.models.ProductData
import com.example.xmarket.utilities.Constants.KEY_PRODUCT_SAVED_INSTANCE
import com.example.xmarket.utilities.Constants.isOnline
import com.example.xmarket.viewmodles.ApiViewModel
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView


class ProductFragment : BaseFragment() {
    private lateinit var productName : TextView
    private lateinit var productPrice : TextView
    private lateinit var productImage : CircleImageView
    private lateinit var nameProgressBar: ProgressBar
    private lateinit var priceProgressBar: ProgressBar
    private val args:ProductFragmentArgs by navArgs()
    private val apiViewModel: ApiViewModel by viewModels()
    private var data : ProductData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
        if(savedInstanceState!=null){
            data = Gson().fromJson(savedInstanceState.getString(KEY_PRODUCT_SAVED_INSTANCE),ProductData::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        if (data == null) {
            getData()
        }else{
            productName.text = data!!.name
            val price = "${data!!.price} $"
            productPrice.text = price
            Glide.with(View(requireActivity())).load(data!!.image).into(productImage)
            loading(false)
        }
        apiViewModel.productLiveData.observe(requireActivity()){
            data=it.data
            productName.text = it.data.name
            val price = "${it.data.price} $"
            productPrice.text = price
            Glide.with(View(requireActivity())).load(it.data.image).into(productImage)
            loading(false)

        }
        apiViewModel.errorMessageLiveData.observe(requireActivity()){

            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Error")
            builder.setMessage("$it do you want to try again ?")
            builder.setCancelable(false)
            builder.setIcon(R.drawable.ic_no_internet)
            builder.setPositiveButton("reload") { _, _ ->
                Thread{apiViewModel.getProductData(args.productId)}.start()
            }

            builder.setNegativeButton("exit") { _, _ ->
                requireActivity().finish()
            }


            builder.show()

        }
    }
    private fun getData(){
        if(isOnline(requireContext())){
            apiViewModel.getProductData(args.productId)
        }else{
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Error")
            builder.setMessage("please check your internet connection and try again")
            builder.setCancelable(false)
            builder.setIcon(R.drawable.ic_no_internet)
            builder.setPositiveButton("reload") { _, _ ->
                getData()
            }

            builder.setNegativeButton("exit") { _, _ ->
                requireActivity().finish()
            }


            builder.show()
        }
    }

    override fun init() {

    }

    override fun initViews(view:View) {
        productName=view.findViewById(R.id.item_name)
        productPrice=view.findViewById(R.id.item_price)
        productImage=view.findViewById(R.id.item_image)
        nameProgressBar=view.findViewById(R.id.name_progress_bar)
        priceProgressBar=view.findViewById(R.id.price_progress_bar)
    }

    override fun getViewId(): Int = R.layout.fragment_product

    private fun loading(isLoading:Boolean){
        if(isLoading){
            nameProgressBar.visibility=View.VISIBLE
            productName.visibility=View.INVISIBLE
            priceProgressBar.visibility=View.VISIBLE
            productPrice.visibility=View.INVISIBLE
        }else{
            nameProgressBar.visibility=View.INVISIBLE
            productName.visibility=View.VISIBLE
            priceProgressBar.visibility=View.INVISIBLE
            productPrice.visibility=View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        apiViewModel.productLiveData.removeObservers(requireActivity())
        apiViewModel.errorMessageLiveData.removeObservers(requireActivity())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(data!=null) {
            outState.putString(KEY_PRODUCT_SAVED_INSTANCE, Gson().toJson(data))
        }
    }
}