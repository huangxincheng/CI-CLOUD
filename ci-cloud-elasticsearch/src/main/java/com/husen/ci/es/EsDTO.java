package com.husen.ci.es;

import com.husen.ci.es.annotation.ElasticSearchDomId;
import com.husen.ci.es.annotation.ElasticSearchIndex;
import lombok.Data;
import lombok.experimental.Accessors;

/***
 @Author:MrHuang
 @Date: 2019/7/18 17:33
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@ElasticSearchIndex("dto_es")
public class EsDTO {

    @ElasticSearchDomId
    private String id;

    private String name;
}
