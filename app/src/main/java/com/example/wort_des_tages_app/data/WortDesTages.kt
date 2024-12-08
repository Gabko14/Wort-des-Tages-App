package com.example.wort_des_tages_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "wort_des_tages")
data class WortDesTages(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fk_wort1: Int,
    val fk_wort2: Int,
    val fk_wort3: Int,
    val fk_wort4: Int,
    val fk_wort5: Int,
    val date: Date
)