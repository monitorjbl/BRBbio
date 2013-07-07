SELECT pl.plate_name, rd.identifier, rd.time_marker, (rd.data/p.ctrl)/(n.ctrl/p.ctrl) as norm
	FROM raw_data rd 
	JOIN plates pl ON pl.id = rd.plate_id
	JOIN avg_control p ON p.plate_id = rd.plate_id AND p.time_marker = rd.time_marker AND p.control_type = 'positive_control'
	JOIN avg_control n ON n.plate_id = rd.plate_id AND n.time_marker = rd.time_marker AND n.control_type = 'negative_control'
WHERE pl.run_id = ?
ORDER BY pl.plate_name, rd.identifier, rd.time_marker ASC