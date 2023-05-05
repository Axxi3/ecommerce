package com.example.ecommerce.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecommerce.Otp
import com.example.ecommerce.Profile
import com.example.ecommerce.RegisterAct
import com.example.ecommerce.databinding.FragmentMoreBinding
import com.example.ecommerce.order
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (FirebaseAuth.getInstance().currentUser != null) {
            binding.login.visibility = View.GONE
            binding.textView5.visibility = View.GONE
        }
        binding.textView4.setOnClickListener {
            val intent=Intent(requireContext(),order::class.java)
            startActivity(intent)
        }
        binding.textView7.setOnClickListener {
            val intent=Intent(requireContext(),Profile::class.java)
            startActivity(intent)
        }
        binding.Address.setOnClickListener {
            startActivity(Intent(requireContext(),Profile::class.java))
        }
        binding.login.setOnClickListener {
            val intent=Intent(requireContext(),Otp::class.java)
            startActivity(intent)
        }
        binding.order.setOnClickListener {
            val intent=Intent(requireContext(),order::class.java)
            startActivity(intent)
        }
    }
}


    