package com.xiaolyuh.utils;

import com.google.common.net.HttpHeaders;
import org.apache.commons.collections4.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导出CSV
 *
 * @author olafwang
 */
public class CsvExportUtil {
    private static final String CSV_COLUMN_SEPARATOR = ",";
    private static final String CSV_ROW_SEPARATOR = "\r\n";
    private static final String CSV_TAB = "\t";

    public static void doExportByList(List<List<Object>> dataList, List<String> titles, OutputStream os) throws IOException {
        StringBuilder buf = new StringBuilder();
        if (CollectionUtils.isNotEmpty(titles)) {
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i);
                buf.append(title);
                if (i < titles.size() - 1) {
                    buf.append(CSV_TAB).append(CSV_COLUMN_SEPARATOR);
                }
            }
            buf.append(CSV_ROW_SEPARATOR);
        }

        if (CollectionUtils.isNotEmpty(dataList)) {
            for (List<Object> data : dataList) {
                for (int i = 0; i < data.size(); i++) {
                    buf.append(data.get(i));
                    if (i < titles.size() - 1) {
                        buf.append(CSV_TAB).append(CSV_COLUMN_SEPARATOR);
                    }
                }
                buf.append(CSV_ROW_SEPARATOR);
            }
        }

        os.write(buf.toString().getBytes("UTF-8"));
        os.flush();
    }

    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName == null ? sdf.format(new Date()) + ".csv" : fileName + sdf.format(new Date()) + ".csv";
        String utf = "UTF-8";
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
    }

}
