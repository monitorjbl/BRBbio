SELECT pl.plate_name, pa.time_marker, 1-(3*(ns.ctrl + ps.ctrl))/(na.ctrl-pa.ctrl) as z_factor
	FROM plates pl 
	JOIN avg_control pa ON pa.plate_id = pl.id AND pa.control_type = 'positive_control'
	JOIN avg_control na ON na.plate_id = pl.id AND na.time_marker = pa.time_marker AND na.control_type = 'negative_control'
	JOIN std_control ps ON ps.plate_id = pl.id AND ps.time_marker = pa.time_marker AND ps.control_type = 'positive_control'
	JOIN std_control ns ON ns.plate_id = pl.id AND ns.time_marker = pa.time_marker AND ns.control_type = 'negative_control'
WHERE pl.run_id = ? 
ORDER BY pl.plate_name, pa.time_marker ASC