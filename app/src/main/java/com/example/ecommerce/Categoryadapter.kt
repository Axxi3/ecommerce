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
import com.example.ecommerce.Mode.categorymodel
import com.example.ecommerce.fragments.Homefrag
import com.example.ecommerce.fragments.category_frag

class Categoryadapter(val context:Homefrag?,val context2: category_frag?,val list:List<categorymodel>):RecyclerView.Adapter<Categoryadapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(LayoutInflater.from(parent.context).inflate(R.layout.categories,parent,false))
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        if(context!= null) {
            Glide.with(context!!).load(list[position].img).into(holder.img)
            holder.name.text = list[position].kate.uppercase()
            holder.view.setOnClickListener {
                val intent= Intent(holder.view.context,AllProductShower::class.java).apply {
                    putExtra("Category", list[position].kate)
                }
                context!!.startActivity(intent)
            }
        } else {
            Glide.with(context2!!).load(list[position].img).into(holder.img)
            holder.name.text = list[position].kate.uppercase()
            holder.view.setOnClickListener {
                val intent= Intent(holder.view.context,AllProductShower::class.java).apply {
                    putExtra("Category", list[position].kate)
                }
                context2!!.startActivity(intent)
            }
        }




    }

    private fun letsgo() {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Viewholder(val view: View):RecyclerView.ViewHolder(view) {
  val img=view.findViewById<ImageView>(R.id.categoryImage)
        val name=view.findViewById<TextView>(R.id.namer)
    }
}