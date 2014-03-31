insert into hts_version_info values('revision','${scmVersion}');
insert into hts_version_info values('time','${timestamp}');

insert into users (user_name,first_name,last_name,password_hash,active,admin)
    values('admin','Administrator','',MD5('admin'),1,1);