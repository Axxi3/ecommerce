package com.example.ecommerce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.airbnb.lottie.LottieAnimationView
import com.example.ecommerce.databinding.ActivityFirstBinding

class First_Activity : AppCompatActivity() {
    private lateinit var binding:ActivityFirstBinding
    private lateinit var animation: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        animation = findViewById(R.id.lottie)
        Handler(Looper.getMainLooper()).postDelayed({
            animation.visibility = View.VISIBLE
            animation.playAnimation()
            animation.loop(true)
        }, 20)




        binding.button.setOnClickListener {
            startActivity(Intent(this,RegisterAct::class.java))
            finish()
        }
        binding.button3.setOnClickListener {
            val b=Bundle()
                b.putString("no","no")
            val i=Intent(this,MainActivity::class.java)
            i.putExtras(b)
            startActivity(i)
            finish()
        }
    }
}