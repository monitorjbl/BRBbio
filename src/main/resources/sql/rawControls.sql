SELECT DISTINCT c.gene_symbol FROM plates p
	JOIN raw_data_controls c ON c.plate_id = p.id 
	JOIN user_security rs ON rs.run_id = p.run_id
WHERE p.run_id=? AND rs.user_name = ?