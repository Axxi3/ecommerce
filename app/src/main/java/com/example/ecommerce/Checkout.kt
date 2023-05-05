package com.example.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecommerce.databinding.ActivityCheckoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Checkout : AppCompatActivity() {
    private lateinit var binding:ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val user=FirebaseAuth.getInstance().currentUser!!.phoneNumber
        getdata(user)
        var categoryList=ArrayList<String>()
        categoryList.add(0,"Choose a payment method")
        categoryList.add(1,"Cash On Delivery")
       // categoryList.add(2,"Pay Online")
        val arrayadapter= ArrayAdapter(this,R.layout.dropdown,categoryList)
        binding.spinner.adapter=arrayadapter
        binding.textView15.setOnClickListener {
            Toast.makeText(this, "Please fill all the feilds", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Profile::class.java))
        }

        binding.imageView13.setOnClickListener {
            val id=intent.getStringExtra("single")
            if(binding.address.text.toString()=="a,a,a,a") {
                val intent=Intent(this,Profile::class.java)
                Toast.makeText(this, "Please provide a valid address", Toast.LENGTH_SHORT).show()
                startActivity(intent)

            }else {
                if(binding.spinner.selectedItemPosition!=0) {
                    val b = Bundle()
                    b.putStringArrayList("productid", intent.getStringArrayListExtra("productid"))
                    b.putString("Total", intent.getStringExtra("Total"))
                    b.putString("payment",
                        binding.spinner.selectedItemPosition.toString()
                    )
                    b.putString("single",id)
                    Log.d("idkivalue", "onCreate: $id")
                    val intent = Intent(this, Payment::class.java)
                    intent.putExtras(b)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this, "Please select how you would like to pay.", Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

    private fun getdata(user: String?) {
        Firebase.firestore.collection("users").document(user!!).get().addOnSuccessListener {
            binding.Namer.text=it.getString("name")
            binding.Phone.text=it.getString("userphonenumber")
            binding.address.text=it.getString("streetName")+","+it.getString("city")+","+it.getString("state")+","+it.getString("pincode")
        }
    }
}