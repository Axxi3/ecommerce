package com.example.ecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.ecommerce.Categoryadapter
import com.example.ecommerce.Mode.categorymodel
import com.example.ecommerce.R
import com.example.ecommerce.databinding.FragmentCategoryFragBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class category_frag : Fragment() {
 private lateinit var binding:FragmentCategoryFragBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCategoryFragBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        getcate()

    }

    private fun getcate() {
        val list=ArrayList<categorymodel>()
        Firebase.firestore.collection("Category").get()
            .addOnSuccessListener {
                list.clear()
                for(doc in it.documents) {
                    val data=doc.toObject(categorymodel::class.java)
                    list.add(data!!)
                }
                binding.caterecyce.adapter = Categoryadapter(null,this@category_frag, list)
            }
    }


}