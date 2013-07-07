select p_data.plate_name, rd.identifier, rd.time_marker, (rd.data/p_data.positive_control)/(p_data.negative_control/p_data.positive_control) as norm from (
	select r.id as run_id, p.id as plate_id, p.plate_name, ctrl.time_marker, ctrl.positive_control, ctrl.negative_control
		from runs r
		join plates p on p.run_id = r.id 
		join controls ctrl on ctrl.plate_id = p.id
) p_data join raw_data rd on rd.plate_id = p_data.plate_id and rd.time_marker = p_data.time_marker
where p_data.run_id = ?
order by p_data.plate_name, rd.identifier, rd.time_marker ASC