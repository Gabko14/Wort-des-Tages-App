package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WortDao {
    @Query("""SELECT * FROM wort WHERE id IN (
            SELECT fk_wort1 FROM wort_des_tages WHERE date = date('now', 'localtime')
            UNION ALL
            SELECT fk_wort2 FROM wort_des_tages WHERE date = date('now', 'localtime')
            UNION ALL
            SELECT fk_wort3 FROM wort_des_tages WHERE date = date('now', 'localtime')
            UNION ALL
            SELECT fk_wort4 FROM wort_des_tages WHERE date = date('now', 'localtime')
            UNION ALL
            SELECT fk_wort5 FROM wort_des_tages WHERE date = date('now', 'localtime')
           )""")
    suspend fun getWortDesTages(): List<Wort>
}