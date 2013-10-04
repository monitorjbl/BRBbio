SELECT pl.plate_name, a.identifier, null as time_marker, #function# as norm
FROM plates pl
	JOIN (
		SELECT plate_id, identifier, data, 'raw' as type
			FROM cell_viability 
		UNION ALL
		SELECT c.plate_id, rd.identifier, c.data, c.identifier as type
			FROM cell_viability_controls c
			JOIN cell_viability rd ON rd.plate_id = c.plate_id
	) a ON a.plate_id = pl.id
	JOIN user_security rs ON rs.run_id = pl.run_id
WHERE pl.run_id = ? and rs.user_name = ?
GROUP BY pl.plate_name, a.identifier