drop database ssafy_mobile_cafe;
select @@global.transaction_isolation, @@transaction_isolation;
set @@transaction_isolation="read-committed";

create database ssafy_mobile_cafe;
use ssafy_mobile_cafe;

create table t_user(
	id varchar(100) primary key,
    name varchar(100) not null,
    pass varchar(100) not null,
    stamps integer default 0
);
create table t_product(
	id integer auto_increment primary key,
    name varchar(100) not null,
    type varchar(20) not null,
    price integer not null,
    img varchar(100) not null
);


create  table t_order(
	o_id integer auto_increment primary key,
    user_id varchar(100) not null,
    order_table varchar(20),
    order_time timestamp default CURRENT_TIMESTAMP,    
    completed char(1) default 'N',
    constraint fk_order_user foreign key (user_id) references t_user(id) on delete cascade
);

create table t_order_detail(
	d_id integer auto_increment primary key,
    order_id integer not null,
    product_id integer not null,
    quantity integer not null default 1,
    constraint fk_order_detail_product foreign key (product_id) references t_product(id) on delete cascade,
    constraint fk_order_detail_order foreign key(order_id) references t_order(o_id) on delete cascade
);                                                 

create table t_stamp(
	id integer auto_increment primary key,
    user_id varchar(100) not null,
    order_id integer not null,
    quantity integer not null default 1,
    constraint fk_stamp_user foreign key (user_id) references t_user(id) on delete cascade,
    constraint fk_stamp_order foreign key (order_id) references t_order(o_id) on delete cascade
);

create table t_comment(
	id integer auto_increment primary key,
    user_id varchar(100) not null,
    product_id integer not null,
    rating float not null default 1,
    comment varchar(200),
    constraint fk_comment_user foreign key(user_id) references t_user(id) on delete cascade,
    constraint fk_comment_product foreign key(product_id) references t_product(id) on delete cascade
);

select * from t_user;

INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 01', 'name 01', 'pass 01', 4);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 02', 'name 02', 'pass 02', 1);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 03', 'name 03', 'pass 03', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 04', 'name 04', 'pass 04', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 05', 'name 05', 'pass 05', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 06', 'name 06', 'pass 06', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 07', 'name 07', 'pass 07', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 08', 'name 08', 'pass 08', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 09', 'name 09', 'pass 09', 0);
INSERT INTO t_user (id, name, pass, stamps) VALUES ('id 10', 'name 10', 'pass 10', 0);

INSERT INTO t_product (name, type, price, img) VALUES ('아메리카노', 'coffee', 2000, 'coffee1.png');
INSERT INTO t_product (name, type, price, img) VALUES ('카페라떼', 'coffee', 2300, 'coffee2.png');
INSERT INTO t_product (name, type, price, img) VALUES ('카라멜 마끼야또', 'coffee', 2500, 'coffee3.png');
INSERT INTO t_product (name, type, price, img) VALUES ('카포치노', 'coffee', 2800, 'coffee4.png');
INSERT INTO t_product (name, type, price, img) VALUES ('모카라떼', 'coffee', 3000, 'coffee5.png');
INSERT INTO t_product (name, type, price, img) VALUES ('민트라떼', 'coffee', 3000, 'coffee6.png');
INSERT INTO t_product (name, type, price, img) VALUES ('화이트 모카라떼', 'coffee', 2500, 'coffee7.png');
INSERT INTO t_product (name, type, price, img) VALUES ('자몽에이드', 'tea', 2500, 'coffee8.png');
INSERT INTO t_product (name, type, price, img) VALUES ('레몬에이드', 'tea', 3000, 'coffee9.png');
INSERT INTO t_product (name, type, price, img) VALUES ('쿠키', 'cookie', 1500, 'cookie.png');

commit;
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 01', 01, 01, 'comment 01');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 02', 01, 02, 'comment 02');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 03', 01, 03, 'comment 03');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 04', 04, 04, 'comment 04');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 05', 05, 05, 'comment 05');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 06', 06, 06, 'comment 06');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 07', 07, 07, 'comment 07');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 08', 08, 08, 'comment 08');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 09', 09, 09, 'comment 09');
INSERT INTO t_comment (user_id, product_id, rating, comment) VALUES ('id 10', 10, 10, 'comment 10');

INSERT INTO t_order (user_id, order_table) VALUES ('id 01', 'order_table 01');
INSERT INTO t_order (user_id, order_table) VALUES ('id 02', 'order_table 02');
INSERT INTO t_order (user_id, order_table) VALUES ('id 03', 'order_table 03');
INSERT INTO t_order (user_id, order_table) VALUES ('id 04', 'order_table 04');
INSERT INTO t_order (user_id, order_table) VALUES ('id 05', 'order_table 05');
INSERT INTO t_order (user_id, order_table) VALUES ('id 06', 'order_table 06');
INSERT INTO t_order (user_id, order_table) VALUES ('id 07', 'order_table 07');
INSERT INTO t_order (user_id, order_table) VALUES ('id 08', 'order_table 08');
INSERT INTO t_order (user_id, order_table) VALUES ('id 09', 'order_table 09');
INSERT INTO t_order (user_id, order_table) VALUES ('id 10', 'order_table 10');

INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (01, 01, 01);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (01, 02, 03);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (02, 01, 01);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (03, 03, 03);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (04, 04, 04);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (05, 05, 05);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (06, 06, 06);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (07, 07, 07);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (08, 08, 08);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (09, 09, 09);
INSERT INTO t_order_detail (order_id, product_id, quantity) VALUES (10, 10, 10);

INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 01', 1, 04);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 02', 2, 01);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 03', 3, 03);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 04', 4, 04);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 05', 5, 05);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 06', 6, 06);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 07', 7, 07);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 08', 8, 08);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 09', 9, 09);
INSERT INTO t_stamp (user_id, order_id, quantity) VALUES ('id 10', 10, 10);

select * from t_order;

commit;

select * from t_stamp;

select * from t_user;