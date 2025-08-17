package com.example.wort_des_tages_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wort_des_tages_app.shared.DateConverters
import androidx.sqlite.db.SupportSQLiteDatabase

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
                    .createFromAsset("database/dwds.db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // Apply migrations manually if prepackaged DB is missing the new columns
                            val cursor = db.query("PRAGMA table_info('user_settings')")
                            // Determine index of the 'name' column, guaranteed non-negative
                            val nameColumnIndex = cursor.getColumnIndexOrThrow("name")
                            var hasColumn = false
                            while (cursor.moveToNext()) {
                                if (cursor.getString(nameColumnIndex) == "amountSubstantiv") {
                                    hasColumn = true
                                    break
                                }
                            }
                            cursor.close()
                            if (!hasColumn) {
                                MIGRATION_1_2.migrate(db)
                                MIGRATION_2_3.migrate(db)
                            }

                            // Ensure default settings row exists
                            db.execSQL("""
                                INSERT OR IGNORE INTO user_settings (
                                    id, anzahl_woerter, amountSubstantiv, amountAdjektiv, amountVerb, amountAdverb,
                                    amountMehrwortausdruckOrNull, minFrequenzklasse, notificationsEnabled
                                ) VALUES (1, 3, 1, 2, 1, 1, 0, 0, 1);
                            """.trimIndent())
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}