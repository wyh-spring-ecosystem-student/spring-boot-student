package com.xiaolyuh.repository;

import com.xiaolyuh.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}