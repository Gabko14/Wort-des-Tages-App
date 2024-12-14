package com.example.wort_des_tages_app.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE user_settings ADD COLUMN amountSubstantiv INTEGER NOT NULL DEFAULT 1")
        db.execSQL("ALTER TABLE user_settings ADD COLUMN amountAdjektiv INTEGER NOT NULL DEFAULT 2")
        db.execSQL("ALTER TABLE user_settings ADD COLUMN amountVerb INTEGER NOT NULL DEFAULT 1")
        db.execSQL("ALTER TABLE user_settings ADD COLUMN amountAdverb INTEGER NOT NULL DEFAULT 1")
        db.execSQL("ALTER TABLE user_settings ADD COLUMN amountMehrwortausdruckOrNull INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE user_settings ADD COLUMN minFrequenzklasse INTEGER NOT NULL DEFAULT 0")
    }
}