-- users
INSERT INTO tinyhuman_db.users (is_deleted, status, created_at, id, last_login_at, updated_at, created_by, email, name, password, updated_by) VALUES (false, 'ACTIVE', '2023-08-17 15:27:48.051351', 1, null, '2023-08-17 15:27:48.051351', null, 'godshin@gmail.com', '신', '1234', null);
INSERT INTO tinyhuman_db.users (is_deleted, status, created_at, id, last_login_at, updated_at, created_by, email, name, password, updated_by) VALUES (false, 'ACTIVE', '2023-08-17 15:27:48.051351', 2, null, '2023-08-17 15:27:48.051351', null, 'kimreal@gmail.com', '김진짜', '1234', null);
INSERT INTO tinyhuman_db.users (is_deleted, created_at, id, last_login_at, updated_at, created_by, email, name, password, status, updated_by) VALUES (false, '2023-08-30 16:58:18.491488', 3, null, '2023-08-30 16:58:18.491488', null, 'shinsanghooon@gmail.com', '신상훈', '$2a$04$weOfsS5XBbblFiIYRuzmW.7rpRoVTTx9vIkAIp1HNw32z4xJFh8xO', 'ACTIVE', null);

-- babies
INSERT INTO tinyhuman_db.babies (day_of_birth, is_deleted, time_of_birth, created_at, id, updated_at, user_id, created_by, gender, name, nick_name, profile_img_url, updated_by) VALUES ('2022-09-27', false, 14, '2023-08-17 15:27:50.345741', 1, '2023-08-17 15:27:50.345741', null, null, 'FEMALE', '신지안', '리카', 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/profile/DSCF7306.JPG', null);
INSERT INTO tinyhuman_db.babies (day_of_birth, is_deleted, time_of_birth, created_at, id, updated_at, user_id, created_by, gender, name, nick_name, profile_img_url, updated_by) VALUES ('2025-03-12', false, 10, '2023-08-17 15:37:01.867475', 2, '2023-08-17 15:37:01.867475', null, null, 'MALE', '신지구', '칭이', 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/profile/DSCF7306.JPG', null);
INSERT INTO tinyhuman_db.babies (day_of_birth, is_deleted, time_of_birth, created_at, id, updated_at, user_id, created_by, gender, name, nick_name, profile_img_url, updated_by) VALUES ('2023-08-12', false, 10, '2023-08-17 15:37:01.867475', 3, '2023-08-17 15:37:01.867475', null, null, 'MALE', '김아들', '우왕', 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/profile/DSCF7306.JPG', null);

-- user-baby-relations
INSERT INTO tinyhuman_db.user_baby_relations (is_deleted, baby_id, created_at, updated_at, user_id, created_by, relation, role, updated_by) VALUES (false, 1, '2023-08-17 15:27:50.382922', '2023-08-17 15:27:50.382922', 1, null, 'FATHER', 'ADMIN', null);
INSERT INTO tinyhuman_db.user_baby_relations (is_deleted, baby_id, created_at, updated_at, user_id, created_by, relation, role, updated_by) VALUES (false, 2, '2023-08-17 15:37:01.892436', '2023-08-17 15:37:01.892436', 1, null, 'FATHER', 'ADMIN', null);
INSERT INTO tinyhuman_db.user_baby_relations (is_deleted, baby_id, created_at, updated_at, user_id, created_by, relation, role, updated_by) VALUES (false, 3, '2023-08-17 15:37:01.892436', '2023-08-17 15:37:01.892436', 2, null, 'MOTHER', 'ADMIN', null);

-- diaries

-- sentences
-- INSERT INTO tinyhuman_db.sentences (created_at, diary_id, id, updated_at, sentence, created_by, updated_by) VALUES ('2023-08-18 02:22:09.850124', 3, 7, '2023-08-18 02:22:09.850124', '오늘은 지안이가 일어서서 움직이기 시작했다', null, null);
-- INSERT INTO tinyhuman_db.sentences (created_at, diary_id, id, updated_at, sentence, created_by, updated_by) VALUES ('2023-08-18 02:22:09.860375', 3, 8, '2023-08-18 02:22:09.860375', '날이 갈수록 트월킹이 심해지고 있다', null, null);
-- INSERT INTO tinyhuman_db.sentences (created_at, diary_id, id, updated_at, sentence, created_by, updated_by) VALUES ('2023-08-18 02:22:09.864697', 3, 9, '2023-08-18 02:22:09.864697', '밤에 왜 이렇게 깨는지 이해할 수가 없다.', null, null);

-- pictures
-- INSERT INTO tinyhuman_db.pictures (is_main_picture, created_at, diary_id, id, updated_at, content_type, created_by, original_s3_url, updated_by) VALUES (true, '2023-08-18 02:22:10.027063', 3, 1, '2023-08-18 02:22:10.027063', 'PICTURE', null, 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/diary/DSCF7305.JPG', null);
-- INSERT INTO tinyhuman_db.pictures (is_main_picture, created_at, diary_id, id, updated_at, content_type, created_by, original_s3_url, updated_by) VALUES (false, '2023-08-18 02:22:10.033521', 3, 2, '2023-08-18 02:22:10.033521', 'PICTURE', null, 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/diary/DSCF7298.JPG', null);
-- INSERT INTO tinyhuman_db.pictures (is_main_picture, created_at, diary_id, id, updated_at, content_type, created_by, original_s3_url, updated_by) VALUES (false, '2023-08-18 02:22:10.040841', 3, 3, '2023-08-18 02:22:10.040841', 'PICTURE', null, 'https://tiny-human-dev.s3.ap-northeast-2.amazonaws.com/images/1/diary/DSCF7402.JPG', null);
