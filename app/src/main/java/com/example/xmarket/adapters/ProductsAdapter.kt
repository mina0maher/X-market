package com.example.xmarket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xmarket.R
import com.example.xmarket.interfaces.ProductsListener
import com.example.xmarket.models.Data
import de.hdodenhof.circleimageview.CircleImageView

class ProductsAdapter(private val list: ArrayList<Data>, private var context: Context, private val productsListener: ProductsListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            holder.setData(list[position],context,productsListener)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var layout:ConstraintLayout= itemView.findViewById(R.id.layout)
        private var productName : TextView = itemView.findViewById(R.id.item_name)
        private var productPrice : TextView = itemView.findViewById(R.id.item_price)
        private var productImage : CircleImageView = itemView.findViewById(R.id.item_image)
        fun setData(model: Data,context: Context,productsListener: ProductsListener) {
            productName.text = model.name
            val price = "${model.price} $"
            productPrice.text = price
            Glide.with(View(context)).load(model.image).into(productImage)
            layout.setOnClickListener {
                productsListener.onProductClicked(model.id,productImage)
            }
        }
    }


}