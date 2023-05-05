package com.example.ecommerce

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.ecommerce.Mode.UsersModel
import com.example.ecommerce.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var animation: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val auth=FirebaseAuth.getInstance().currentUser
        val user=auth?.phoneNumber
        if(FirebaseAuth.getInstance().currentUser!=null) {
            showdata(user)
        }  else {
            binding.username.visibility= View.GONE
            binding.editTextTextPersonName4.visibility= View.GONE
            binding.textView6.visibility= View.GONE
            binding.streetname.visibility= View.GONE
            binding.country.visibility= View.GONE
            binding.state.visibility= View.GONE
            binding.city.visibility= View.GONE
            binding.save.visibility= View.GONE
            binding.pincode.visibility= View.GONE
            binding.guess.visibility= View.VISIBLE
            binding.lottiebhayer.visibility= View.GONE
            animation = findViewById(R.id.lottiebhayer)
            Handler(Looper.getMainLooper()).postDelayed({
                animation.visibility = View.VISIBLE
                animation.playAnimation()
                animation.loop(true)
            }, 20)


        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        binding.editTextTextPersonName4.text=user
        binding.save.setOnClickListener {
         auth(user)

        }


    }

    private fun showdata(user: String?) {
        Firebase.firestore.collection("users").document(user!!).get().addOnSuccessListener {
            binding.username.setText(it.getString("name"))
            if(it.getString("streetName")!!.isNotEmpty()) {
                binding.streetname.setText(it.getString("streetName"))
            }
            if(it.getString("city")!!.isNotEmpty()) {
                binding.city.setText(it.getString("city"))
            }
            if(it.getString("pincode")!!.isNotEmpty()) {
                binding.pincode.setText(it.getString("pincode"))
            }
            if(it.getString("state")!!.isNotEmpty()) {
                binding.state.setText(it.getString("state"))
            }
            if(it.getString("county")!!.isNotEmpty()) {
                binding.country.setText(it.getString("county"))
            }

        }
    }

    private fun auth(user: String?) {
        if(binding.username.text.isEmpty()) {
            Toast.makeText(this, "Useranme can't be Empty", Toast.LENGTH_SHORT).show()
        } else if(binding.streetname.text.isEmpty()|| binding.country.text.isEmpty()
            ||binding.state.text.isEmpty()||binding.city.text.isEmpty()||binding.pincode.text.isEmpty()) {
            Toast.makeText(this, "Please provide the correct Address", Toast.LENGTH_SHORT).show()
        } else {
            progressDialog.show()
                storedata(user)
        }
    }

    private fun storedata(user: String?) {
        val data=UsersModel(binding.username.text.toString(),user!!,binding.streetname.text.toString(),binding.city.text.toString(),binding.pincode.text.toString(),binding.state.text.toString(),binding.country.text.toString())
        Firebase.firestore.collection("users").document(user!!).set(data).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
        }
    }
}