package com.xiaolyuh;

import com.xiaolyuh.entity.Person;
import com.xiaolyuh.utils.CsvExportUtil;
import com.xiaolyuh.utils.ExcelExportUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuhao.wang
 */
@RestController
public class ExprotController {

    @GetMapping("/excel")
    public void exportExcel(HttpServletResponse response) {

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

        ExcelExportUtils.exportExcel("PersonInfo", headers, contents, response);
    }

    @GetMapping("/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        String fileName = "PersonInfo";
        List<String> titles = Arrays.asList("ID", "名称", "年龄", "地址");

        List<Person> list = new ArrayList<>();
        list.add(new Person(1L, "姓名1", 28, "地址1"));
        list.add(new Person(2L, "姓名2", 29, "地址2"));

        List<List<Object>> dataList = list.stream().map(person -> {
            List<Object> data = new ArrayList<>();
            data.add(person.getId());
            data.add(person.getName());
            data.add(person.getAge());
            data.add(person.getAddress());
            return data;
        }).collect(Collectors.toList());

        OutputStream os = response.getOutputStream();
        CsvExportUtil.responseSetProperties(fileName, response);
        CsvExportUtil.doExportByList(dataList, titles, os);
    }
}