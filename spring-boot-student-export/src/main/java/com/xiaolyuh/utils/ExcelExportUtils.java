package com.xiaolyuh.utils;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件导出Excel 工具类
 *
 * @author olafwang
 */
public class ExcelExportUtils {
    private static Logger log = LoggerFactory.getLogger(ExcelExportUtils.class);

    /**
     * 导出Excel
     *
     * @param fileName 文件名
     * @param headers  表头(表格的第一行)
     * @param contents 内容
     * @param response HttpServletResponse
     */
    public final static void exportExcel(String fileName, String[] headers, List<String[]> contents, HttpServletResponse response) {
        // 定义输出类型
        response.setContentType("application/msexcel;charset=utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(response.getOutputStream());
            WritableSheet sheet = createSheet(workbook, "Sheet", 0);

            // 写表头
            if (headers != null && headers.length > 0) {
                WritableCellFormat headerFormat = getHeaderFormat();
                for (int i = 0; i < headers.length; i++) {
                    sheet.addCell(new Label(i, 0, headers[i], headerFormat));
                }
            }

            // 写数据
            if (contents != null && contents.size() > 0) {
                WritableCellFormat contentFormat = getContentFormat();
                for (int i = 0; i < contents.size(); i++) {
                    String[] content = contents.get(i);
                    if (content != null && content.length > 0) {
                        for (int j = 0; j < content.length; j++) {
                            sheet.addCell(new Label(j, (i + 1), content[j], contentFormat));
                        }
                    }
                }
            }
            workbook.write();
        } catch (Exception e) {
            log.error("导出Excel文件失败", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("关闭文件失败", e);
                }
            }
        }
    }

    /**
     * 创建数据页，并设置表格属性
     *
     * @param workbook
     * @return
     */
    private static WritableSheet createSheet(WritableWorkbook workbook, String name, int index) {
        WritableSheet sheet = workbook.createSheet(name, index);
        // 设置表格属性
        jxl.SheetSettings sheetset = sheet.getSettings();
        sheetset.setProtected(false);
        return sheet;
    }

    /**
     * 表头样式
     *
     * @return
     * @throws WriteException
     */
    private static WritableCellFormat getHeaderFormat() throws WriteException {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat format = new WritableCellFormat(font);
        // 线条
        format.setBorder(Border.ALL, BorderLineStyle.THIN);
        // 文字垂直对齐
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        // 文字水平对齐
        format.setAlignment(Alignment.CENTRE);
        // 文字是否换行
        format.setWrap(false);
        return format;
    }

    /**
     * 数据样式
     *
     * @return
     * @throws WriteException
     */
    private static WritableCellFormat getContentFormat() throws WriteException {
        WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
        WritableCellFormat format = new WritableCellFormat(font);
        // 线条
        format.setBorder(Border.NONE, BorderLineStyle.THIN);
        // 文字垂直对齐
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        // 文字水平对齐
        format.setAlignment(Alignment.LEFT);
        // 文字是否换行
        format.setWrap(false);
        return format;
    }

}