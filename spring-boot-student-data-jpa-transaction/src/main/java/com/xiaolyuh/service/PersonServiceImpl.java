package com.xiaolyuh.service;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonRepository personRepository;

    // 注入自身代理对象，在本类内部方法调用事务的传递性才会生效
    @Autowired
    PersonService selfProxyPersonService;

    /**
     * 测试事务的传递性
     *
     * @param person
     * @return
     */
    @Transactional
    public Person save(Person person) {
        Person p = personRepository.save(person);
        try {
            // 新开事务 独立回滚
            selfProxyPersonService.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 使用当前事务 全部回滚
            selfProxyPersonService.save2(person);
        } catch (Exception e) {
            e.printStackTrace();
        }
        personRepository.save(person);

        return p;
    }

    @Transactional
    public void save2(Person person) {
        personRepository.save(person);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete() {
        personRepository.delete(1L);
        throw new RuntimeException();
    }
}
