package com.example.wort_des_tages_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wort")
data class Wort(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lemma: String?,
    val url: String?,
    val wortklasse: String?,
    val artikeldatum: String?,
    val artikeltyp: String?,
    val frequenzklasse: String?,
)