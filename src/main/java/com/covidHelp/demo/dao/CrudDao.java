package com.covidHelp.demo.dao;

import java.io.IOException;
import java.util.Map;

import com.covidHelp.demo.model.GenericElasticSearchRequest;
import com.covidHelp.demo.model.Material;
import com.covidHelp.demo.request.MaterialUpdateRequest;
import com.covidHelp.demo.service.ElasticService;
import com.covidHelp.demo.service.elasticSearch.EsRequest;
import com.covidHelp.demo.service.elasticSearch.EsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CrudDao {

    @Value("${elasticsearch.oxygenCylinderIndex}")
    private String oxygenCylinderIndex;

    @Autowired
    private ElasticService elasticService;

    @Autowired
    private ElasticSearchQueryHelper esQueryHelper;

    public boolean addStock(Material material, String index) throws IOException{
        Map materialMap = new ObjectMapper().convertValue(material, Map.class);
        if(materialMap.get("category") == null){
            materialMap.remove("category");
        }
        if(materialMap.containsKey("materialType")){
            materialMap.remove("materialType");
        }
        EsResponse esResponse = elasticService.insertIntoEs(new EsRequest().index(index)
        .docObject(materialMap).docId(material.getId()));
        return esResponse.getMessage().equals(DocWriteResponse.Result.CREATED.toString());
    }

    public boolean updateStock(String id, MaterialUpdateRequest materialUpdateRequest, String index) throws IOException{
        Map materialUpdateMap = new ObjectMapper().convertValue(materialUpdateRequest, Map.class);
        String updateStatus = elasticService.updateESRecord(new EsRequest().index(index).docId(id)
        .docObject(materialUpdateMap));
        return updateStatus.equals(DocWriteResponse.Result.UPDATED.toString()) ||
        updateStatus.equals(DocWriteResponse.Result.NOOP.toString());
    }

    public EsResponse getStock(GenericElasticSearchRequest request, String index) throws IOException{
        BoolQueryBuilder boolQueryBuilder = esQueryHelper.buildBoolQuery(request);

        EsRequest esRequest = new EsRequest().queryBuilder(boolQueryBuilder).index(index);

        EsResponse esResponse = elasticService.searchEs(esRequest);
        return esResponse;
    }

    public boolean deleteStock(String id, String index) throws IOException{
        String result = elasticService.deleteEsRecord(new EsRequest().docId(id).index(index));
        return result.equals(DocWriteResponse.Result.DELETED.toString());
    }
}
