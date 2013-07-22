SELECT pl.plate_name, a.time_marker, #function# as z_factor 
FROM (
SELECT pl.plate_name, p.time_marker, pl.run_id, p.data, 'positive' as type
	FROM plates pl 
	JOIN controls p ON p.plate_id = pl.id AND p.control_type = 'positive_control'
UNION ALL
SELECT pl.plate_name, n.time_marker, pl.run_id, n.data, 'negative' as type
	FROM plates pl 
	JOIN controls n ON n.plate_id = pl.id AND n.control_type = 'negative_control'
) a
WHERE pl.run_id = ?
GROUP BY pl.plate_name, a.time_marker
ORDER BY pl.plate_name, a.time_marker ASC