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
import com.example.ecommerce.roomDB.Roomdbbutforproducts
import com.example.ecommerce.roomDB.productsbutinDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(val context: Context, val list: List<productsbutinDB>):RecyclerView.Adapter<CartAdapter.viewholder>(){
private var finaltotalprice=0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        return viewholder(
            LayoutInflater.from(context).inflate(R.layout.cartproduct, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val data=list[position]

        val dao=Roomdbbutforproducts.getInstance(context).productDao()



        var toot=1
        val initialPrice = data.productSp.toInt()
        Glide.with(context).load(data.ProductImg).into(holder.img)
        holder.name.text=data.productName
        holder.price.text=(initialPrice * toot).toString()
        holder.total.text= "Number of Quantity: " + toot.toString()
        finaltotalprice=(toot.toDouble()*data.productSp.toDouble())


        holder.remove.setOnClickListener {
            GlobalScope.launch {
                dao.deleteproduct(productsbutinDB(data.productid,data.productName,data.ProductImg,data.productSp,data.productcategory,data.Price))
            }
        }



        holder.img.setOnClickListener {
            val intent=Intent(context,Product_details::class.java).apply {
                putExtra("key",list[position].productid)
                putExtra("kate", list[position].productcategory)
            }
            context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }

    class viewholder(val view: View):RecyclerView.ViewHolder(view) {
        val img=view.findViewById<ImageView>(R.id.imageView8)
        val name=view.findViewById<TextView>(R.id.textView10)
        val total=view.findViewById<TextView>(R.id.textView11)
        val price=view.findViewById<TextView>(R.id.textView12)
        val remove=view.findViewById<ImageView>(R.id.imageView12)
    }
}