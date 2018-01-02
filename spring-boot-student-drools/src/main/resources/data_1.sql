-- 用户表
CREATE TABLE `person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `age` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- 规则表
CREATE TABLE `rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rule_key` varchar(255) NOT NULL DEFAULT '' COMMENT '规则编码',
  `version` varchar(255) NOT NULL DEFAULT '' COMMENT '规则编码',
  `content` varchar(2048) NOT NULL DEFAULT '' COMMENT '规则n内容',
  `create_time` varchar(255) NOT NULL,
  `update_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_version` (`version`) USING BTREE,
  UNIQUE KEY `uk_rule_key` (`rule_key`) USING BTREE,
  KEY `uk_update_time` (`update_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- 文件名修改成data.sql放到resources文件夹下才会执行
insert into person(name,age,address) values('wyf',32,'合肥');
insert into person(name,age,address) values('xx',31,'北京');
insert into person(name,age,address) values('yy',30,'上海');
insert into person(name,age,address) values('zz',29,'南京');
insert into person(name,age,address) values('aa',28,'武汉');
insert into person(name,age,address) values('bb',27,'合肥');

-- 插入规则
INSERT INTO `rule` (`id`, `rule_key`, `version`, `content`, `create_time`, `update_time`) VALUES ('1', 'score', '1', 'package plausibcheck.adress\n\nimport com.neo.drools.model.Address;\nimport com.neo.drools.model.fact.AddressCheckResult;\n\nrule \"Postcode 6 numbers\"\n\n    when\n        address : Address(postcode != null, postcode matches \"([0-9]{6})\")\n        checkResult : AddressCheckResult();\n    then\n        checkResult.setPostCodeResult(true);\n		System.out.println(\"规则6中打印日志：校验通过!\");\nend', '111', '111');
