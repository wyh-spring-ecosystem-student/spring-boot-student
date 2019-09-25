package com.xiaolyuh.transaction.dao;

import com.alibaba.fastjson.JSON;
import com.xiaolyuh.transaction.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author yuhao.wang3
 */
@Repository
public class PersonDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 操作数据的方法
     */
    public void insert(Person person) {
        String sql = "INSERT INTO `person` (`id`,`name`, `age`, `address`) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, person.getId(), person.getName(), person.getAge(), person.getAddress());
    }

    /**
     * 操作数据的方法
     */
    public List<Person> findAll() {
        String sql = "select `id`,`name`, `age`, `address` from `person` order by id desc";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return JSON.parseArray(JSON.toJSONString(list), Person.class);
    }
}