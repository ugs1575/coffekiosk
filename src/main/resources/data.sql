insert into users(deleted, name, email, point, created_date, last_modified_date)
values (false, '우경서', 'test@coffee.kiosk.com', 10000, now(), now());

insert into items (item_type, name, price, last_modified_date_time, created_date, last_modified_date)
values ('COFFEE', '카페라떼', 5000, now(), now(), now()),
       ('COFFEE', '카페모카', 5500, now(), now(), now()),
       ('COFFEE', '아메리카노', 4500, now(), now(), now()),
       ('COFFEE', '카푸치노', 5000, now(), now(), now()),
       ('COFFEE', '카라멜 마키아또', 5900, now(), now(), now()),
       ('COFFEE', '화이트 초콜릿 모카', 5900, now(), now(), now()),
       ('COFFEE', '에스프레소', 4000, now(), now(), now()),
       ('COFFEE', '자바칩 프라푸치노', 6300, now(), now(), now()),
       ('COFFEE', '카라멜 프라푸치노', 5900, now(), now(), now()),
       ('COFFEE', '에스프레소 프라푸치노', 5500, now(), now(), now()),
       ('COFFEE', '디카페인 카페라떼', 5000, now(), now(), now()),
       ('COFFEE', '디카페인 카페모카', 5500, now(), now(), now()),
       ('COFFEE', '디카페인 아메리카노', 4500, now(), now(), now()),
       ('COFFEE', '디카페인 카푸치노', 5000, now(), now(), now()),
       ('COFFEE', '디카페인 카라멜 마키아또', 5900, now(), now(), now()),
       ('COFFEE', '오트 콜드 브루', 5800, now(), now(), now()),
       ('COFFEE', '돌체 콜드 브루', 6000, now(), now(), now()),
       ('COFFEE', '바닐라 크림 콜드 브루', 5800, now(), now(), now()),
       ('COFFEE', '콜드 브루', 4900, now(), now(), now()),
       ('DESSERT', '딸기 초코 레이어 케이크', 7900, now(), now(), now()),
       ('DESSERT', '바스크 치즈 케이크', 7900, now(), now(), now()),
       ('DESSERT', '티라미수 롤', 5900, now(), now(), now()),
       ('DESSERT', '초콜릿 생크림 케이크', 5900, now(), now(), now()),
       ('DESSERT', '마카롱', 2700, now(), now(), now()),
       ('DESSERT', '초코 아이스크림', 3000, now(), now(), now()),
       ('DESSERT', '바닐라 아이스크림', 3000, now(), now(), now()),
       ('NON_COFFEE', '유기농 오렌지 주스', 5100, now(), now(), now()),
       ('NON_COFFEE', '유기농 사과 주스', 5100, now(), now(), now()),
       ('NON_COFFEE', '유자 민트 티', 5900, now(), now(), now()),
       ('NON_COFFEE', '녹차', 5300, now(), now(), now()),
       ('NON_COFFEE', '얼 그레이 티', 4500, now(), now(), now()),
       ('NON_COFFEE', '히비스커스 티', 4500, now(), now(), now()),
       ('NON_COFFEE', '말차 라떼', 6100, now(), now(), now()),
       ('NON_COFFEE', '망고 바나나 블랜디드', 6300, now(), now(), now()),
       ('NON_COFFEE', '피치 요거트 블랜디드', 6100, now(), now(), now()),
       ('NON_COFFEE', '딸기 요거트 블랜디드', 6300, now(), now(), now()),
       ('NON_COFFEE', '망고 패션 티 블랜디드', 5400, now(), now(), now()),
       ('NON_COFFEE', '핫 초콜릿', 5700, now(), now(), now()),
       ('NON_COFFEE', '스팀 우유', 4100, now(), now(), now()),
       ('NON_COFFEE', '딸기 라떼', 6500, now(), now(), now()),
       ('NON_COFFEE', '우유', 4100, now(), now(), now());

insert into cart (item_id, user_id, item_count, created_date, last_modified_date)
values (40, 1, 1, now(), now());

insert into notices (title, content, user_id, registered_date_time, created_date, last_modified_date)
values ('새로 오픈했습니다. 잘 부탁드립니다.', '새로 오픈했습니다. 잘 부탁드립니다.', 1, now(), now(), now()),
       ('오픈이벤트 진행합니다. (12.4 ~ 12.7, 3일간)', '오픈이벤트 진행합니다. (12.4 ~ 12.7, 3일간)', 1, now(), now(), now());