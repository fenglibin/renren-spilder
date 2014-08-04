drop database xiaoshuo;
create database xiaoshuo default character set utf8;
use xiaoshuo;
create table types(
	id int primary key auto_increment,
	name varchar(128),
	ename varchar(128),
	description varchar(1024)
);

create table books(
	id int primary key auto_increment,
	type_id int,
	name varchar(128),
	recommend boolean,
	specialRecommend boolean,
	author varchar(50),
	img varchar(128),
	description varchar(1024),
	isFinished boolean,
	spilderUrl varchar(256)
);

create table chapters(
	id int primary key auto_increment,
	book_id int,
	title varchar(256),
	inTime date,
	context text,
	isGenHtml boolean
);
CREATE TABLE downurl (
  `url` varchar(200) NOT NULL,
  inTime date, 
  PRIMARY KEY (`url`)
);

create index spilderUrlIdx on books(spilderUrl);


insert into types(id,name,ename,description) values(1,'玄幻小说','xhxs','玄幻小说');
insert into types(id,name,ename,description) values(2,'穿越小说','cyxs','穿越小说');
insert into types(id,name,ename,description) values(3,'异能异界','ynyj','异能异界');
insert into types(id,name,ename,description) values(4,'历史军事','lsjs','历史军事');
insert into types(id,name,ename,description) values(5,'武侠仙侠','wxxx','武侠仙侠');
insert into types(id,name,ename,description) values(6,'都市感情','dsqg','都市感情');
insert into types(id,name,ename,description) values(7,'网游游戏','wyyx','网游游戏');
insert into types(id,name,ename,description) values(8,'超级文学','cjwx','超级文学');
insert into types(id,name,ename,description) values(9,'重生文学 ','cswx','重生文学 ');
insert into types(id,name,ename,description) values(10,'科幻小说 ','khxs','科幻小说 ');
insert into types(id,name,ename,description) values(11,'完本珍藏','wbzc','完本珍藏');


insert into types(id,name,ename,description) values(1,'玄幻小說','xhxs','玄幻小說');
insert into types(id,name,ename,description) values(2,'穿越小說','cyxs','穿越小說');
insert into types(id,name,ename,description) values(3,'異能異界','ynyj','異能異界');
insert into types(id,name,ename,description) values(4,'歷史軍事','lsjs','歷史軍事');
insert into types(id,name,ename,description) values(5,'武俠仙俠','wxxx','武俠仙俠');
insert into types(id,name,ename,description) values(6,'都市感情','dsqg','都市感情');
insert into types(id,name,ename,description) values(7,'網游遊戲','wyyx','網游遊戲');
insert into types(id,name,ename,description) values(8,'超級文學','cjwx','超級文學');
insert into types(id,name,ename,description) values(9,'重生文學','cswx','重生文學');
insert into types(id,name,ename,description) values(10,'科幻小說','khxs','科幻小說');
insert into types(id,name,ename,description) values(11,'完本珍藏','wbzc','完本珍藏');

