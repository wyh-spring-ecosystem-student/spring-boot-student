# [麻烦移步帮我点一下 ```Star```，非常感谢您的支持！](https://github.com/xiaolyuh/layering-cache)([layering-cache](https://github.com/xiaolyuh/layering-cache))
layering-cache是一个支持分布式环境的多级缓存框架，使用方式和spring-cache类似。一级缓存使用Caffeine作为本地缓存，二级缓存使用redis作为集中式缓存。一级缓存和二级缓存的数据一致性是通过推和拉两种模式相结合的方式来实现的。推主要是基于redis的pub/sub机制，拉主要是基于消息队列和记录消费消息的偏移量来实现的。

[https://github.com/xiaolyuh/layering-cache](https://github.com/xiaolyuh/layering-cache)

# 作者信息
- 作者博客：https://xiaolyuh.blog.csdn.net/

- 作者邮箱： xiaolyuh@163.com

- github 地址：https://github.com/xiaolyuh/layering-cache

# 捐赠
项目的发展离不开你的支持，请作者喝杯咖啡吧！

![微信-支付宝](https://img-blog.csdnimg.cn/20200218152559645.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3hpYW9seXVoMTIz,size_16,color_FFFFFF,t_70)

# 技术支持
捐赠100元以上的用户可享有需求优先解决、协助部署、二次开发帮助。

![微信](https://github.com/xiaolyuh/layering-cache/blob/master/images/wechat.png)

# spring-boot-student
## spring-boot-student-banner
- 修改Spring Boot启动时的默认图案（Banner）
- [基于Docker的部署](https://www.jianshu.com/p/a04eaa881396)
- [Spring Boot核心-基本配置](http://blog.csdn.net/xiaolyuh123/article/details/70698659)

## spring-boot-student-config
- Spring 安全配置@ConfigurationProperties的使用
- 自定义properties并使用@PropertySource注解引入配置文件，使用@Value获取属性值
- 通过@ImportResource注解引入XML配置

[Spring Boot 核心-外部配置](http://blog.csdn.net/xiaolyuh123/article/details/70740118)

## spring-boot-student-log
- Spring Boot日志配置
- Spring Boot 使用XML配置日志
- Spring Boot 切换日志框架
- logback配置异步日志
- Logback 快速定位用户在一次请求中的所有日志

[Spring Boot 日志配置](http://blog.csdn.net/xiaolyuh123/article/details/70740598)

[Logback 快速定位用户在一次请求中的所有日志](https://www.jianshu.com/p/3dca4aeb6edd)

## spring-boot-student-profile
- Spring Boot Profile配置

[Spring Boot Profile 配置](http://blog.csdn.net/xiaolyuh123/article/details/70746844)
## spring-boot-student-data-jpa
- Spring boot 和Spring data jpa配置
- DBCP2连接池配置
- Spring Boot连接池分析
- Spring MVC Test(测试Controller)

[Spring Boot+Spring Data Jpa+DBCP2数据源](http://blog.csdn.net/xiaolyuh123/article/details/73457769)

[dbcp2数据源配置详解](http://blog.csdn.net/xiaolyuh123/article/details/73331093)

[Spring Boot中Datasource配置分析](http://blog.csdn.net/xiaolyuh123/article/details/73330996)

[Spring MVC 测试](http://blog.csdn.net/xiaolyuh123/article/details/70314176)

## spring-boot-student-mybatis
- Spring boot集成Mybatis和PageHPagehelper分页
- Mybatis一级缓存分析
- Mybatis二级缓存分析
- Mybatis自带二级缓存的配置
- Mysql查询的执行过程


[Spring Boot+Mybatis+Pagehelper分页](http://blog.csdn.net/xiaolyuh123/article/details/73506189)

[MySQL查询的执行过程](http://blog.csdn.net/xiaolyuh123/article/details/73550077)

[《深入理解mybatis原理》 Mybatis数据源与连接池](http://blog.csdn.net/xiaolyuh123/article/details/73648929)

[MyBatis的一级缓存实现详解](http://blog.csdn.net/xiaolyuh123/article/details/73650236)

[MyBatis的二级缓存实现详解](http://blog.csdn.net/xiaolyuh123/article/details/73650274)

[MyBatis缓存机制的设计与实现](http://blog.csdn.net/xiaolyuh123/article/details/73650298)

## spring-boot-student-mybatis-ehcache
- Spring Boot集成Mybatis
- 使用ehcache作为Mybatis的二级缓存

[Spring Boot + Mybatis + Ehcache 二级缓存实例](http://blog.csdn.net/xiaolyuh123/article/details/73904352)

[Spring Boot + Mybatis + 二级缓存实例（Ehcache，Redis）](http://blog.csdn.net/xiaolyuh123/article/details/73771818)

## spring-boot-student-mybatis-redis
- Spring Boot集成Mybatis
- 使用Redis作为Mybatis的二级缓存
- 通过实现ApplicationContextAware接口获取Spring容器，并获取容器中的Bean

[Spring Boot + Mybatis + Redis二级缓存实例](http://blog.csdn.net/xiaolyuh123/article/details/73912617)

[Spring Boot + Mybatis + 二级缓存实例（Ehcache，Redis）](http://blog.csdn.net/xiaolyuh123/article/details/73771818)

## spring-boot-student-mybatis-druid 自己定制
- Druid简介
- Druid数据源配置
- Druid监控的配置
- Spring Boot与Druid集成

[Druid简介（Spring Boot + Mybatis + Druid数据源）](http://blog.csdn.net/xiaolyuh123/article/details/74099972)

[maven替换中央仓库- 阿里云](http://blog.csdn.net/xiaolyuh123/article/details/74091268)

## spring-boot-student-mybatis-druid-2
- Spring Boot 使用druid连接池连接数据库（官方start）

[Druid简介（Spring Boot + Mybatis + Druid数据源【官方start】）](http://blog.csdn.net/xiaolyuh123/article/details/75315282)

## spring-boot-student-encode
- AES，MD5，SHA1加密算法

[加密算法 AES MD5 SHA1](http://blog.csdn.net/xiaolyuh123/article/details/74344893)


## spring-boot-student-data-jpa-transaction
- 开启事务
- Spring 事务类型
- Spring 事务原理
- Spring 事务的实现方式
- Spring 事务的注意事项

[Spring Boot Spring事务管理](http://blog.csdn.net/xiaolyuh123/article/details/56278121)

[Spring Boot AOP代理](http://blog.csdn.net/xiaolyuh123/article/details/76999835)

## spring-boot-student-cache
- 缓存的简介
- Spring对缓存支持
- 开启声名式缓存支持
- 声名式缓存注解
- Spring Boot的支持

[Spring Boot 数据缓存 Cache](http://blog.csdn.net/xiaolyuh123/article/details/77056340)

[Spring Boot缓存实战 默认Cache（ConcurrentMapCacheManager）](http://blog.csdn.net/xiaolyuh123/article/details/77096708)

## spring-boot-student-cache-ehcache
- Spring Boot 使用 ehcache作为 Spring 缓存

[Spring Boot缓存实战 EhCache](http://blog.csdn.net/xiaolyuh123/article/details/77161084)

## spring-boot-student-cache-redis
- Spring Boot缓存实战 Redis
- Spring Boot 使用 redis作为 Spring 缓存（自动刷新缓存）
- 自动刷新缓存
- redis key、value的序列化
- java反射
- 线程池

[Spring Boot缓存实战 Redis](http://blog.csdn.net/xiaolyuh123/article/details/77184508)

[Spring Boot Cache + redis 设置有效时间和自动刷新缓存，时间支持在配置文件中配置](http://blog.csdn.net/xiaolyuh123/article/details/78620302)

[Redis 序列化方式StringRedisSerializer、FastJsonRedisSerializer](http://blog.csdn.net/xiaolyuh123/article/details/78682200)

[Spring Redis Cache @Cacheable 大并发下返回null](http://blog.csdn.net/xiaolyuh123/article/details/78613041)

## spring-boot-student-cache-caffeine
- Spring Boot 使用 caffeine作为 Spring 缓存
- caffeine 简介

[Caffeine 缓存](http://blog.csdn.net/xiaolyuh123/article/details/78794012)

[Spring Boot 与 Caffeine的集成](http://blog.csdn.net/xiaolyuh123/article/details/78794119)

## spring-boot-student-cache-redis-caffeine
- 基于redis + caffeine实现的多级缓存

[Redis 序列化方式StringRedisSerializer、FastJsonRedisSerializer和KryoRedisSerializer](http://blog.csdn.net/xiaolyuh123/article/details/78682200)

[Spring Boot缓存实战 Redis + Caffeine 实现多级缓存](https://www.jianshu.com/p/ef9042c068fd)

## spring-boot-student-data-mongo
- Spring Boot 使用Spring data mongo 操作mongodb数据库

## spring-boot-student-data-redis
- Spring Boot 使用Spring data redis 操作redis数据库

## spring-boot-student-data-redis-distributed-lock
- 基于redis实现的分布式锁
- 分布式锁的实现方案

[基于 redis 实现的分布式锁（一）](http://blog.csdn.net/xiaolyuh123/article/details/78536708)

[基于 redis 实现的分布式锁（二）](http://blog.csdn.net/xiaolyuh123/article/details/78551345)

## spring-boot-student-drools
- [drools规则引擎入门](https://www.jianshu.com/p/e713860b128e)

## spring-boot-student-stock-redis
- [基于redis实现的扣减库存](https://www.jianshu.com/p/76bc0e963172)

## spring-boot-student-rabbitmq
- [Docker 安装部署RabbitMQ](https://www.jianshu.com/p/14ffe0f3db94)
- [RabbitMQ基础概念详细介绍](https://www.jianshu.com/p/e55e971aebd8)
- [RabbitMQ 快速入门](http://www.jianshu.com/p/b3d9e189323c)
- [Spring Boot RabbitMQ实践](https://www.jianshu.com/p/86b8171e4be4)

## spring-boot-student-monitor 
- [使用Spring Boot Actuator监控应用](http://blog.csdn.net/xiaolyuh123/article/details/79312291)

## spring-boot-student-validated
- [使用Spring Boot 参数校验]()

## spring-boot-student-guava-retrying
- [guava-retrying 重试机制](https://blog.csdn.net/xiaolyuh123/article/details/80209858)

## spring-boot-student-spring-retry
- [spring-retry 重试机制](https://blog.csdn.net/xiaolyuh123/article/details/80209831)
- [spring-retry重试与熔断详解](https://blog.csdn.net/xiaolyuh123/article/details/80209815)

## spring-boot-student-completable-future
- [CompletableFuture get方法一直阻塞或抛出TimeoutException](https://www.jianshu.com/p/548a6f1763b4)





















