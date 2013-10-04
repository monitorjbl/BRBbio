SELECT r.* FROM runs r 
	JOIN user_security rs ON rs.run_id = r.id
WHERE rs.user_name = ?