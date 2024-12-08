package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    suspend fun getUserSettings(): UserSettings?

//    @Query("INSERT OR REPLACE INTO user_settings (id, anzahl_woerter) VALUES (1, :anzahlWoerter)")
    @Query("INSERT OR REPLACE INTO user_settings (id, anzahl_woerter) VALUES (1, :anzahlWoerter)")
    suspend fun setAnzahlWoerter(anzahlWoerter: Int): Long
}