package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1")
    suspend fun getUserSettings(): UserSettings?

    @Update
    suspend fun updateUserSettings(settings: UserSettings): Int
}