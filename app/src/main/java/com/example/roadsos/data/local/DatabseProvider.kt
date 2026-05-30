package com.example.roadsos.data.local

import android.content.Context
import android.util.Log
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream

object DatabaseProvider {

    private const val DB_NAME = "roadsos_india.db"

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {

        copyDatabaseFromAssetsIfNeeded(context)

        return INSTANCE ?: synchronized(this) {

            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .build()

            INSTANCE = instance

            instance
        }
    }

    private fun copyDatabaseFromAssetsIfNeeded(context: Context) {

        val dbFile: File =
            context.getDatabasePath(DB_NAME)

        if (dbFile.exists() && dbFile.length() > 0) {
            Log.d(
                "DB_COPY",
                "Database already exists: ${dbFile.absolutePath}"
            )
            return
        }

        try {

            dbFile.parentFile?.mkdirs()

            context.assets.open(DB_NAME).use { input ->

                FileOutputStream(dbFile).use { output ->

                    input.copyTo(output)
                }
            }

            Log.d(
                "DB_COPY",
                "Database copied successfully: ${dbFile.absolutePath}"
            )

        } catch (e: Exception) {

            Log.e(
                "DB_COPY",
                "Manual DB copy failed: ${e.message}",
                e
            )
        }
    }
}