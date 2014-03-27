SELECT time_marker FROM (
  SELECT DISTINCT d.time_marker
    FROM raw_data d
    JOIN plates p ON p.id = d.plate_id
    JOIN user_security sec ON sec.run_id = p.run_id
  WHERE p.run_id = ? AND sec.user_name = ?
  UNION
  SELECT DISTINCT c.time_marker
    FROM raw_data_controls c
    JOIN raw_data d ON d.plate_id = c.plate_id AND d.time_marker = c.time_marker
    JOIN plates p ON p.id = d.plate_id
    JOIN user_security sec ON sec.run_id = p.run_id
  WHERE p.run_id = ? AND sec.user_name = ?
) a ORDER BY time_marker