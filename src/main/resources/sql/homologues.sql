SELECT DISTINCT rd.gene_id, rd.gene_symbol, h.gene_id as h_gene_id, h.gene_symbol as h_gene_symbol
FROM raw_data rd
    JOIN ncbi_homologue h
    ON h.gene_id = rd.gene_id
    JOIN plates p
    ON p.id = rd.plate_id
WHERE p.run_id = ? AND h.taxonomy_id = ?
ORDER BY rd.gene_symbol