package com.xiaolyuh;

import com.xiaolyuh.domain.mapper.PersonMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextHolderTest {

    private Logger logger = LoggerFactory.getLogger(SpringContextHolderTest.class);

    @Test
    public void testGetApplicationContext() {
        ApplicationContext context = SpringContextHolder.getApplicationContext();
        PersonMapper personMapper = SpringContextHolder.getBean(PersonMapper.class);
        personMapper.findAll();
        personMapper.findAll();
        logger.info(personMapper.getClass().getName());
    }

    @Test
    public void testStackOverflowError() {
        String str = "hjafl;jaslfdj离开家啊是的拉风科技拉上的风景；拉上的纠纷；拉数据地方；啦手机发的；打上了飞机打算离开减肥；拉沙德大数据放假阿里四大会计法；老大时间；里发生了东风科技卡上飞机；拉萨减肥；离开觉得是；力法加点；放假啦是当机立断放假了打发时间来打发时间；老大说减肥；里的卡手机发了离开对方接受；拉上飞机；来的时间里附近；来打扫房间";
        A a = new A();
        a.loop();
    }


    class A {
        private B b = new B();

        public void loop() {
            b.loop();
        }
    }

    class B {
        private A a = new A();

        public void loop() {
            a.loop();
        }
    }
}
