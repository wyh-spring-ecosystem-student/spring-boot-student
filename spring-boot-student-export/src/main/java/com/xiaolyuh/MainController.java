package com.xiaolyuh;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.utils.ExcelUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuhao.wang
 */
@RestController
public class MainController {

    @GetMapping("/")
    public String helloWorld(HttpServletResponse response) {

        List<Person> list = new ArrayList<>();
        list.add(new Person(1L, "姓名1", 28, "地址1"));
        list.add(new Person(2L, "姓名2", 29, "地址2"));

        String[] headers = new String[]{"ID", "名称", "年龄", "地址"};
        List<String[]> contents = new ArrayList<>(list.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Person person : list) {
            int i = 0;
            String[] row = new String[headers.length];
            row[i++] = person.getId() + "";
            row[i++] = person.getName();
            row[i++] = person.getAge() + "";
            row[i++] = person.getAddress();
            contents.add(row);
        }

        ExcelUtils.exportExcel("PersonInfo", headers, contents, response);

        return "Hello World";
    }
}