CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `age` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;


-- 文件名修改成data.sql放到resources文件夹下才会执行
insert into person(name,age,address) values('wyf',32,'合肥'); 
insert into person(name,age,address) values('xx',31,'北京'); 
insert into person(name,age,address) values('yy',30,'上海'); 
insert into person(name,age,address) values('zz',29,'南京'); 
insert into person(name,age,address) values('aa',28,'武汉'); 
insert into person(name,age,address) values('bb',27,'合肥');