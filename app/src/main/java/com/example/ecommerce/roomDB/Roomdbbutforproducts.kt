package com.example.ecommerce.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [productsbutinDB::class], version = 3,exportSchema = false)
abstract class Roomdbbutforproducts:RoomDatabase() {

    companion object {

        val migration1_2=object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Products ADD COLUMN ProductCategory TEXT NOT NULL DEFAULT 'undefined'")
            }
        }
        val migration2_3=object :Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Products ADD COLUMN Price INT NOT NULL DEFAULT 0")
            }
        }



        private var Databse:Roomdbbutforproducts?=null
        private var DATABASE_NAME="CartProducts"

        @Synchronized
        fun getInstance(context: Context):Roomdbbutforproducts {





            @Volatile
            if(Databse==null) {
                     Databse =Room.databaseBuilder(
                         context.applicationContext,
                         Roomdbbutforproducts::class.java,
                         DATABASE_NAME
                     ).addMigrations(migration1_2, migration2_3).build()
            }
            return Databse!!
        }
    }

    abstract fun productDao():productsButinDbDao
}