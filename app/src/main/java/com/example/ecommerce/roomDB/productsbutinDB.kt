package com.example.ecommerce.roomDB

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Entity(tableName = "Products")
data class productsbutinDB(
@PrimaryKey
@NonNull
val productid:String,
@ColumnInfo(name = "productName")
val productName:String="" ,
@ColumnInfo("ProductImg")
val ProductImg:String="" ,
@ColumnInfo("productSp")
var productSp:String="",
@ColumnInfo("ProductCategory")
val productcategory:String="",
@ColumnInfo("Price")
val Price:Int
) {

}



