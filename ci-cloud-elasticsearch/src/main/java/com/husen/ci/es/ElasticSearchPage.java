package com.husen.ci.es;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/7/22 11:01
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
public class ElasticSearchPage implements Serializable {

    /**
     * 当前索引
     */
    private int pageFrom;

    /**
     * 当前页数
     */
    private int pageNo;

    /**
     * 分页大小
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 总记录条数
     */
    private long totalRecord;

    /**
     * 记录对象
     */
    private List<?> records;
}
