SELECT pl.plate_name, a.gene_id, a.gene_symbol, null as time_marker, #function# as norm
FROM plates pl
	JOIN (
		SELECT c.plate_id, rd.gene_id, rd.gene_symbol, c.data as control, rd.data as raw_data, c.gene_symbol as type
			FROM cell_viability_controls c
			JOIN cell_viability rd ON rd.plate_id = c.plate_id
	) a ON a.plate_id = pl.id
	JOIN user_security rs ON rs.run_id = pl.run_id
WHERE pl.run_id = ? and rs.user_name = ?
GROUP BY pl.plate_name, a.gene_symbol