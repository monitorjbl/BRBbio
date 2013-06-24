select p.id, p.plate_name, ctrl.time_marker, ctrl.positive_control, ctrl.negative_control
from plates p 
	join controls ctrl on ctrl.plate_id = p.id
	where p.run_id = :id