package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WortDesTagesDao {
    @Query("""INSERT INTO wort_des_tages(fk_wort1, fk_wort2, fk_wort3, fk_wort4, fk_wort5, date) VALUES( 
           (SELECT id FROM wort ORDER BY RANDOM() LIMIT 1), 
           (SELECT id FROM wort ORDER BY RANDOM() LIMIT 1), 
           (SELECT id FROM wort ORDER BY RANDOM() LIMIT 1), 
           (SELECT id FROM wort ORDER BY RANDOM() LIMIT 1), 
           (SELECT id FROM wort ORDER BY RANDOM() LIMIT 1), 
           date('now', 'localtime'))""")
    suspend fun createWortDesTages(): Long
}