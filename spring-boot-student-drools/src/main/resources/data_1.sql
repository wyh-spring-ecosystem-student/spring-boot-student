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
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rule_key` (`rule_key`) USING BTREE,
  KEY `uk_update_time` (`update_time`) USING BTREE,
  KEY `uk_version` (`version`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


-- 文件名修改成data.sql放到resources文件夹下才会执行
insert into person(name,age,address) values('wyf',32,'合肥');
insert into person(name,age,address) values('xx',31,'北京');
insert into person(name,age,address) values('yy',30,'上海');
insert into person(name,age,address) values('zz',29,'南京');
insert into person(name,age,address) values('aa',28,'武汉');
insert into person(name,age,address) values('bb',27,'合肥');

-- 插入规则
INSERT INTO `rule` (`id`, `rule_key`, `version`, `content`, `create_time`, `update_time`) VALUES ('1', 'score', '1', 'package com.xiaolyuh.drools.address\r\n\r\nimport com.xiaolyuh.domain.model.Address;\r\nimport com.xiaolyuh.domain.fact.AddressCheckResult;\r\n\r\nrule \"address first 6\"\r\n    no-loop true\r\n    lock-on-active true\r\n    salience 9999999\r\n    when\r\n		eval(true);\r\n    then\r\n        System.out.println(\"执行了规则Address6文件\"); \r\nend\r\n\r\nrule \"Postcode 6 numbers\"\r\n\r\n    when\r\n        address : Address(postcode != null, postcode matches \"([0-9]{6})\")\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        checkResult.setPostCodeResult(true);\r\n		System.out.println(\"规则6中打印日志：校验通过!\");\r\nend', '2018-01-02 15:58:46', '2018-01-02 15:58:46');
INSERT INTO `rule` (`id`, `rule_key`, `version`, `content`, `create_time`, `update_time`) VALUES ('3', 'score1', '2', 'package com.xiaolyuh.drools.address\r\n\r\nimport com.xiaolyuh.domain.model.Address;\r\nimport com.xiaolyuh.domain.fact.AddressCheckResult;\r\n\r\nrule \"address first 7\"\r\n    no-loop true\r\n    lock-on-active true\r\n    salience 9999999\r\n    when\r\n		eval(true);\r\n    then\r\n        System.out.println(\"执行了规则Address7文件\"); \r\nend\r\n\r\nrule \"Postcode 7 numbers\"\r\n\r\n    when\r\n        address : Address(postcode != null, postcode matches \"([0-9]{6})\")\r\n        checkResult : AddressCheckResult();\r\n    then\r\n        checkResult.setPostCodeResult(true);\r\n		System.out.println(\"规则7中打印日志：校验通过!\");\r\nend', '2018-01-02 16:01:55', '2018-01-02 16:01:55');
INSERT INTO `rule` (`id`, `rule_key`, `version`, `content`, `create_time`, `update_time`) VALUES ('4', 'hello world', '1', 'package com.xiaolyuh.drools\r\nimport com.xiaolyuh.domain.model.Message;\r\n\r\nrule \"message first\"\r\n    no-loop true\r\n    lock-on-active true\r\n    salience 9999999\r\n    when\r\n		eval(true);\r\n    then\r\n        System.out.println(\"执行了规则Message文件\"); \r\nend\r\n\r\nrule \"rule1\"\r\n	when\r\n		Message( status == 1, myMessage : msg )\r\n	then\r\n		System.out.println( 1+\":\"+myMessage );\r\nend\r\n', '2018-01-02 16:01:55', '2018-01-02 16:01:55');
