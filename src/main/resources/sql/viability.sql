SELECT pl.plate_name, a.identifier, null as time_marker, #function# as norm
FROM plates pl
JOIN (
SELECT plate_id, identifier, data, 'raw' as type
	FROM cell_viability 
UNION ALL
SELECT c.plate_id, rd.identifier, c.data, c.identifier as type
	FROM cell_viability_controls c
	JOIN raw_data rd ON rd.plate_id = c.plate_id
) a ON a.plate_id = pl.id
WHERE pl.run_id = ?
GROUP BY pl.plate_name, a.identifier