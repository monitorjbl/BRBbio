SELECT pl.plate_name, rd.identifier, null as time_marker, rd.data/n.ctrl as norm
	FROM cell_viability rd 
	JOIN plates pl ON pl.id = rd.plate_id
	JOIN (SELECT plate_id, AVG(data) ctrl FROM controls WHERE control_type = 'negative_control' GROUP BY plate_id) n ON n.plate_id = rd.plate_id
WHERE pl.run_id = ?
ORDER BY pl.plate_name, rd.identifier ASC