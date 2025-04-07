INSERT INTO public.app_user (
    index_id, user_age, create_date_time, deleted_date_time, gender, nickname, phone_number, "role",
    security_answer, security_question, user_email, user_password
) VALUES
(2, 25, CURRENT_TIMESTAMP, NULL, '남', '김민수', '010-1000-0001', 'USER', '서울', '어디서 태어났나요?', 'minsu@gmail.com', 'password123'),
(3, 30, CURRENT_TIMESTAMP, NULL, '여', '이혜진', '010-1000-0002', 'USER', '경기도', '어디서 태어났나요?', 'hyejin@gmail.com', 'password123'),
(4, 28, CURRENT_TIMESTAMP, NULL, '남', '박진우', '010-1000-0003', 'USER', '부산', '어디서 태어났나요?', 'jinwoo@gmail.com', 'password123'),
(5, 35, CURRENT_TIMESTAMP, NULL, '여', '최유진', '010-1000-0004', 'USER', '전주', '어디서 태어났나요?', 'yujin@gmail.com', 'password123'),
(6, 22, CURRENT_TIMESTAMP, NULL, '남', '김태호', '010-1000-0005', 'USER', '대구', '어디서 태어났나요?', 'taeho@gmail.com', 'password123'),
(7, 40, CURRENT_TIMESTAMP, NULL, '여', '정수진', '010-1000-0006', 'USER', '인천', '어디서 태어났나요?', 'sujin@gmail.com', 'password123'),
(8, 24, CURRENT_TIMESTAMP, NULL, '남', '홍길동', '010-1000-0007', 'USER', '서울', '어디서 태어났나요?', 'gildong@gmail.com', 'password123'),
(9, 29, CURRENT_TIMESTAMP, NULL, '여', '이민정', '010-1000-0008', 'USER', '광주', '어디서 태어났나요?', 'minjung@gmail.com', 'password123'),
(10, 32, CURRENT_TIMESTAMP, NULL, '남', '김석진', '010-1000-0009', 'USER', '대전', '어디서 태어났나요?', 'seokjin@gmail.com', 'password123'),
(11, 33, CURRENT_TIMESTAMP, NULL, '여', '박보영', '010-1000-0010', 'USER', '서울', '어디서 태어났나요?', 'boyoung@gmail.com', 'password123'),
(12, 25, CURRENT_TIMESTAMP, NULL, '남', '조성현', '010-1000-0011', 'USER', '제주', '어디서 태어났나요?', 'seonghyun@gmail.com', 'password123'),
(13, 31, CURRENT_TIMESTAMP, NULL, '여', '김소영', '010-1000-0012', 'USER', '강원도', '어디서 태어났나요?', 'soyoung@gmail.com', 'password123'),
(14, 27, CURRENT_TIMESTAMP, NULL, '남', '최준호', '010-1000-0013', 'USER', '울산', '어디서 태어났나요?', 'junho@gmail.com', 'password123'),
(15, 26, CURRENT_TIMESTAMP, NULL, '여', '이수진', '010-1000-0014', 'USER', '경상도', '어디서 태어났나요?', 'sujin2@gmail.com', 'password123'),
(16, 23, CURRENT_TIMESTAMP, NULL, '남', '박성민', '010-1000-0015', 'USER', '서울', '어디서 태어났나요?', 'seongmin@gmail.com', 'password123'),
(17, 30, CURRENT_TIMESTAMP, NULL, '여', '유지현', '010-1000-0016', 'USER', '대전', '어디서 태어났나요?', 'jihyun@gmail.com', 'password123'),
(18, 28, CURRENT_TIMESTAMP, NULL, '남', '정진수', '010-1000-0017', 'USER', '부산', '어디서 태어났나요?', 'jinsu@gmail.com', 'password123'),
(19, 36, CURRENT_TIMESTAMP, NULL, '여', '한예슬', '010-1000-0018', 'USER', '인천', '어디서 태어났나요?', 'yesul@gmail.com', 'password123'),
(20, 37, CURRENT_TIMESTAMP, NULL, '남', '이상훈', '010-1000-0019', 'USER', '광주', '어디서 태어났나요?', 'sanghoon@gmail.com', 'password123'),
(21, 26, CURRENT_TIMESTAMP, NULL, '여', '김지원', '010-1000-0020', 'USER', '대구', '어디서 태어났나요?', 'jiwon@gmail.com', 'password123'),
(22, 29, CURRENT_TIMESTAMP, NULL, '남', '한승호', '010-1000-0021', 'USER', '서울', '어디서 태어났나요?', 'seungho@gmail.com', 'password123'),
(23, 22, CURRENT_TIMESTAMP, NULL, '여', '박지영', '010-1000-0022', 'USER', '경기도', '어디서 태어났나요?', 'jiyeong@gmail.com', 'password123'),
(24, 34, CURRENT_TIMESTAMP, NULL, '남', '오준호', '010-1000-0023', 'USER', '전주', '어디서 태어났나요?', 'junho2@gmail.com', 'password123'),
(25, 32, CURRENT_TIMESTAMP, NULL, '여', '정다은', '010-1000-0024', 'USER', '강원도', '어디서 태어났나요?', 'daeun@gmail.com', 'password123'),
(26, 33, CURRENT_TIMESTAMP, NULL, '남', '이호준', '010-1000-0025', 'USER', '부산', '어디서 태어났나요?', 'hojun@gmail.com', 'password123'),
(27, 31, CURRENT_TIMESTAMP, NULL, '여', '김민경', '010-1000-0026', 'USER', '광주', '어디서 태어났나요?', 'minkyung@gmail.com', 'password123'),
(28, 27, CURRENT_TIMESTAMP, NULL, '남', '정상훈', '010-1000-0027', 'USER', '울산', '어디서 태어났나요?', 'sanghoon2@gmail.com', 'password123'),
(29, 24, CURRENT_TIMESTAMP, NULL, '여', '이정은', '010-1000-0028', 'USER', '서울', '어디서 태어났나요?', 'jeongeun@gmail.com', 'password123'),
(30, 35, CURRENT_TIMESTAMP, NULL, '남', '이경수', '010-1000-0029', 'USER', '경상도', '어디서 태어났나요?', 'kyoungsoo@gmail.com', 'password123');

INSERT INTO public.user_webtoon_review
(user_index_id, webtoon_id, is_favorite, is_liked, is_watched, rating)
VALUES
(2, 1, True, False, True, 4),
(3, 2, False, True, False, 5),
(4, 3, True, True, True, 3),
(5, 4, False, False, True, 2),
(6, 5, True, True, False, 1),
(7, 6, False, True, True, 5),
(8, 7, True, False, False, 4),
(9, 8, False, True, True, 3),
(10, 9, True, True, False, NULL),
(11, 10, False, False, True, 4),
(12, 11, True, True, False, 2),
(13, 12, False, True, True, 5),
(14, 13, True, False, False, 3),
(15, 14, False, True, True, 4),
(16, 15, True, True, False, NULL),
(17, 16, False, False, True, 1),
(18, 17, True, True, False, 5),
(19, 18, False, True, True, 2),
(20, 19, True, False, False, 4),
(21, 20, False, True, True, 3),
(22, 21, True, True, False, NULL),
(23, 22, False, False, True, 5),
(24, 23, True, True, False, 2),
(25, 24, False, True, True, 4),
(26, 25, True, False, False, 1),
(27, 26, False, True, True, 5),
(28, 27, True, True, False, 3),
(29, 28, False, True, True, 4),
(30, 29, True, False, False, 2),
(2, 30, False, True, True, 5);

INSERT INTO public.recommend_webtoon
(is_recommended, create_date_time, user_index_id, webtoon_id)
VALUES
(True, '2025-04-07 16:55:00', 1, 257), -- seen_webtoon=1, recom_webtoon=257
(True, '2025-04-07 16:55:00', 1, 143), -- seen_webtoon=1, recom_webtoon=143
(True, '2025-04-07 16:55:00', 1, 740), -- seen_webtoon=1, recom_webtoon=740
(True, '2025-04-07 16:55:00', 2, 105), -- seen_webtoon=2, recom_webtoon=105
(True, '2025-04-07 16:55:00', 2, 1129), -- seen_webtoon=2, recom_webtoon=1129
(True, '2025-04-07 16:55:00', 2, 254), -- seen_webtoon=2, recom_webtoon=254
(True, '2025-04-07 16:55:00', 3, 89), -- seen_webtoon=3, recom_webtoon=89
(True, '2025-04-07 16:55:00', 3, 135), -- seen_webtoon=3, recom_webtoon=135
(True, '2025-04-07 16:55:00', 3, 134), -- seen_webtoon=3, recom_webtoon=134
(True, '2025-04-07 16:55:00', 4, 123), -- seen_webtoon=4, recom_webtoon=123
(True, '2025-04-07 16:55:00', 4, 20), -- seen_webtoon=4, recom_webtoon=20
(True, '2025-04-07 16:55:00', 4, 774), -- seen_webtoon=4, recom_webtoon=774
(True, '2025-04-07 16:55:00', 5, 38), -- seen_webtoon=5, recom_webtoon=38
(True, '2025-04-07 16:55:00', 5, 27), -- seen_webtoon=5, recom_webtoon=27
(True, '2025-04-07 16:55:00', 5, 715); -- seen_webtoon=5, recom_webtoon=715