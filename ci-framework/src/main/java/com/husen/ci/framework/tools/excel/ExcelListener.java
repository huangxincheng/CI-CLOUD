package com.husen.ci.framework.tools.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author husen
 */
public class ExcelListener extends AnalysisEventListener {

    private List<Object> data = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        data.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("ExcelListener doAfterAllAnalysed ....");
    }

    public List<Object> getData() {
        return data;
    }
}