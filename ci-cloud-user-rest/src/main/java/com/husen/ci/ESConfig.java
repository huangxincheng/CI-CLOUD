package com.husen.ci;

import com.husen.ci.es.ElasticSearchClient;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/7/19 15:51
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Configuration
public class ESConfig implements InitializingBean, DisposableBean {

    //    @Value("${es.address}")
    /**
     * es1.com:9200,es2.com:9200,es3.com:9200,
     */
    private String esAddress = "node1.es.sizne.net:9200,node2.es.sizne.net:9200,node3.es.sizne.net:9200,";

    @Override
    public void destroy() throws Exception {
        ElasticSearchClient.destroy();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] addressArray = esAddress.split(",");
        List<HttpHost> hostList = new ArrayList<>();
        for (String address : addressArray) {
            String host = address.split(":")[0];
            String port = address.split(":")[1];
            hostList.add(new HttpHost(host, Integer.parseInt(port)));
        }
        ElasticSearchClient.init(hostList.toArray(new HttpHost[]{}));
    }
}
