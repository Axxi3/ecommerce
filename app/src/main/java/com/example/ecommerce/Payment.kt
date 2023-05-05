package com.example.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.Mode.UsersModel
import com.example.ecommerce.roomDB.Roomdbbutforproducts
import com.example.ecommerce.roomDB.productsbutinDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.AutoReadOtpHelper
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class Payment : AppCompatActivity(), PaymentResultListener {
    private lateinit var orderlist: MutableList<Any>
    private var i=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
       val price= intent.getStringExtra("Total").toString()
        val paymentMethod= intent.getStringExtra("payment").toString()
        if(paymentMethod=="1") {
            uploaddata()
        }
        val co=com.razorpay.Checkout()
        co.setKeyID("rzp_test_ADFZP5P2V1uoKt")
        try {
            val options = JSONObject()
            options.put("name","Ecorp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc")
            options.put("currency","INR")
            options.put("amount",(price.toInt()*100).toString())//pass amount in currency subunits
            options.put("email","anuragkumar774962@gmail.com")
            options.put("contact","8638258564")
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
        }
    }



    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Order Successfull", Toast.LENGTH_SHORT).show()
        uploaddata()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploaddata() {
            val id = intent.getStringArrayListExtra("productid")
            orderlist = mutableListOf()
            Log.d("idhubhay", "uploaddata: $id")
            for (currentid in id!!) {
                Log.d("nooftime", "uploaddata: ")
                fetchdata(currentid)
            }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun fetchdata(currentid: String?) {
        val dao=Roomdbbutforproducts.getInstance(this).productDao()


            Firebase.firestore.collection("Products").document(currentid!!).get().addOnSuccessListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteproduct(productsbutinDB(currentid,it.getString("productName")!!,it.getString("productcoverimg")!!,it.getString("productSp")!!,it.getString("productcategory")!!,it.getString("productSp")!!.toInt() ))
                }

                savedata(it.getString("productName"),
                    (it.getString("productSp")),(it.getString("productcoverimg")),it.getString("productid"))

            }
    }

    private fun savedata(name: String?, Sp: String?, coverimg: String?, id: String?) {
        val random = Random()
        val number = random.nextInt(90000000) + 10000000
        val myVariable: Int = number


        Log.d("name", "savedata: $name")
        Log.d("name", "savedata: $id")
            val data= hashMapOf<String,Any>()
        data["name"]=name!!
        data["SP"]=Sp!!
        data["coverimg"]=coverimg!!
        data["id"]=id!!
        data["OrdersStatus"]="Ordered"
        data["userid"]=FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()
        val firestore=Firebase.firestore.collection("all Orders").document(myVariable.toString())
        //val key=firestore.document().id
        data["OrdersID"]=myVariable.toString()
        data["totalPrice"]=intent.getStringExtra("Total").toString()
        orderlist.add(data)
        val size=intent.getStringArrayListExtra("productid")
        Log.d("ikivalue", "savedata: $i")
        Log.d("ikivalue", "savedata: "+ size!!.size)
        firestore.set(data).addOnSuccessListener {
            Log.d("iinsuccess", "savedata: $i")
        if(i==size!!.size) {
            Log.d("iinsuccess2", "savedata: $i")
            orderlist.add(data)
              Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString()).get().
                      addOnSuccessListener {
                          val model = UsersModel(it.getString("name").toString(),FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString(),it.getString("streetName").toString(),it.getString("city").toString(),it.getString("pincode").toString(),it.getString("state").toString(),it.getString("country").toString(),order = orderlist)
                          Firebase.firestore.collection("users")
                              .document(FirebaseAuth.getInstance().currentUser!!.phoneNumber.toString())
                              .update("order", FieldValue.arrayUnion(data))
                              .addOnSuccessListener {
                                  Toast.makeText(this, "Order Successfull", Toast.LENGTH_SHORT)
                                      .show()
                                  startActivity(Intent(this, MainActivity::class.java))
                              }
                      }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
        i=i+1

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()

    }
}