CREATE VIEW avg_control AS
SELECT plate_id, control_type, time_marker, AVG(data) AS ctrl 
   FROM controls 
GROUP BY plate_id, control_type, time_marker;

CREATE VIEW std_control AS
SELECT plate_id, control_type, time_marker, STDDEV_POP(data) AS ctrl 
   FROM controls 
GROUP BY plate_id, control_type, time_marker;