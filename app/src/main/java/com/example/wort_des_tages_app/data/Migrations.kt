package com.example.wort_des_tages_app.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Migrate from version 1 to version 2
// Add a new column "word_of_the_day" to the "words" table
val MIGRATION_1_2 = object : Migration(2, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
//        db.execSQL("")
    }
}