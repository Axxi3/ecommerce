package com.example.ecommerce.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface productsButinDbDao {

    @Insert
    suspend fun insertProduct(product:productsbutinDB)

@Delete
    suspend fun deleteproduct(product: productsbutinDB)

@Query("SELECT * FROM Products")
    fun getAllproduct():LiveData<List<productsbutinDB>>

    @Query("SELECT * FROM Products WHERE productid=:id")
    fun isreal(id:String):productsbutinDB
    @Query("UPDATE Products SET Price=:price WHERE productid=:productId")
    suspend fun updatePrice(productId: String, price: Int)

    @Query("SELECT Price FROM Products WHERE productid = :id")
    fun getliveprice(id:String):Int

    @Query("UPDATE Products SET Price = :newPrice WHERE productSp = :productSpValue")
    fun updatePriceByProductSp(productSpValue: String, newPrice: Int)

}