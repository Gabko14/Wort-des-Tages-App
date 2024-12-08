package com.example.wort_des_tages_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val anzahl_woerter: Int?,
)