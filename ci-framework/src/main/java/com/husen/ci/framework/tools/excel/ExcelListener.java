package com.husen.ci.framework.tools.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author husen
 */
@Slf4j
public class ExcelListener extends AnalysisEventListener {

    private List<Object> data = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        data.add(object);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("ExcelListener doAfterAllAnalysed ......");
    }

    public List<Object> getData() {
        return data;
    }
}