package com.example.roadsos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HospitalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hospitalDao(): HospitalDao
}