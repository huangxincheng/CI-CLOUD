package com.husen.ci.framework.tools.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/***
 @Author:MrHuang
 @Date: 2019/7/12 15:47
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class EasyExcelUtils {

    private static InputStream getResourceAsStream(String fileName) throws FileNotFoundException {
        // 读取resource目录
//        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        return new BufferedInputStream(new FileInputStream(new File(fileName)));
    }

    /**
     * 读取少于10000条的07版的excel
     * @return
     */
    public static List<Object> read(String fileName, int sheetNo, int startRow) {
        InputStream is = null;
        try {
            is = getResourceAsStream(fileName);
            return EasyExcelFactory.read(is, new Sheet(sheetNo, startRow));
        } catch (Exception ex) {
            log.error("read fail", ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    /**
     * 读取少于10000条的07版的excel
     * @return
     */
    public static List<Object> read(String fileName, int sheetNo, int startRow, Class clazz) {
        InputStream is = null;
        try {
            is = getResourceAsStream(fileName);
            return EasyExcelFactory.read(is, new Sheet(sheetNo, startRow, clazz));
        } catch (Exception ex) {
            log.error("read fail", ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }


    /**
     * 读取多于10000条的07版的excel
     * @return
     */
    public static List<Object> readSax(String fileName, int sheetNo, int startRow) {
        InputStream is = null;
        try {
            is = getResourceAsStream(fileName);
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(is, new Sheet(sheetNo, startRow), excelListener);
            return excelListener.getData();
        } catch (Exception ex) {
            log.error("readSax fail", ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    /**
     * 读取多于10000条的07版的excel
     * @return
     */
    public static List<Object> readSax(String fileName, int sheetNo, int startRow, Class clazz) {
        InputStream is = null;
        try {
            is = getResourceAsStream(fileName);
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(is, new Sheet(sheetNo, startRow, clazz), excelListener);
            return excelListener.getData();
        } catch (Exception ex) {
            log.error("readSax fail", ex);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }


    public static void write() throws IOException {
        OutputStream out = new FileOutputStream("E:/MyProject2/CI-CLOUD/ci-framework/src/main/resources/2007-1.xlsx");
        ExcelWriter writer = EasyExcelFactory.getWriter(out);
        //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
        Sheet sheet1 = new Sheet(1, 3);
        sheet1.setSheetName("第一个sheet");

        //设置列宽 设置每列的宽度
        Map columnWidth = new HashMap();
        columnWidth.put(0,10000);columnWidth.put(1,40000);columnWidth.put(2,10000);columnWidth.put(3,10000);
        sheet1.setColumnWidthMap(columnWidth);
        sheet1.setHead(createTestListStringHead());
        //or 设置自适应宽度
        //sheet1.setAutoWidth(Boolean.TRUE);
        writer.write1(createTestListObject(), sheet1);

        //写第二个sheet sheet2  模型上打有表头的注解，合并单元格
        Sheet sheet2 = new Sheet(2, 3, WriteModel.class, "第二个sheet", null);
        sheet2.setTableStyle(createTableStyle());
        //writer.write1(null, sheet2);
        writer.write(createTestListJavaMode(), sheet2);
        //需要合并单元格
        writer.merge(5,20,1,1);

        //写第三个sheet包含多个table情况
        Sheet sheet3 = new Sheet(3, 0);
        sheet3.setSheetName("第三个sheet");
        Table table1 = new Table(1);
        table1.setHead(createTestListStringHead());
        writer.write1(createTestListObject(), sheet3, table1);

        //写sheet2  模型上打有表头的注解
        Table table2 = new Table(2);
        table2.setTableStyle(createTableStyle());
        table2.setClazz(WriteModel.class);
        writer.write(createTestListJavaMode(), sheet3, table2);

        writer.finish();
        out.close();


    }

    public static List<List<Object>> createTestListObject() {
        List<List<Object>> object = new ArrayList<List<Object>>();
        for (int i = 0; i < 1000; i++) {
            List<Object> da = new ArrayList<Object>();
            da.add("字符串"+i);
            da.add(Long.valueOf(187837834+i));
            da.add(Integer.valueOf(2233+i));
            da.add(Double.valueOf(2233.00+i));
            da.add(Float.valueOf(2233.0f+i));
            da.add(new Date());
            da.add(new BigDecimal("3434343433554545"+i));
            da.add(Short.valueOf((short)i));
            object.add(da);
        }
        return object;
    }

    public static List<List<String>> createTestListStringHead(){
        //写sheet3  模型上没有注解，表头数据动态传入
        List<List<String>> head = new ArrayList<List<String>>();
        List<String> headCoulumn1 = new ArrayList<String>();
        List<String> headCoulumn2 = new ArrayList<String>();
        List<String> headCoulumn3 = new ArrayList<String>();
        List<String> headCoulumn4 = new ArrayList<String>();
        List<String> headCoulumn5 = new ArrayList<String>();

        headCoulumn1.add("第一列");headCoulumn1.add("第一列");headCoulumn1.add("第一列");
        headCoulumn2.add("第一列");headCoulumn2.add("第一列");headCoulumn2.add("第一列");

        headCoulumn3.add("第二列");headCoulumn3.add("第二列");headCoulumn3.add("第二列");
        headCoulumn4.add("第三列");headCoulumn4.add("第三列2");headCoulumn4.add("第三列2");
        headCoulumn5.add("第一列");headCoulumn5.add("第3列");headCoulumn5.add("第4列");

        head.add(headCoulumn1);
        head.add(headCoulumn2);
        head.add(headCoulumn3);
        head.add(headCoulumn4);
        head.add(headCoulumn5);
        return head;
    }

    public static List<WriteModel> createTestListJavaMode(){
        List<WriteModel> model1s = new ArrayList<WriteModel>();
        for (int i = 0; i <10000 ; i++) {
            WriteModel model1 = new WriteModel();
            model1.setP1("第一列，第行");
            model1.setP2("121212jjj");
            model1.setP3(33+i);
            model1.setP4(44);
            model1.setP5("555");
            model1.setP6(666.2f);
            model1.setP7(new BigDecimal("454545656343434"+i));
            model1.setP8(new Date());
            model1.setP9("llll9999>&&&&&6666^^^^");
            model1.setP10(1111.77+i);
            model1s.add(model1);
        }
        return model1s;
    }

    public static TableStyle createTableStyle() {
        TableStyle tableStyle = new TableStyle();
        Font headFont = new Font();
        headFont.setBold(true);
        headFont.setFontHeightInPoints((short)22);
        headFont.setFontName("楷体");
        tableStyle.setTableHeadFont(headFont);
        tableStyle.setTableHeadBackGroundColor(IndexedColors.BLUE);

        Font contentFont = new Font();
        contentFont.setBold(true);
        contentFont.setFontHeightInPoints((short)22);
        contentFont.setFontName("黑体");
        tableStyle.setTableContentFont(contentFont);
        tableStyle.setTableContentBackGroundColor(IndexedColors.GREEN);
        return tableStyle;
    }
    public static void main(String[] args) throws IOException {
//        List<Object> objects = readSax("E:/MyProject2/CI-CLOUD/ci-framework/src/main/resources/2003.xls", 1, 1);
//        for (Object object : objects) {
//            System.out.println(object);
//        }
//        System.out.println(objects.size());
        write();
    }

    public static class WriteModel extends BaseWriteModel {



        @ExcelProperty(value = {"表头3","表头3","表头3"},index = 2)
        private int p3;

        @ExcelProperty(value = {"表头1","表头4","表头4"},index = 3)
        private long p4;

        @ExcelProperty(value = {"表头5","表头51","表头52"},index = 4)
        private String p5;

        @ExcelProperty(value = {"表头6","表头61","表头611"},index = 5)
        private float p6;

        @ExcelProperty(value = {"表头6","表头61","表头612"},index = 6)
        private BigDecimal p7;

        @ExcelProperty(value = {"表头6","表头62","表头621"},index = 7)
        private Date p8;

        @ExcelProperty(value = {"表头6","表头62","表头622"},index = 8)
        private String p9;

        @ExcelProperty(value = {"表头6","表头62","表头622"},index = 9)
        private double p10;

        public String getP1() {
            return p1;
        }

        public void setP1(String p1) {
            this.p1 = p1;
        }

        public String getP2() {
            return p2;
        }

        public void setP2(String p2) {
            this.p2 = p2;
        }

        public int getP3() {
            return p3;
        }

        public void setP3(int p3) {
            this.p3 = p3;
        }

        public long getP4() {
            return p4;
        }

        public void setP4(long p4) {
            this.p4 = p4;
        }

        public String getP5() {
            return p5;
        }

        public void setP5(String p5) {
            this.p5 = p5;
        }

        public float getP6() {
            return p6;
        }

        public void setP6(float p6) {
            this.p6 = p6;
        }

        public BigDecimal getP7() {
            return p7;
        }

        public void setP7(BigDecimal p7) {
            this.p7 = p7;
        }

        public Date getP8() {
            return p8;
        }

        public void setP8(Date p8) {
            this.p8 = p8;
        }

        public String getP9() {
            return p9;
        }

        public void setP9(String p9) {
            this.p9 = p9;
        }

        public double getP10() {
            return p10;
        }

        public void setP10(double p10) {
            this.p10 = p10;
        }

        @Override
        public String toString() {
            return "JavaModel1{" +
                    "p1='" + p1 + '\'' +
                    ", p2='" + p2 + '\'' +
                    ", p3=" + p3 +
                    ", p4=" + p4 +
                    ", p5='" + p5 + '\'' +
                    ", p6=" + p6 +
                    ", p7=" + p7 +
                    ", p8=" + p8 +
                    ", p9='" + p9 + '\'' +
                    ", p10=" + p10 +
                    '}';
        }
    }

    public static class BaseWriteModel extends BaseRowModel {
        @ExcelProperty(value = {"表头1","表头1","表头31"},index = 0)
        protected String p1;

        @ExcelProperty(value = {"表头1","表头1","表头32"},index = 1)
        protected String p2;

        public String getP1() {
            return p1;
        }

        public void setP1(String p1) {
            this.p1 = p1;
        }

        public String getP2() {
            return p2;
        }

        public void setP2(String p2) {
            this.p2 = p2;
        }
    }


}
