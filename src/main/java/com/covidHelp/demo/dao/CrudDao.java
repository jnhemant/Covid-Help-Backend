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

    /**
     * Insert new record into DB
     * @param material - Object to be inserted into db
     * @param index - index into which the record is inserted
     * @return - Created material stock
     * @throws IOException - If record is not created
     */
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

    /**
     * Update an existing record of specified materialType
     * @param id - Id of the record to be updated
     * @param materialUpdateRequest - POJO to replace the existing record
     * @param index - index in which record is updated
     * @return - True if record is updated, otherwise false
     * @throws IOException - if record is not updated
     */
    public boolean updateStock(String id, MaterialUpdateRequest materialUpdateRequest, String index) throws IOException{
        Map materialUpdateMap = new ObjectMapper().convertValue(materialUpdateRequest, Map.class);
        String updateStatus = elasticService.updateESRecord(new EsRequest().index(index).docId(id)
        .docObject(materialUpdateMap));
        return updateStatus.equals(DocWriteResponse.Result.UPDATED.toString()) ||
        updateStatus.equals(DocWriteResponse.Result.NOOP.toString());
    }

    /**
     * Fetch records using filters in search request
     * @param request - contains the filters like district, category
     * @param index - index from which record is fetched
     * @return - response obtained from db
     * @throws IOException - Exception occurred while fetching records
     */
    public EsResponse getStock(GenericElasticSearchRequest request, String index) throws IOException{
        BoolQueryBuilder boolQueryBuilder = esQueryHelper.buildBoolQuery(request);

        EsRequest esRequest = new EsRequest().queryBuilder(boolQueryBuilder).index(index);

        EsResponse esResponse = elasticService.searchEs(esRequest);
        return esResponse;
    }

    /**
     * Delete the record
     * @param id - delete the record with given id
     * @param index - delete the record in given index
     * @return - true is record is deleted, else false
     * @throws IOException - any exception occurred while deleting record from ES
     */
    public boolean deleteStock(String id, String index) throws IOException{
        String result = elasticService.deleteEsRecord(new EsRequest().docId(id).index(index));
        return result.equals(DocWriteResponse.Result.DELETED.toString());
    }
}
