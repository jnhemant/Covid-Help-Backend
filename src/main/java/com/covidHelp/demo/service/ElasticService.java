package com.covidHelp.demo.service;

import java.io.IOException;

import com.covidHelp.demo.service.elasticSearch.EsRequest;
import com.covidHelp.demo.service.elasticSearch.EsResponse;  
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    public EsResponse searchEs(EsRequest esRequest) throws IOException{
        SearchResponse searchResponse;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest;

        if(esRequest.getQueryBuilder() != null){
            sourceBuilder = sourceBuilder.query(esRequest.getQueryBuilder()).trackTotalHits(true);
        }

        if(esRequest.getSortMap() != null){
            for(String key : esRequest.getSortMap().keySet()){
                String sortOrder = esRequest.getSortMap().get(key);
                sourceBuilder = sourceBuilder.sort(key, SortOrder.fromString(sortOrder));
            }
        }

        if(esRequest.getOffset() != null){
            sourceBuilder = sourceBuilder.from(esRequest.getOffset().intValue());
        }

        if(esRequest.getNoOfRecords() != null){
            sourceBuilder = sourceBuilder.size(esRequest.getNoOfRecords().intValue());
        }

        if(esRequest.getIncludeFields() != null && esRequest.getIncludeFields().length > 0){
            sourceBuilder = sourceBuilder.fetchSource(esRequest.getIncludeFields(), null);
        }
        else if(!esRequest.isFetchSource()){
            sourceBuilder = sourceBuilder.fetchSource(false);
        }

        if(esRequest.getIndex() != null){
            searchRequest = new SearchRequest(esRequest.getIndex());
        }
        else{
            searchRequest = new SearchRequest(esRequest.getIndices());
        }

        searchRequest.source(sourceBuilder).indicesOptions(IndicesOptions.lenientExpandOpen());

        searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        EsResponse esResponse = new EsResponse();
        if(searchResponse.getHits() != null){
            esResponse.setHits(searchResponse.getHits().getHits());
            esResponse.setTotalHits(searchResponse.getHits().getTotalHits().value);
        }
        
        return esResponse;
    }

    public EsResponse insertIntoEs(EsRequest esRequest) throws IOException{

        byte[] docByteArray = new ObjectMapper().writeValueAsBytes(esRequest.getDocObject());
        IndexRequest indexRequest = new IndexRequest();
        if (esRequest.getDocId() != null){
            indexRequest.index(esRequest.getIndex()).id(esRequest.getDocId()).source(docByteArray, XContentType.JSON);
        }
        else{
            indexRequest.index(esRequest.getIndex()).source(docByteArray, XContentType.JSON);
        }
        System.out.println(indexRequest);

        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        EsResponse esResponse = new EsResponse();
        esResponse.setMessage(indexResponse.getResult().name());
        esResponse.setDocId(indexResponse.getId());
        return esResponse;
    }

    public boolean isRecordsExists(String docId, String indexName) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, docId);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    }

    public GetResponse getByDocId(String docId, String indexName, boolean fetchSource) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, docId);
        getRequest.fetchSourceContext(new FetchSourceContext(fetchSource));
        return restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    public GetResponse getByDocId(String docId, String indexName) throws IOException {
        return getByDocId(docId, indexName, true);
    }

    public String updateESRecord(EsRequest esRequest) throws IOException{
        byte[] docByteArray = new ObjectMapper().writeValueAsBytes(esRequest.getDocObject());

        UpdateRequest updateRequest = new UpdateRequest().index(esRequest.getIndex())
            .id(esRequest.getDocId()).doc(docByteArray, XContentType.JSON);
        if(esRequest.isRefreshPolicy()){
            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        }

        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        return updateResponse.getResult().name();
    }

    public String deleteEsRecord(EsRequest esRequest) throws IOException{

        DeleteRequest deleteRequest = new DeleteRequest().setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        if(esRequest.getDocId() != null){
            deleteRequest = deleteRequest.id(esRequest.getDocId());
        }
        if(esRequest.getIndex() != null){
            deleteRequest = deleteRequest.index(esRequest.getIndex());
        }

        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return deleteResponse.getResult().name();
    }
}
