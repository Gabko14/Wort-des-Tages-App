package com.example.wort_des_tages_app.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface WortDesTagesDao {
	@Query("""WITH
		subquery2 AS (
			SELECT id
			FROM wort
			WHERE wortklasse = 'Substantiv'
			  AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
			ORDER BY RANDOM()
			LIMIT (SELECT amountSubstantiv FROM user_settings WHERE id = 1)
		),
		subquery3 AS (
			SELECT id
			FROM wort
			WHERE wortklasse = 'Adjektiv'
			  AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
			ORDER BY RANDOM()
			LIMIT (SELECT amountAdjektiv FROM user_settings WHERE id = 1)
		),
		subquery4 AS (
			SELECT id
			FROM wort
			WHERE wortklasse = 'Verb'
			  AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
			ORDER BY RANDOM()
			LIMIT (SELECT amountVerb FROM user_settings WHERE id = 1)
		),
		subquery5 AS (
			SELECT id
			FROM wort
			WHERE wortklasse = 'Adverb'
			  AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
			ORDER BY RANDOM()
			LIMIT (SELECT amountAdverb FROM user_settings WHERE id = 1)
		),
		subquery6 AS (
			SELECT id
			FROM wort
			WHERE (wortklasse = 'Mehrwortausdruck' OR wortklasse IS NULL)
			  AND CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
			ORDER BY RANDOM()
			LIMIT (SELECT amountMehrwortausdruckOrNull FROM user_settings WHERE id = 1)
		),
		filler_needed AS (
			SELECT CAST((SELECT anzahl_woerter FROM user_settings WHERE id = 1) AS INTEGER) AS req
		),
		combined_results_all AS (
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
		unique_results AS (
			SELECT DISTINCT id FROM (
				SELECT id FROM combined_results_all
				UNION ALL
				SELECT id FROM (
					SELECT id FROM wort
					WHERE CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
					ORDER BY RANDOM()
					LIMIT (
						CASE 
							WHEN (SELECT req FROM filler_needed) - (SELECT COUNT(DISTINCT id) FROM combined_results_all) > 0 
							THEN (SELECT req FROM filler_needed) - (SELECT COUNT(DISTINCT id) FROM combined_results_all)
							ELSE 0 
						END
					)
				)
			)
		),
		final_results AS (
			SELECT id FROM unique_results
			UNION ALL
			SELECT id FROM (
				SELECT id FROM wort
				WHERE CASE WHEN frequenzklasse = 'n/a' THEN 0 ELSE CAST(frequenzklasse AS INTEGER) END >= (SELECT minFrequenzklasse FROM user_settings WHERE id = 1)
				  AND id NOT IN (SELECT id FROM unique_results)
				ORDER BY RANDOM()
				LIMIT (
					CASE 
						WHEN 5 - (SELECT COUNT(*) FROM unique_results) > 0 THEN 5 - (SELECT COUNT(*) FROM unique_results)
						ELSE 0
					END
				)
			)
		),
		numbered_results AS (
			SELECT fr.id,
				   (SELECT COUNT(*) FROM final_results fr2 WHERE fr2.id <= fr.id) AS rn
			FROM final_results fr
		)
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