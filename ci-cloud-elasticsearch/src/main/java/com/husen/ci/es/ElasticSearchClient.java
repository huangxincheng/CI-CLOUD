package com.husen.ci.es;

import com.husen.ci.es.annotation.ElasticSearchDomId;
import com.husen.ci.es.annotation.ElasticSearchIndex;
import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/***
 @Author:MrHuang
 @Date: 2019/7/18 15:35
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class ElasticSearchClient {

    private static HttpHost[] httpHosts;

    private volatile static ElasticSearchClient esClient;

    protected RestHighLevelClient restClient;

    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String GET = "GET";


    private ElasticSearchClient() {
        restClient = new RestHighLevelClient(RestClient.builder(ElasticSearchClient.httpHosts));
    }

    /**
     * Init
     * @param httpHosts
     */
    public static void init(HttpHost ... httpHosts) {
        ElasticSearchClient.httpHosts = httpHosts;
        getInstance();
    }

    /**
     * destroy
     */
    public static void destroy() {
        try {
            ElasticSearchClient.getInstance().restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * getInstance
     * @return
     */
    public static ElasticSearchClient getInstance() {
        if (esClient == null) {
            synchronized (ElasticsearchClient.class) {
                if (esClient == null) {
                    esClient = new ElasticSearchClient();
                }
            }
        }
        if (esClient == null) {
            throw new RuntimeException("EsClient getInstance is null ....");
        }
        return esClient;
    }

    /**
     * doSendRequest
     * @param method
     * @param endpoint
     * @param entity
     * @return
     * @throws IOException
     */
    public Response doSendRequest(String method, String endpoint, String entity) throws IOException {
        Request request = new Request(method, endpoint);
        if (StringUtils.hasText(entity)){
            request.setJsonEntity(entity);
        }
        return getInstance().restClient.getLowLevelClient().performRequest(request);
    }


    protected String getIndex(Class clazz) {
        ElasticSearchIndex annotation = AnnotationUtils.findAnnotation(clazz, ElasticSearchIndex.class);
        Assert.notNull(annotation, "index must not be null!");
        return annotation.index();
    }

    protected String getDomId(Object object) {
        String domIdValue = null;
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            ElasticSearchDomId domId = field.getAnnotation(ElasticSearchDomId.class);
            if (domId != null) {
                try {
                    domIdValue = field.get(object).toString();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        return domIdValue;
    }


    /**
     * index
     * @param endpoint
     * @param entity
     * @return
     * @throws IOException
     */
    public Response index(String endpoint, String entity) throws IOException {
        return doSendRequest(POST, endpoint, entity);
    }

    /**
     * update
     * @param endpoint
     * @param entity
     * @return
     * @throws IOException
     */
    public Response update(String endpoint, String entity) throws IOException {
        return doSendRequest(POST, endpoint, entity);
    }

    /**
     * delete
     * @param endpoint
     * @return
     * @throws IOException
     */
    public Response delete(String endpoint) throws IOException {
        return doSendRequest(DELETE, endpoint, null);
    }

    /**
     * get
     * @param endpoint
     * @return
     * @throws IOException
     */
    public Response get(String endpoint) throws IOException {
        return doSendRequest(GET, endpoint, null);
    }


    /**
     * Query语法
     * @param method 请求方法类型 POST GET DELETE PUT ...
     * @param endpoint 断点 即 /${index}/_update/${_id} ...
     * @param entity 请求体 json
     * @return
     * @throws IOException
     */
    public Response query(String method, String endpoint, String entity) throws IOException {
        return doSendRequest(method, endpoint, entity);
    }


    public static void main(String[] args) throws IOException {
//        HttpHost[] hosts = new HttpHost[]{
//                new HttpHost("node1.es.sizne.net", 9200)
//        };
//        init(hosts);
//        destroy();
    }
}
