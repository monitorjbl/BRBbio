SELECT pl.plate_name, a.time_marker, #function# as z_factor 
FROM plates pl
	JOIN (
		SELECT c.plate_id, c.identifier, c.time_marker, c.data as control, c.identifier as type
			FROM raw_data_controls c
	) a ON a.plate_id = pl.id
	JOIN user_security rs ON rs.run_id = pl.run_id
WHERE pl.run_id = ? and rs.user_name = ?
GROUP BY pl.plate_name, a.time_marker
ORDER BY pl.plate_name, a.time_marker ASC