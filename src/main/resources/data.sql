-- charset: utf8

insert into patient (id, name) values (1, '손흥민');
insert into patient (id, name) values (2, '이강인');
insert into patient (id, name) values (3, '황희찬');

insert into doctor (id, name, hospital) values (1, '손웅래', '메라키병원');
insert into doctor (id, name, hospital) values (2, '선재원', '메라키병원');

insert into clinic_service (id, discipline, is_non_benefit, doctor_id) values (1, '정형외과', 0, 1);
insert into clinic_service (id, discipline, is_non_benefit, doctor_id) values (2, '내과', 0, 1);
insert into clinic_service (id, discipline, is_non_benefit, doctor_id) values (3, '일반의', 0, 1);
insert into clinic_service (id, discipline, is_non_benefit, doctor_id) values (4, '한의사', 0, 2);
insert into clinic_service (id, discipline, is_non_benefit, doctor_id) values (5, '일반의', 0, 2);
insert into clinic_service (id, discipline, is_non_benefit, doctor_id) values (6, '다이어트약', 1, 2);

insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (1, '09:00:00', '19:00:00', '11:00:00', '12:00:00', 'MONDAY', 1);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (2, '09:00:00', '19:00:00', '11:00:00', '12:00:00', 'TUESDAY', 1);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (3, '09:00:00', '19:00:00', '11:00:00', '12:00:00', 'WEDNESDAY', 1);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (4, '09:00:00', '19:00:00', '11:00:00', '12:00:00', 'THURSDAY', 1);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (5, '09:00:00', '19:00:00', '11:00:00', '12:00:00', 'FRIDAY', 1);

insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (6, '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'MONDAY', 2);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (7, '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'TUESDAY', 2);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (8, '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'WEDNESDAY', 2);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (9, '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'THURSDAY', 2);
insert into operating_hour (id, start_time, end_time, lunch_start_time, lunch_end_time, day_of_week, doctor_id)
values (10, '08:00:00', '17:00:00', '12:00:00', '13:00:00', 'FRIDAY', 2);
insert into operating_hour (id, start_time, end_time, day_of_week, doctor_id)
values (11, '08:00:00', '13:00:00', 'SATURDAY', 2);


