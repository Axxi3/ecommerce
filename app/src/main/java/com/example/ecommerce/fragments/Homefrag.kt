package com.example.ecommerce.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aemerse.slider.model.CarouselItem
import com.example.ecommerce.Categoryadapter
import com.example.ecommerce.Mode.Carouseldata
import com.example.ecommerce.Mode.categorymodel
import com.example.ecommerce.Mode.getproducts
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentHomefragBinding
import com.example.ecommerce.peoductShowerAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class Homefrag : Fragment() {
    private lateinit var binding: FragmentHomefragBinding
    private lateinit var activityContext: Context
    private lateinit var progressBar: ProgressBar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomefragBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        progressBar = binding.progressBar

        getSlider()
        getcate()
        getMostpop()

    }

    private fun getMostpop() {
        progressBar.visibility = View.VISIBLE
        val list=ArrayList<getproducts>()
        Firebase.firestore.collection("Products").get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data=doc.toObject(getproducts::class.java)
                    list.add(data!!)
                }
                binding.Popularrecycler.adapter = peoductShowerAdapter(activityContext, list)
                binding.Popularrecycler.layoutManager = GridLayoutManager(activityContext, 2)
            }
        progressBar.visibility = View.GONE
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getcate() {
        val list=ArrayList<categorymodel>()
        Firebase.firestore.collection("Category").get()
            .addOnSuccessListener {
                list.clear()
                for(doc in it.documents) {
                    val data=doc.toObject(categorymodel::class.java)
                    list.add(data!!)
                }
                binding.caterecycler.adapter = Categoryadapter(this@Homefrag,null, list)
            }
    }

    fun getSlider() {
        val list = mutableListOf<CarouselItem>()
        Firebase.firestore.collection("slider").get()
            .addOnSuccessListener {
                list.clear()
                for (doc in it.documents) {
                    val data=doc.toObject(Carouseldata::class.java)
                    list.add(
                        CarouselItem(
                            imageUrl = data!!.img
                        )
                    )
                }
                binding.carousel.setData(list)
            }
    }
}
