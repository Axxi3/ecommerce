package com.example.ecommerce

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.Mode.getproducts

class peoductShowerAdapter(val context:Context,val list:List<getproducts>):RecyclerView.Adapter<peoductShowerAdapter.viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.productshower, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        Glide.with(context).load(list[position].productcoverimg).into(holder.img)
        holder.name.text=list[position].productName
        holder.price.text=list[position].productSp
        holder.view.setOnClickListener {
            val intent=Intent(context,Product_details::class.java).apply {
                putExtra("key",list[position].productid)
                putExtra("kate", list[position].productcategory)
                putExtra("price",list[position].productSp)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewholder(val view:View):RecyclerView.ViewHolder(view) {
          val img=view.findViewById<ImageView>(R.id.ProductImage)
        val name=view.findViewById<TextView>(R.id.Name)
        val price=view.findViewById<TextView>(R.id.Price)
    }
}