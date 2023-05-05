package com.example.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.ecommerce.Mode.orderDetails
import com.example.ecommerce.databinding.ActivityOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class order : AppCompatActivity() {
    private lateinit var orderList:MutableList<orderDetails>
    private lateinit var binding:ActivityOrderBinding
    private lateinit var animation: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if(FirebaseAuth.getInstance().currentUser!=null) {
            getdata()
        }
        else {
                binding.orderrecycler.visibility=View.GONE
                binding.lottiebhayer.visibility=View.GONE
            animation = findViewById(R.id.lottiebhayer)
            Handler(Looper.getMainLooper()).postDelayed({
                animation.visibility = View.VISIBLE
                animation.playAnimation()
                animation.loop(true)
            }, 20)
        }

    }

    private fun getdata() {
       orderList = mutableListOf<orderDetails>()
        Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()).get().addOnSuccessListener {snapshot->
            val orderArray = snapshot.get("order") as ArrayList<HashMap<String, String>>
            for (orderData in orderArray) {
                val order = orderDetails(
                    ordersID = orderData["OrdersID"] ?: "",
                    ordersStatus = orderData["OrdersStatus"] ?: "",
                    sp = orderData["SP"] ?: "",
                    coverimg = orderData["coverimg"] ?: "",
                    id = orderData["id"] ?: "",
                    name = orderData["name"] ?: "",
                    totalPrice = orderData["totalPrice"] ?: "",
                    userID = orderData["userid"] ?: ""
                )
                orderList.add(order)

            }
            binding.orderrecycler.adapter=orderAdapter(this,orderList)
        }

    }

}