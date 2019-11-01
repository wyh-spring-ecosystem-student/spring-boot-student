package com.xiaolyuh.mybatis;

import com.xiaolyuh.BaseTest;
import com.xiaolyuh.domain.mapper.PersonMapper;
import com.xiaolyuh.domain.model.Person;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yuhao.wang3
 * @since 2019/10/14 17:44
 */
public class MybatisTest extends BaseTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws IOException {
        String resource = "config/mybatis-config.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            // 1.读取mybatis配置文件创SqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }

    @Test
    // 面向接口编程模型
    public void quickStart() throws Exception {
        // 2.获取sqlSession
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            initH2dbMybatis(sqlSession);

            // 3.获取对应mapper
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            JdkProxySourceClassUtil.writeClassToDisk(mapper.getClass().getSimpleName(), mapper.getClass());
            // 4.执行查询语句并返回结果
            Person person = mapper.selectByPrimaryKey(1L);
            System.out.println(person.toString());
        }
    }

    @Test
    // ibatis编程模型
    public void quickStartIBatis() throws Exception {
        // 2.获取sqlSession
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            initH2dbMybatis(sqlSession);
            // ibatis编程模型(与配置文件耦合严重)
            Person person = sqlSession.selectOne("com.xiaolyuh.domain.mapper.PersonMapper.selectByPrimaryKey", 1L);
            System.out.println(person.toString());
        }
    }
}
