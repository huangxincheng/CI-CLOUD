package com.husen.ci;

import com.husen.ci.es.annotation.ElasticSearchDomId;
import com.husen.ci.es.annotation.ElasticSearchIndex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/7/19 15:53
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@ElasticSearchIndex("es_bean")
public class EsBean implements Serializable {

    @ElasticSearchDomId
    private String id;

    private String beanName;

    private String beanTitle;

    private Integer beanSize;
}
