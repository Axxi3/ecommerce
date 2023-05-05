package com.example.ecommerce

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.ecommerce.Mode.getproducts
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.fragments.Homefrag
import com.example.ecommerce.fragments.MoreFragment
import com.example.ecommerce.fragments.cartFragment
import com.example.ecommerce.fragments.category_frag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  lateinit var dataList: ArrayList<getproducts>
    private var databaseReference:DatabaseReference?=null
    private var eventListener:ValueEventListener?=null
    private lateinit var adapter:searchRecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        search()
        val no=intent.getStringExtra("no")
        Log.d("nonono", "onCreate: $no")
        val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("no_key", no)
        editor.apply()
        val sharedPref2 = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val no2 = sharedPref.getString("no_key", "a")
        Log.d("nonono", "onCreate: $no")


        if(no!="no"  && no2!="a") {
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this, First_Activity::class.java))
                finish()
            }
        }


        replace(Homefrag())
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val customMenu = findViewById<Toolbar>(R.id.toolbar)
        customMenu.inflateMenu(R.menu.toolbar)
        val cart=cartFragment()
        customMenu.setOnMenuItemClickListener {
            when (it.itemId) {

                else -> null == true
            }
        }
        binding.bottomBar.setOnItemSelectedListener {
           if(it==0) {
                   replace(Homefrag())
           }  else if(it==1) {
                    replace(category_frag())
           }  else if(it ==2){
               replace(cartFragment())

           }  else if(it==3) {
               replace(MoreFragment())
           }
        }


    }
    fun replace(Frag: Fragment) {
         supportFragmentManager.beginTransaction().replace(R.id.container,Frag).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        if (menu != null) {
            val manager=getSystemService(Context.SEARCH_SERVICE)as SearchManager
            val search=menu.findItem(R.id.action_search)
            val searchView =search.actionView as SearchView
            searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    searchView.isIconified=true
                    searchView.onActionViewCollapsed()
                    if(query==null||query.isEmpty()) {
                        binding.searchrecycler.visibility=View.GONE
                    }  else {
                        searchList(query)
                    }

                    Toast.makeText(this@MainActivity, "$query", Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText==null || newText.isEmpty()) {
                        binding.searchrecycler.visibility=View.GONE

                    }  else {
                        searchList(newText)
                    }

                    return true
                }
            })
        }
        return true
    }

    fun search() {
        dataList = ArrayList()
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("products")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (productSnapshot in snapshot.children) {
                   val dataclass= productSnapshot.getValue(getproducts::class.java )
                    if(dataclass != null) {
                        Log.d("datalist", "onDataChange: 1")
                        Log.d("datalist", "onDataChange: ${dataclass.productcoverimg}")
                        dataList.add(dataclass)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })


    }

    private fun searchList(query: String?) {
        val searchlist=ArrayList<getproducts>()
        searchlist.clear()
        if(query!=null && query.isNotEmpty()) {
            for (doc in dataList) {
                if(doc.productName!!.lowercase().contains(query.lowercase())==true) {
                    searchlist.add(doc)
                }else {
                    binding.searchrecycler.visibility=View.GONE
                }
            }
            binding.searchrecycler.visibility=View.VISIBLE
            binding.searchrecycler.adapter=searchRecycle(this,searchlist)
        }
    }


}