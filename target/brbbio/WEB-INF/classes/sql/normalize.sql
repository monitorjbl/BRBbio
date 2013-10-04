SELECT pl.plate_name, a.identifier, a.time_marker, #function# as norm
FROM plates pl
JOIN (
SELECT plate_id, identifier, time_marker, data, 'raw' as type
	FROM raw_data 
UNION ALL
SELECT c.plate_id, rd.identifier, c.time_marker, c.data, c.identifier as type
	FROM raw_data_controls c
	JOIN raw_data rd ON rd.plate_id = c.plate_id AND rd.time_marker = c.time_marker
) a ON a.plate_id = pl.id
WHERE pl.run_id = ?
GROUP BY pl.plate_name, a.identifier, a.time_marker
ORDER BY pl.plate_name, a.identifier, a.time_marker ASC