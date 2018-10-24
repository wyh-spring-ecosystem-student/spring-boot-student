package com.xiaolyuh.service;

import com.xiaolyuh.entity.Result;

public interface PersonService {

    Result semaphore(String arg);

    Result thread(String arg);
}
