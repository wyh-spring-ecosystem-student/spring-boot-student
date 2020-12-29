package com.xiaolyuh.utils;

import com.google.common.net.HttpHeaders;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 导出CSV
 */
public class CsvExportUtil {
    private static final String CSV_COLUMN_SEPARATOR = ",";
    private static final String CSV_ROW_SEPARATOR = "\r\n";
    private static final String CSV_TAB = "\t";

    public static void doExportByList(List<List<Object>> dataList, List<String> titles, OutputStream os) throws IOException {
        StringBuffer buf = new StringBuffer();
        Iterator var4;
        if (CollectionUtils.isNotEmpty(titles)) {
            var4 = titles.iterator();

            while (var4.hasNext()) {
                String title = (String) var4.next();
                buf.append(title).append(",");
            }

            buf.append("\r\n");
        }

        if (CollectionUtils.isNotEmpty(dataList)) {
            var4 = dataList.iterator();

            while (var4.hasNext()) {
                List<Object> data = (List) var4.next();

                for (int i = 0; i < data.size(); ++i) {
                    buf.append(data.get(i)).append("\t").append(",");
                }

                buf.append("\r\n");
            }
        }

        os.write(buf.toString().getBytes("UTF-8"));
        os.flush();
    }

    public static void doExport(List<Map<String, Object>> dataList, List<String> titles, List<String> keys, OutputStream os) throws Exception {
        StringBuffer buf = new StringBuffer();
        Iterator var5 = titles.iterator();

        while (var5.hasNext()) {
            String title = (String) var5.next();
            buf.append(title).append(",");
        }

        buf.append("\r\n");
        if (CollectionUtils.isNotEmpty(dataList)) {
            var5 = dataList.iterator();

            while (var5.hasNext()) {
                Map<String, Object> data = (Map) var5.next();
                Iterator var7 = keys.iterator();

                while (var7.hasNext()) {
                    String key = (String) var7.next();
                    buf.append(data.get(key)).append(",");
                }

                buf.append("\r\n");
            }
        }

        os.write(buf.toString().getBytes("GBK"));
    }

    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName == null ? sdf.format(new Date()) + ".csv" : fileName + sdf.format(new Date()) + ".csv";
        String utf = "UTF-8";
        //response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    }

    public static void setResponseHeader(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        String utf = "UTF-8";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, utf));
    }

    public static InputStream genInputStream(List<List<Object>> dataList, List<String> titles) {
        StringBuilder sb = new StringBuilder(StringUtils.join(titles, ","));
        sb.append("\r\n");
        Iterator var3 = dataList.iterator();

        while (var3.hasNext()) {
            List<Object> objects = (List) var3.next();
            sb.append(StringUtils.join(objects, ",")).append("\r\n");
        }

        String csv = sb.toString();
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }
}
