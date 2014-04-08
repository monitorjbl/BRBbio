insert into hts_system_info values('version','${project.version}');

insert into users (user_name,first_name,last_name,password_hash,active,admin)
    values('admin','Administrator','',MD5('admin'),1,1);