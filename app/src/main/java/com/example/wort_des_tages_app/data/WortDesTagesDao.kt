package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WortDesTagesDao {
    @Query("""WITH 
        subquery1 AS (
            SELECT id 
            FROM wort 
            WHERE frequenzklasse >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM() 
            LIMIT 5
        ),
        subquery2 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Substantiv" 
              AND frequenzklasse >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountSubstantiv FROM user_settings WHERE id = 1)
        ),
        subquery3 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Adjektiv" 
              AND frequenzklasse >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountAdjektiv FROM user_settings WHERE id = 1)
        ),
        subquery4 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Verb" 
              AND frequenzklasse >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountVerb FROM user_settings WHERE id = 1)
        ),
        subquery5 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Adverb" 
              AND frequenzklasse >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountAdverb FROM user_settings WHERE id = 1)
        ),
        subquery6 AS (
            SELECT id
            FROM wort
            WHERE (wortklasse = "Mehrwortausdruck" OR wortklasse IS NULL) 
              AND frequenzklasse >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountMehrwortausdruckOrNull FROM user_settings WHERE id = 1)
        ),
        combined_results AS (
            SELECT id FROM subquery1
            UNION ALL
            SELECT id FROM subquery2
            UNION ALL
            SELECT id FROM subquery3
            UNION ALL
            SELECT id FROM subquery4
            UNION ALL
            SELECT id FROM subquery5    
            UNION ALL
            SELECT id FROM subquery6
        ),
        numbered_results AS (
            SELECT id,
                   (SELECT COUNT(*) 
                    FROM combined_results cr2 
                    WHERE cr2.id <= cr.id) AS rn
            FROM combined_results cr
        )
        -- Insert results into wort_des_tages
        INSERT OR REPLACE INTO wort_des_tages (fk_wort1, fk_wort2, fk_wort3, fk_wort4, fk_wort5, date)
        SELECT
            MAX(CASE WHEN rn = 1 THEN id END) AS fk_wort1,
            MAX(CASE WHEN rn = 2 THEN id END) AS fk_wort2,
            MAX(CASE WHEN rn = 3 THEN id END) AS fk_wort3,
            MAX(CASE WHEN rn = 4 THEN id END) AS fk_wort4,
            MAX(CASE WHEN rn = 5 THEN id END) AS fk_wort5,
            date('now', 'localtime')
        FROM numbered_results;""")
    suspend fun createWortDesTages(): Long
}