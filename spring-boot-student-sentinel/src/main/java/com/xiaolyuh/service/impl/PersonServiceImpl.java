package com.xiaolyuh.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.xiaolyuh.entity.Person;
import com.xiaolyuh.entity.Result;
import com.xiaolyuh.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PersonServiceImpl implements PersonService {
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    private static ThreadLocalRandom random = ThreadLocalRandom.current();

    static {
        random.ints();
    }

    @Override
    @SentinelResource(entryType = EntryType.IN)
    public Result semaphore(String arg) {
        Person person = new Person();
        person.setAge(18);
        person.setId(2L);
        person.setName("名称semaphore");
        person.setAddress("地址semaphore");
        logger.info(JSON.toJSONString(person));
        return Result.success(person);
    }

    @Override
    public Result thread(String arg) {
        String resourceName = "testSentinel";
        int time = random.nextInt(700);
        Entry entry = null;
        String retVal;
        try {
            entry = SphU.entry(resourceName, EntryType.IN);
            Thread.sleep(time);
            if (time > 690) {
                throw new RuntimeException("耗时太长啦");
            }

            retVal = "passed";
        } catch (BlockException e) {
//            logger.error("请求拒绝 {}", e.getMessage(), e);
            retVal = "blocked";
        } catch (Exception e) {
//            logger.error("异常 {}", e.getMessage(), e);
            // 异常数统计埋点
            Tracer.trace(e);
            throw new RuntimeException(e);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return Result.success(retVal + "::" + time);
    }

}
