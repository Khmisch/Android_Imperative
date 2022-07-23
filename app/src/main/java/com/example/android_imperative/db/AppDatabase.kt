package com.example.android_imperative.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_imperative.model.TVShow
import java.security.AccessControlContext

@Database(entities = [TVShow::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getTVShowDao(): TVShowDao

    companion object{
        @Volatile
        private var DB_INSTANCE: AppDatabase? = null
         fun getAppDBInstance(context: Context): AppDatabase{
             if (DB_INSTANCE == null){
                 DB_INSTANCE = Room.databaseBuilder(
                     context.applicationContext,
                     AppDatabase::class.java,
                     "DB_TV_SHOW"
                 )
                     .allowMainThreadQueries()
                     .build()
             }
             return DB_INSTANCE!!
         }
    }
}