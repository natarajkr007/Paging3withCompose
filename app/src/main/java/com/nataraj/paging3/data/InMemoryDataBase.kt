package com.nataraj.paging3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author nataraj-7085
 * @since 07/10/25
 * */
@Database(
    entities = [DummyProduct::class],
    version = 1,
    exportSchema = false
)
abstract class InMemoryDataBase : RoomDatabase() {
    abstract fun dummyProductsDao(): DummyProductsDao
}

object InMemoryDatabaseProvider {
    lateinit var INSTANCE: InMemoryDataBase

    fun init(context: Context) {
        synchronized(this) {
            val instance = Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                InMemoryDataBase::class.java
            ).build()

            INSTANCE = instance
        }
    }
}