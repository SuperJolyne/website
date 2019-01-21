drop database if exists business;

create database business;

use business;

create table user(
	uid varchar(20) primary key not null,
	name varchar(20) not null,
	sex varchar(2) not null,
	phone varchar(20) not null,
	mail varchar(20) not null,
	pwd varchar(20) not null,
	coupon varchar(20) not null default 'cou:000001',
	address varchar(2000),
	date datetime not null default now()
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table goods(
	gid varchar(30) primary key not null,
	one varchar(20) not null,
	two varchar(20) not null,
	three varchar(50),
	brand varchar(20),
	title varchar(150),
	description varchar(1500),
	discount varchar(10),
	price varchar(10),
	pro_code varchar(40),
	color varchar(10),
	size varchar(30),
	im varchar(2000)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table orders(
	oid varchar(25) primary key not null,
	uid varchar(15) not null,
	gid_size_qua varchar(40) not null,
	order_time varchar(20) not null,
	isPay varchar(10) not null,
	isdelivery varchar(10) not null default '未发货',
	express_name varchar(10),
	express varchar(20),
	discount varchar(10),
	price varchar(10),
	actual varchar(10),
	coupon_id varchar(20)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table brand(
	name varchar(10),
	image varchar(100)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table coupon(
	did varchar(15) not null,
	name varchar(20) not null,
	percent varchar(10) not null,
	quanlity varchar(10) not null,
	dead_time varchar(10)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table comment(
	cid varchar(15) not null,
	uid varchar(15) not null,
	user_name varchar(15) not null,
	oid varchar(15) not null,
	content varchar(100) not null,
	comment_time varchar(20) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


