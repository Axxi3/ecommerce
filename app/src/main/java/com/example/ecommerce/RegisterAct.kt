package com.example.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.airbnb.lottie.LottieAnimationView
import com.example.ecommerce.Mode.UsersModel
import com.example.ecommerce.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class RegisterAct : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var animation: LottieAnimationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val customMenu = findViewById<Toolbar>(R.id.toolbar)
        customMenu.inflateMenu(R.menu.toolbar)
        animation = findViewById(R.id.lottiebhay)
        Handler(Looper.getMainLooper()).postDelayed({
            animation.visibility = View.VISIBLE
            animation.playAnimation()
            animation.loop(true)
        }, 20)
 binding.button.setOnClickListener {
   auth()
 }

        binding.Signin.setOnClickListener {
            startActivity(Intent(this,Otp::class.java))
            finish()
        }
    }

    private fun auth() {
        if(binding.editTextTextPersonName.text.isEmpty() || binding.editTextNumberPassword.text.isEmpty() || binding.editTextTextPersonName2.text.isEmpty())  {
            Toast.makeText(this, "Please fill everything", Toast.LENGTH_SHORT).show()
        }  else if (binding.editTextNumberPassword.text.toString().trim().length != 2 && binding.editTextNumberPassword.text.toString().trim().length != 3 ) {
            Toast.makeText(this, "Invalid country code", Toast.LENGTH_SHORT).show()
        }  else if(binding.editTextTextPersonName.text.trim().length != 10 ) {
            Toast.makeText(this, "Invalid Phone number", Toast.LENGTH_SHORT).show()
        }  else {
            storedata()
        }
    }

    private fun storedata() {
        val builder=AlertDialog.Builder(this).setTitle("Loading.....").setMessage("Please wait").setCancelable(false).create()
        builder.show()
        var token:String?=null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }

            // Get new FCM registration token
             token = task.result
            Log.d("FMCtoken", "storedata: $token")
        })

        val data= UsersModel(binding.editTextTextPersonName2.text.toString(),
            "+"+binding.editTextNumberPassword.text.toString()+binding.editTextTextPersonName.text.toString())

        Firebase.firestore.collection("users").document("+"+binding.editTextNumberPassword.text.toString()+binding.editTextTextPersonName.text.toString()).set(data).addOnSuccessListener {
            startActivity(Intent(this,Otp::class.java))
            finish()
            Toast.makeText(this, "You can now Login", Toast.LENGTH_SHORT).show()
            builder.dismiss()

        } .addOnFailureListener {
            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
            builder.dismiss()
        }
    }

    private fun openlogin() {
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}