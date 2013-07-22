SELECT plate_name, identifier, time_marker, #function# as norm
FROM plates pl
JOIN (
SELECT plate_id, identifier, time_marker, data, 'raw' as type
	FROM raw_data 
UNION ALL
SELECT c.plate_id, rd.identifier, c.time_marker, c.data, 'negative' as type
	FROM controls c
	JOIN raw_data rd ON rd.plate_id = c.plate_id AND rd.time_marker = c.time_marker
	WHERE c.control_type = 'negative_control'
UNION ALL
SELECT c.plate_id, rd.identifier, c.time_marker, c.data, 'positive' as type
	FROM controls c
	JOIN raw_data rd ON rd.plate_id = c.plate_id AND rd.time_marker = c.time_marker
	WHERE c.control_type = 'positive_control'
) a ON a.plate_id = pl.id
WHERE pl.run_id = ?
GROUP BY plate_name, identifier, time_marker
ORDER BY plate_name, identifier, time_marker ASC