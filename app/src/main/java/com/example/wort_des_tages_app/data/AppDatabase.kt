package com.example.wort_des_tages_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wort_des_tages_app.shared.DateConverters

@Database(
    entities = [Wort::class, WortDesTages::class, UserSettings::class],
    version = 3,
    exportSchema = false,
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wortDao(): WortDao
    abstract fun wortDesTagesDao(): WortDesTagesDao
    abstract fun userSettingsDao(): UserSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    "word_des_tages_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .createFromAsset("database/dwds.db")
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}