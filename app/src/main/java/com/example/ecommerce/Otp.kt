package com.example.ecommerce

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
import com.airbnb.lottie.LottieAnimationView
import com.example.ecommerce.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class Otp : AppCompatActivity() {
    private lateinit var binding:ActivityOtpBinding
    private lateinit var builder: AlertDialog
    private var time = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
      val  animation = findViewById<LottieAnimationView>(R.id.lottiebhayer)
        Handler(Looper.getMainLooper()).postDelayed({
            animation.visibility = View.VISIBLE
            animation.playAnimation()
            animation.loop(true)
        }, 20)


        builder=AlertDialog.Builder(this).setTitle("Loading.....").setMessage("Please wait").setCancelable(false).create()
        binding.verify.setOnClickListener {
           auth()
        }

        binding.Register.setOnClickListener {
            startActivity(Intent(this,RegisterAct::class.java))
            finish()
        }

    }

    private fun auth() {
        if (binding.PhoneNumber.text.toString()
                .trim().length != 10 || binding.PhoneNumber.text.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Please write a valid Phone number",
                Toast.LENGTH_SHORT
            ).show()
        } else if (binding.CC.text.toString()
                .trim().length != 2 || binding.CC.text.isEmpty()
        ) {
            Toast.makeText(
               this,
                "Please write a valid country code",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            verifyuser("+" + binding.CC.text.toString() + binding.PhoneNumber.text.toString())
           // sendOTP("+" + binding.CC.text.toString() + binding.PhoneNumber.text.toString())
        }
    }

    private fun verifyuser(num: String) {
        Firebase.firestore.collection("users").document(num).get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    sendOTP(num)
                } else {
                    Toast.makeText(this, "Please Register to continue", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun sendOTP(phoneNumber: String) {

        builder.show()
        Log.d("number", "sendOTP: $phoneNumber")
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


   val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            binding.PhoneNumber.visibility=View.GONE
            binding.CC.visibility=View.GONE
            binding.verify.visibility=View.GONE
            binding.Register.visibility=View.GONE
            binding.otp.visibility=View.VISIBLE
            binding.verifyotp.visibility=View.VISIBLE
            builder.dismiss()
            binding.verifyotp.setOnClickListener {
                Log.d("verrificationid", "onCodeSent: $verificationId")
                if(binding.otp.text.isEmpty()) {
                    Toast.makeText(this@Otp, "Please provide the Otp", Toast.LENGTH_SHORT).show()
                }  else {
                    checkOTP(verificationId,binding.otp.text.toString())
                }
            }

        }
    }



    private fun checkOTP(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "You are signed in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()

                    val user = task.result?.user
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            onDestroy()
            super.onBackPressed()

        } else {
            Toast.makeText(this, "Press again to Exit", Toast.LENGTH_SHORT).show()
        }
        time = System.currentTimeMillis()
    }
}