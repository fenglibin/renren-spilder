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


insert into types(id,name,ename,description) values(1,'����С˵','xhxs','����С˵');
insert into types(id,name,ename,description) values(2,'��ԽС˵','cyxs','��ԽС˵');
insert into types(id,name,ename,description) values(3,'�������','ynyj','�������');
insert into types(id,name,ename,description) values(4,'��ʷ����','lsjs','��ʷ����');
insert into types(id,name,ename,description) values(5,'��������','wxxx','��������');
insert into types(id,name,ename,description) values(6,'���и���','dsqg','���и���');
insert into types(id,name,ename,description) values(7,'������Ϸ','wyyx','������Ϸ');
insert into types(id,name,ename,description) values(8,'������ѧ','cjwx','������ѧ');
insert into types(id,name,ename,description) values(9,'������ѧ ','cswx','������ѧ ');
insert into types(id,name,ename,description) values(10,'�ƻ�С˵ ','khxs','�ƻ�С˵ ');
insert into types(id,name,ename,description) values(11,'�걾���','wbzc','�걾���');


insert into types(id,name,ename,description) values(1,'����С�f','xhxs','����С�f');
insert into types(id,name,ename,description) values(2,'��ԽС�f','cyxs','��ԽС�f');
insert into types(id,name,ename,description) values(3,'���ܮ���','ynyj','���ܮ���');
insert into types(id,name,ename,description) values(4,'�vʷ܊��','lsjs','�vʷ܊��');
insert into types(id,name,ename,description) values(5,'��b�ɂb','wxxx','��b�ɂb');
insert into types(id,name,ename,description) values(6,'���и���','dsqg','���и���');
insert into types(id,name,ename,description) values(7,'�W���[��','wyyx','�W���[��');
insert into types(id,name,ename,description) values(8,'�����ČW','cjwx','�����ČW');
insert into types(id,name,ename,description) values(9,'�����ČW','cswx','�����ČW');
insert into types(id,name,ename,description) values(10,'�ƻ�С�f','khxs','�ƻ�С�f');
insert into types(id,name,ename,description) values(11,'�걾���','wbzc','�걾���');

