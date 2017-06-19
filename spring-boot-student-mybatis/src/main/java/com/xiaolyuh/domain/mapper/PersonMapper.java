package com.xiaolyuh.domain.mapper;

import com.github.pagehelper.Page;
import com.xiaolyuh.domain.model.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);

    /**
     * 获取所有数据
     * @return
     */
    List<Person> findAll();

    /**
     * 分页查询数据
     * @return
     */
    Page<Person> findByPage();
}