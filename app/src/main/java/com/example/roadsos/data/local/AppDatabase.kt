package com.example.roadsos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [HospitalEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hospitalDao(): HospitalDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "roadsos_india.db"
                )

                    .createFromAsset("roadsos_india.db")

                    .fallbackToDestructiveMigration()

                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}