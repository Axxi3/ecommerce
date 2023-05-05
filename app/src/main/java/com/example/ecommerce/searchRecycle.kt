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

class searchRecycle(val context:Context, var list:List<getproducts>):RecyclerView.Adapter<searchRecycle.Viewhold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewhold {
        return Viewhold(
            LayoutInflater.from(parent.context).inflate(R.layout.searchshower, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Viewhold, position: Int) {
        val dta=list[position]
        Glide.with(context).load(dta.productcoverimg).into(holder.img)
        holder.text.text=dta.productName
        holder.view.setOnClickListener {
            val intent= Intent(context,Product_details::class.java).apply {
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

    fun searchdata(searchList:List<getproducts>) {
        list=searchList
        notifyDataSetChanged()
    }

    class Viewhold(val view: View):RecyclerView.ViewHolder(view) {
        val img=view.findViewById<ImageView>(R.id.searchimg)
        val text=view.findViewById<TextView>(R.id.textView17)
    }
}