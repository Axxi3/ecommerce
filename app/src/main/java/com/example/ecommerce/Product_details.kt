package com.example.ecommerce

import com.example.ecommerce.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.aemerse.slider.model.CarouselItem
import com.example.ecommerce.Mode.getproducts
import com.example.ecommerce.databinding.ActivityProductDetailsBinding
import com.example.ecommerce.roomDB.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


class Product_details : AppCompatActivity() {
    private lateinit var binding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val intent: Intent =getIntent()
        val key=intent.getStringExtra("key")
        val category=intent.getStringExtra("kate")
        val price=intent.getStringExtra("price")
        val ss="single"

        binding.bottombar.imageView16.setOnClickListener {
            val username = "aslimills"
            val uri = Uri.parse("http://instagram.com/_u/$username")
            val intent = Intent(Intent.ACTION_VIEW, uri)

// Check if the Instagram app is installed on the device
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // If the Instagram app is not installed, open the Instagram web page in the device's browser
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$username"))
                startActivity(webIntent)
            }

        }


        binding.bottombar.textView28.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aslimills.com"))
            startActivity(intent)

        }
        binding.bottombar.textView29.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aslimills.com"))
            startActivity(intent)

        }


        showData(key,category,price)
    }



    private fun showData(key: String?, category: String?, price: String?) {
        Log.d("key", "showData: $key")
        val list=ArrayList<getproducts>()
        val Imglist = ArrayList<CarouselItem>()
        Firebase.firestore.collection(category +" products").document(key!!).get().addOnSuccessListener {
            Log.d("it", "showData: $it")
            list.clear()
            val data = it.toObject(getproducts::class.java)
            if (data?.productimg != null) {
            val lister= it.get("productimg") as ArrayList<String>
           for(i in 0 until lister.size) {
               Imglist.add(CarouselItem(lister[i]))
           }
                binding.ProductImages.setData(Imglist)
            }
            else {

                val get= it.getString("productName").toString()
                Toast.makeText(this,get, Toast.LENGTH_SHORT).show()
            }
            binding.ProductName.text=it.getString("productName")

            binding.textView3.text=data?.productSp
            binding.des.text=data?.productDes


            cartshopping(key,data!!.productName,data!!.productcoverimg,data!!.productSp,category,price)
        }
    }

    private fun cartshopping(
        key: String,
        productName: String?,
        productcoverimg: String?,
        productSp: String?,
        category: String?,
        price: String?
    ) = CoroutineScope(Dispatchers.Main).launch {
        val productDao = Roomdbbutforproducts.getInstance(this@Product_details).productDao()

        // Perform the database operation on a background thread
        withContext(Dispatchers.IO) {
            if (productDao.isreal(key) != null) {
                binding.cart.setImageResource(R.drawable.baseline_remove_shopping_cart_24)
            } else {
                binding.cart.setImageResource(R.drawable.baseline_add_shopping_cart_24)
            }
        }

        binding.cart.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val productDao = Roomdbbutforproducts.getInstance(this@Product_details).productDao()

                // Perform the database operation on a background thread
                withContext(Dispatchers.IO) {
                    if (productDao.isreal(key) != null) {
                        openCart(
                            productDao,
                            key,
                            productName,
                            productcoverimg,
                            productSp,
                            category,
                            price!!.toInt()
                        )
                    } else {
                        addTocart(
                            productDao,
                            key,
                            productName,
                            productcoverimg,
                            productSp,
                            category,
                            price!!.toInt()
                        )
                    }
                }
            }
        }  else {
                Toast.makeText(this@Product_details
                    , "Please Login to add iten to cart", Toast.LENGTH_SHORT).show()
        }
        }



    }

    private fun addTocart(
        productDao: productsButinDbDao,
        key: String,
        productName: String?,
        productcoverimg: String?,
        productSp: String?,
        category: String?,
        price: Int
    ) {
        val data = productsbutinDB(key, productName!!, productcoverimg!!, productSp!!, category!!,price)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data)
            binding.cart.setImageResource(R.drawable.baseline_remove_shopping_cart_24)
        }
    }

    private fun openCart(
        productDao: productsButinDbDao,
        key: String,
        productName: String?,
        productcoverimg: String?,
        productSp: String?,
        category: String?,
        price: Int
    ) {
        val data = productsbutinDB(key, productName!!, productcoverimg!!, productSp!!, category!!,price)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.deleteproduct(data)
            binding.cart.setImageResource(R.drawable.baseline_add_shopping_cart_24)
        }
    }

}