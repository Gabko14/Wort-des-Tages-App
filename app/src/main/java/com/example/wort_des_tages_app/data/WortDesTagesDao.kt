package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WortDesTagesDao {
    @Query("""WITH
        subquery2 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Substantiv" 
              AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountSubstantiv FROM user_settings WHERE id = 1)
        ),
        subquery3 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Adjektiv" 
              AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountAdjektiv FROM user_settings WHERE id = 1)
        ),
        subquery4 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Verb" 
              AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountVerb FROM user_settings WHERE id = 1)
        ),
        subquery5 AS (
            SELECT id
            FROM wort
            WHERE wortklasse = "Adverb" 
              AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountAdverb FROM user_settings WHERE id = 1)
        ),
        subquery6 AS (
            SELECT id
            FROM wort
            WHERE (wortklasse = "Mehrwortausdruck" OR wortklasse IS NULL) 
              AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
            ORDER BY RANDOM()
            LIMIT (SELECT amountMehrwortausdruckOrNull FROM user_settings WHERE id = 1)
        ),
        subquery1 AS (
            SELECT id FROM wort 
            WHERE CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1) 
            ORDER BY RANDOM() 
            LIMIT ((SELECT anzahl_woerter FROM user_settings WHERE id = 1) - ((SELECT COUNT(*) FROM subquery2) + (SELECT COUNT(*) FROM subquery3) + (SELECT COUNT(*) FROM subquery4) + (SELECT COUNT(*) FROM subquery5) + (SELECT COUNT(*) FROM subquery6)))
        ),
        combined_results AS (
            SELECT id FROM subquery2
            UNION ALL
            SELECT id FROM subquery3
            UNION ALL
            SELECT id FROM subquery4
            UNION ALL
            SELECT id FROM subquery5    
            UNION ALL
            SELECT id FROM subquery6
            UNION ALL
            SELECT id FROM subquery1
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