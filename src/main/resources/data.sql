-- users
INSERT INTO tinyhuman_db.users (is_deleted, status, created_at, id, last_login_at, updated_at, created_by, email, name, password, updated_by) VALUES (false, 'ACTIVE', '2023-08-17 15:27:48.051351', 1, null, '2023-08-17 15:27:48.051351', null, 'godshin@gmail.com', '신', '1234', null);
INSERT INTO tinyhuman_db.users (is_deleted, status, created_at, id, last_login_at, updated_at, created_by, email, name, password, updated_by) VALUES (false, 'ACTIVE', '2023-08-17 15:27:48.051351', 2, null, '2023-08-17 15:27:48.051351', null, 'kimreal@gmail.com', '김진짜', '1234', null);

-- babies
INSERT INTO tinyhuman_db.babies (day_of_birth, is_deleted, time_of_birth, created_at, id, updated_at, user_id, created_by, gender, name, nick_name, profile_img_url, updated_by) VALUES ('2022-09-27', false, 14, '2023-08-17 15:27:50.345741', 1, '2023-08-17 15:27:50.345741', null, null, 'FEMALE', '신지안', '리카', 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/profile/DSCF7306.JPG', null);
INSERT INTO tinyhuman_db.babies (day_of_birth, is_deleted, time_of_birth, created_at, id, updated_at, user_id, created_by, gender, name, nick_name, profile_img_url, updated_by) VALUES ('2025-03-12', false, 10, '2023-08-17 15:37:01.867475', 2, '2023-08-17 15:37:01.867475', null, null, 'MALE', '신지구', '칭이', 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/profile/DSCF7306.JPG', null);
INSERT INTO tinyhuman_db.babies (day_of_birth, is_deleted, time_of_birth, created_at, id, updated_at, user_id, created_by, gender, name, nick_name, profile_img_url, updated_by) VALUES ('2023-08-12', false, 10, '2023-08-17 15:37:01.867475', 3, '2023-08-17 15:37:01.867475', null, null, 'MALE', '김아들', '우왕', 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/profile/DSCF7306.JPG', null);

-- user-baby-relations
INSERT INTO tinyhuman_db.user_baby_relations (is_deleted, baby_id, created_at, updated_at, user_id, created_by, relation, role, updated_by) VALUES (false, 1, '2023-08-17 15:27:50.382922', '2023-08-17 15:27:50.382922', 1, null, 'FATHER', 'ADMIN', null);
INSERT INTO tinyhuman_db.user_baby_relations (is_deleted, baby_id, created_at, updated_at, user_id, created_by, relation, role, updated_by) VALUES (false, 2, '2023-08-17 15:37:01.892436', '2023-08-17 15:37:01.892436', 1, null, 'FATHER', 'ADMIN', null);
INSERT INTO tinyhuman_db.user_baby_relations (is_deleted, baby_id, created_at, updated_at, user_id, created_by, relation, role, updated_by) VALUES (false, 3, '2023-08-17 15:37:01.892436', '2023-08-17 15:37:01.892436', 2, null, 'MOTHER', 'ADMIN', null);
