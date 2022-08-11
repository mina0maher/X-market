package com.example.xmarket.fragments

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmarket.R
import com.example.xmarket.adapters.ProductsAdapter
import com.example.xmarket.interfaces.ProductsListener
import com.example.xmarket.models.ProductsModel
import com.example.xmarket.utilities.Constants.isOnline
import com.example.xmarket.utilities.PreferenceManager
import com.example.xmarket.viewmodles.ApiViewModel


class HomeFragment : BaseFragment() ,ProductsListener{
    private lateinit var productsRecycler : RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var productsLayout : ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var data : ProductsModel
    private lateinit var logoutImage: ImageView
    private val apiViewModel: ApiViewModel by viewModels()
    override fun init() {
        getData()
        apiViewModel.productsLiveData.observe(requireActivity()){
            data = it
            installRecycler()
            loading(false)
        }
        apiViewModel.errorMessageLiveData.observe(requireActivity()){
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Error")
            builder.setMessage("$it \n do you want try again?")
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
        logoutImage.setOnClickListener {
            val preferenceManager = PreferenceManager(requireActivity())
            preferenceManager.clear()
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
    private fun getData(){
        if(isOnline(requireActivity())){
            apiViewModel.getProductsData()
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


    override fun initViews(view: View) {
        productsRecycler=view.findViewById(R.id.products_recycler)
        progressBar = view.findViewById(R.id.progress_bar)
        productsLayout = view.findViewById(R.id.products_layout)
        logoutImage = view.findViewById(R.id.logout)
    }

    override fun getViewId(): Int = R.layout.fragment_home

    private fun installRecycler(){
        layoutManager = GridLayoutManager(requireActivity(),2)
        productsRecycler.layoutManager = layoutManager
        productsAdapter = ProductsAdapter(data.data,requireActivity(),this)
        productsRecycler.adapter = productsAdapter
    }
    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            productsLayout.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            productsLayout.visibility = View.VISIBLE
        }
    }

    override fun onProductClicked(position: Int, productImage: ImageView) {
        //TODO("Not yet implemented")
    }

}