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
        InputStream inputStream = Resources.getResourceAsStream(resource);
        // 1.读取mybatis配置文件创SqlSessionFactory
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        inputStream.close();
    }

    @Test
    // 测试自动映射以及下划线自动转化驼峰
    public void quickStart() throws Exception {
        // 2.获取sqlSession
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            initH2dbMybatis(sqlSession);

            // 3.获取对应mapper
            PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
            // 4.执行查询语句并返回结果
            Person person = mapper.selectByPrimaryKey(1L);
            System.out.println(person.toString());
        }
    }
}
