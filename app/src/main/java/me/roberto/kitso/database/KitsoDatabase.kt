package me.roberto.kitso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.roberto.kitso.model.BookItem

@Database(entities = [(BookItem::class)],version = 1)
abstract class KitsoDatabase: RoomDatabase() {

    abstract fun KitsoDatabase_Impl():BookItemDao


    companion object {

        @Volatile
        private var INSTANCE:KitsoDatabase? = null

        fun getInstance(context: Context): KitsoDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        KitsoDatabase::class.java, "Data.db")
                        .build()
    }

}