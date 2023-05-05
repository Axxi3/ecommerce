package com.example.ecommerce.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecommerce.CartAdapter
import com.example.ecommerce.Checkout
import com.example.ecommerce.Product_details
import com.example.ecommerce.databinding.FragmentCartBinding
import com.example.ecommerce.roomDB.Roomdbbutforproducts
import com.example.ecommerce.roomDB.productsbutinDB
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.roundToInt

class cartFragment : Fragment() {
      private lateinit var binding:FragmentCartBinding
    private lateinit var activityContext: Context
    private lateinit var list:ArrayList<String>
    private lateinit var katelist:ArrayList<String>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val dao=Roomdbbutforproducts.getInstance(activityContext).productDao()


        list=ArrayList()
        katelist=ArrayList()
        dao.getAllproduct().observe( viewLifecycleOwner) {
            binding.cartRecycler.adapter=CartAdapter(requireContext(),it)
            list.clear()
            katelist.clear()
            for(data in it) {
                katelist.add(data.productcategory)
                list.add(data.productid)
            }
            gettotal(it)
        }
    }



    @SuppressLint("SuspiciousIndentation")
    private fun gettotal(data: List<productsbutinDB>) {
        var total = 0
        for (item in data) {
            total += item.productSp.toInt()
        }
        binding.total.text = total.toString()
            binding.imageView13.setOnClickListener {
                if (binding.coupon.text.toString().isEmpty()) {
                    if (list.size != 0) {
                        val intent = Intent(requireContext(), Checkout::class.java)
                        val b = Bundle()
                        b.putStringArrayList("productid", list)
                        b.putString("Total", total.toString())
                        intent.putExtras(b)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please add something here",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }  else {
                    Firebase.firestore.collection("Coupons").document(binding.coupon.text.toString()).get().addOnCompleteListener {task ->
                        if (task.isSuccessful) {
                            if(list.size != 0) {
                                val document = task.result
                                if (document != null && document.exists()) {
                                    val discount = document.getString("discount")
                                    val percent = discount!!.toDouble() / 100
                                    val reltotal = total.toDouble() * percent
                                    total = reltotal.roundToInt()
                                    binding.total.text = total.toString()
                                    binding.coupon.text = null
                                    binding.coupon.visibility = View.GONE
                                    binding.textView21.visibility = View.GONE
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Invalid coupon",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Please add something here",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }  else {
                            // Error getting document
                        }
                    }
                }
            }
    }

}