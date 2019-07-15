package com.husen.ci.framework.tools.excel;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/7/12 15:47
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Slf4j
public class EasyExcelUtils {

    private static InputStream getResourceAsStream(String fileName) throws FileNotFoundException {
        // 读取resource目录  [return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);]
        return new BufferedInputStream(new FileInputStream(new File(fileName)));
    }

    /**
     * 读取少于10000条的excel
     * @return
     */
    public static List<Object> read(String fileName, int sheetNo, int startRow) {
        try (InputStream is = getResourceAsStream(fileName)) {
            return EasyExcelFactory.read(is, new Sheet(sheetNo, startRow));
        } catch (Exception ex) {
            log.error("read fail", ex);
        }
        return null;
    }

    /**
     * 读取少于10000条的excel
     * @return
     */
    public static List<Object> read(String fileName, int sheetNo, int startRow, Class clazz) {
        try (InputStream is = getResourceAsStream(fileName)) {
            return EasyExcelFactory.read(is, new Sheet(sheetNo, startRow, clazz));
        } catch (Exception ex) {
            log.error("read fail", ex);
        }
        return null;
    }


    /**
     * 读取多于10000条的excel
     * @return
     */
    public static List<Object> readSax(String fileName, int sheetNo, int startRow) {
        try (InputStream is = getResourceAsStream(fileName)) {
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(is, new Sheet(sheetNo, startRow), excelListener);
            return excelListener.getData();
        } catch (Exception ex) {
            log.error("readSax fail", ex);
        }
        return null;
    }

    /**
     * 读取多于10000条的excel
     * @return
     */
    public static List<Object> readSax(String fileName, int sheetNo, int startRow, Class clazz) {
        try (InputStream is = getResourceAsStream(fileName)) {
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(is, new Sheet(sheetNo, startRow, clazz), excelListener);
            return excelListener.getData();
        } catch (Exception ex) {
            log.error("readSax fail", ex);
        }
        return null;
    }


    /**
     * 需要写入的Excel，有模型映射关系
     * @param file  需要写入的Excel，格式为xlsx 2007版本
     * @param list 写入Excel中的所有数据，继承于BaseRowModel
     */
    public static void writeExcel2Xlsx(final File file, List<? extends BaseRowModel> list) {
        try (OutputStream out = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(out)){
            ExcelWriter writer = EasyExcelFactory.getWriter(bos);
            //写第一个sheet,  有模型映射关系
            Class clazz = list.get(0).getClass();
            Sheet sheet = new Sheet(1, 0, clazz);
            writer.write(list, sheet);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 需要写入的Excel，有模型映射关系
     * @param file  需要写入的Excel，格式为xls 2003版本
     * @param list 写入Excel中的所有数据，继承于BaseRowModel
     */
    public static void writeExcel2Xls(final File file, List<? extends BaseRowModel> list) {
        try (OutputStream out = new FileOutputStream(file); BufferedOutputStream bos = new BufferedOutputStream(out)){
            ExcelWriter writer = EasyExcelFactory.getWriter(bos, ExcelTypeEnum.XLS, true);
            //写第一个sheet,  有模型映射关系
            Class clazz = list.get(0).getClass();
            Sheet sheet = new Sheet(1, 0, clazz);
            writer.write(list, sheet);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        List<TestExcelRowModel> tms = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tms.add( new TestExcelRowModel().setName("123-" + i).setPassword("123**" + i).setNickName("123" + i).setBirthday(new Date()));
        }
        writeExcel2Xlsx(new File("E:/MyProject2/CI-CLOUD/ci-framework/src/main/resources/2007-bak.xlsx"), tms);
        writeExcel2Xls(new File("E:/MyProject2/CI-CLOUD/ci-framework/src/main/resources/2007-bak.xls"), tms);
        System.out.println("pk");
    }

}
