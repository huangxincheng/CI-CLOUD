package com.husen.ci.es;

import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.framework.utils.BeanUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/***
 @Author:MrHuang
 @Date: 2019/7/19 11:02
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public abstract class ElasticSearchDao<T> {


    /**
     * 泛型T的Class类型
     */
    private Class<T> clazzT;


    private ElasticSearchClient esClient = ElasticSearchClient.getInstance();

    /**
     * 写入泛型Class类型
     */
    private void writeClassType() {
        clazzT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * ES 7.1 Index的语法
     * POST /{index}/_doc
     * {
     *   "name":"456"
     * }
     *
     * POST /${index}/_doc/${_id}
     *{
     *   "name":"456"
     * }
     * @param object
     * @return
     * @throws IOException
     */
    public IndexResponse index(T object) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        String domId = esClient.getDomId(object);
        Assert.notNull(index, "domId must not be null!");
        IndexRequest request = new IndexRequest(index);
        request.id(domId);
        request.source(JSONUtils.object2Json(object), XContentType.JSON);
        IndexResponse rsp = esClient.restClient.index(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * ES 7.1 Update的语法
     *
     * POST /${index}/_update/${_id}
     * {
     *   "doc":{
     *      "name":"sss"
     *   }
     * }
     * @param object
     * @return
     * @throws IOException
     */
    public UpdateResponse update(T object) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        String domId = esClient.getDomId(object);
        Assert.notNull(domId, "domId must not be null!");
        UpdateRequest request = new UpdateRequest(index, domId);
        request.doc(BeanUtils.bean2BeanMap(object));
        UpdateResponse rsp = esClient.restClient.update(request, RequestOptions.DEFAULT);
        return rsp;
    }


    /**
     * ES 7.1 Update的语法
     *
     * DELETE /${index}/_doc/${_id}
     * @param domId
     * @return
     * @throws IOException
     */
    public DeleteResponse delete(String domId) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domId, "domId must not be null!");
        DeleteRequest request = new DeleteRequest(index, domId);
        DeleteResponse rsp = esClient.restClient.delete(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * ES 7.1 Update的语法
     *
     * GET /${index}/_doc/${_id}
     * @param domId
     * @return
     */
    public GetResponse get(String domId) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domId, "domId must not be null!");
        GetRequest request = new GetRequest(index, domId);
        GetResponse rsp = esClient.restClient.get(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * Query
     * @param method
     * @param endpoint
     * @param entity
     * @return
     * @throws IOException
     */
    public Response query(String method, String endpoint, String entity) throws IOException {
        return esClient.query(method, endpoint, entity);
    }
}
