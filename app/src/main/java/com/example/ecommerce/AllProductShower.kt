package com.example.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.Mode.getproducts
import com.example.ecommerce.databinding.ActivityAllProductShowerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllProductShower : AppCompatActivity() {
    private lateinit var binding:ActivityAllProductShowerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAllProductShowerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val customMenu = findViewById<Toolbar>(R.id.toolbar)
        customMenu.inflateMenu(R.menu.toolbar)
        val intent: Intent=getIntent()
        val category=intent.getStringExtra("Category")
        showdata(category)
    }

    private fun showdata(category: String?) {
        val list=ArrayList<getproducts>()
        Firebase.firestore.collection(category.toString()+" products").get()
            .addOnSuccessListener {
                binding.Noof.text= "Number of Products:" +it.size().toString()
                list.clear()
                for (doc in it.documents) {
                    val data=doc.toObject(getproducts::class.java)
                    list.add(data!!)
                }
                binding.productrecycler.adapter=peoductShowerAdapter(this,list)
                binding.productrecycler.layoutManager=GridLayoutManager(this,2)

            }
    }
}