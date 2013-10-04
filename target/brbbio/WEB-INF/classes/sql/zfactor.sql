SELECT pl.plate_name, a.time_marker, #function# as z_factor 
FROM plates pl
JOIN (
SELECT c.plate_id, c.identifier, c.time_marker, c.data, c.identifier as type
	FROM raw_data_controls c
) a ON a.plate_id = pl.id
WHERE pl.run_id = ?
GROUP BY pl.plate_name, a.time_marker
ORDER BY pl.plate_name, a.time_marker ASC