package com.example.ecommerce

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.Mode.orderDetails

class orderAdapter(val context:Context, val list: MutableList<orderDetails>):RecyclerView.Adapter<orderAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ordershower, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=list[position]
        Glide.with(context).load(data.coverimg).into(holder.img)
        holder.name.text=data.name
        holder.price.text="Total amount you will pay: "+data.totalPrice
        holder.id.text="OrderID: " +data.ordersID
     }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val img=view.findViewById<ImageView>(R.id.orderpic)
        val name=view.findViewById<TextView>(R.id.Productanme)
        val price=view.findViewById<TextView>(R.id.Productkadam)
        val id=view.findViewById<TextView>(R.id.OrderId)
    }
}