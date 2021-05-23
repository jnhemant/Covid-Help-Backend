package com.covidHelp.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.covidHelp.demo.config.LocalUserDetails;
import com.covidHelp.demo.dao.CrudDao;
import com.covidHelp.demo.model.GenericElasticSearchRequest;
import com.covidHelp.demo.model.Material;
import com.covidHelp.demo.model.MaterialType;
import com.covidHelp.demo.request.MaterialRequest;
import com.covidHelp.demo.request.MaterialUpdateRequest;
import com.covidHelp.demo.response.MaterialListResponse;
import com.covidHelp.demo.service.elasticSearch.EsResponse;
import com.covidHelp.exception.ConflictException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.output.JsonStream;

import org.elasticsearch.search.SearchHit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service
public class CrudService {

    @Value("${elasticsearch.oxygenCylinderIndex}")
    private String oxygenCylinderIndex;

    @Value("${elasticsearch.generalBedsIndex}")
    private String generalBedsIndex;

    @Value("${elasticsearch.ICUBedsIndex}")
    private String ICUBedsIndex;

    @Value("${elasticsearch.covidBedsIndex}")
    private String covidBedsIndex;

    @Value("${elasticsearch.oxygenConcentratorIndex}")
    private String oxygenConcentratorIndex;

    @Value("${elasticsearch.ventilatorIndex}")
    private String ventilatorIndex;

    @Value("${elasticsearch.plasmaIndex}")
    private String plasmaIndex;

    @Value("${elasticsearch.remdesivirIndex}")
    private String remdesivirIndex;

    @Autowired
    CrudDao crudDao;

    /**
     * Insert new record into DB
     * @param materialRequest - POJO for request payload
     * @return - Created material stock
     * @throws Exception - if record is already present, or new record is not inserted
     */
    public Material addStock(MaterialRequest materialRequest) throws Exception {
        materialRequest.setDistrict(materialRequest.getDistrict().toLowerCase());
        if(materialRequest.getCategory() != null){
            materialRequest.setCategory(materialRequest.getCategory().toLowerCase());
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalUserDetails user = (LocalUserDetails) authentication.getPrincipal();
        String createdBy = user.getId();

        // check if a record for the material already exists
        GenericElasticSearchRequest searchRequest = new GenericElasticSearchRequest();
        Map<String, String> exactMatches = new HashMap<String, String>();
        exactMatches.put("createdBy.keyword", createdBy);
        searchRequest.setExactMatches(exactMatches);
        String index = getIndex(materialRequest.getMaterialType());
        EsResponse response = crudDao.getStock(searchRequest, index);
        if (response.getTotalHits() != 0) {
            throw new ConflictException("Records already exists!");
        }

        Material material = new Material();
        BeanUtils.copyProperties(materialRequest, material);
        material.setCreatedBy(createdBy);
        material.setId(UUID.randomUUID().toString());
        long currentTimeInMillis = System.currentTimeMillis();
        material.setCreatedOn(currentTimeInMillis);
        material.setLastUpdatedOn(currentTimeInMillis);
        if (crudDao.addStock(material, index)) {
            return material;
        }
        return null;
    }

    /**
     * Update an existing record
     * @param materialUpdateRequest - POJO to replace the existing record
     * @param materialType -Type of material to be updated
     * @return - True if record is updated, otherwise false
     * @throws Exception - if record is not updated
     */
    public boolean updateStock(MaterialUpdateRequest materialUpdateRequest, MaterialType materialType)
            throws Exception {
        Material material = getStock(materialType);
        if(material == null){
            throw new NotFoundException("No record found to update");
        }
        String index = getIndex(materialType);
        materialUpdateRequest.setLastUpdatedOn(System.currentTimeMillis());
        return crudDao.updateStock(material.getId(), materialUpdateRequest, index);
    }

    /**
     * Get record associated with current user
     * @param materialType - type of record to be fetched
     * @return - fetched record
     * @throws IOException - if exception is occurred while fetching records
     */
    public Material getStock(MaterialType materialType) throws IOException {
        GenericElasticSearchRequest searchRequest = new GenericElasticSearchRequest();
        Map<String, String> exactMatches = new HashMap<String, String>();

        // fetch createdBy from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalUserDetails user = (LocalUserDetails) authentication.getPrincipal();
        String createdBy = user.getId();
        exactMatches.put("createdBy.keyword", createdBy);
        searchRequest.setExactMatches(exactMatches);
        String index = getIndex(materialType);
        Material material = null;
        EsResponse response = crudDao.getStock(searchRequest, index);
        if (response.getTotalHits() > 0) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                material = mapper.convertValue(response.getHits()[0].getSourceAsMap(), Material.class);
            System.out.println(JsonStream.serialize(material));
        }
        return material;
    }

    /**
     * Get list of records by filtering with district, materialType and category
     * @param district - filter with provided district
     * @param materialType - filter with provided materialType
     * @param category - Valid in case of plasma, filter on the basis of category
     * @return - list of fetched records
     * @throws IOException - if exception is occurred while fetching records
     */
    public MaterialListResponse getMaterialList(String district, MaterialType materialType, String category)
            throws IOException {
        GenericElasticSearchRequest searchRequest = new GenericElasticSearchRequest();
        // searchRequest.setOffset(offset);
        Map<String, String> exactMatches = new HashMap<String, String>();
        exactMatches.put("district", district);
        if (materialType.equals(MaterialType.plasma) && category != null && !category.isEmpty()) {
            exactMatches.put("category", category);
        }
        searchRequest.setExactMatches(exactMatches);
        String index = getIndex(materialType);
        EsResponse response = crudDao.getStock(searchRequest, index);
        List<Material> resultList = new ArrayList<>();
        if (response.getTotalHits() > 0) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            for (SearchHit hit : response.getHits()) {
                Material material = mapper.convertValue(hit.getSourceAsMap(), Material.class);
                resultList.add(material);
            }
            System.out.println(JsonStream.serialize(resultList));
        }
        MaterialListResponse listResponse = new MaterialListResponse();
        listResponse.setDistrict(district);
        listResponse.setMaterialType(materialType);
        if(materialType.equals(MaterialType.plasma) && category != null && !category.isEmpty()) {
            listResponse.setCategory(category);
        }
        listResponse.setData(resultList);
        return listResponse;
    }

    /**
     * Delete record of given materialType associated with current user
     * @param materialType - Type of material to be deleted
     * @return - true if record is deleted, else false
     * @throws IOException - if no record as such is found
     */
    public boolean deleteStock(MaterialType materialType) throws IOException {
        Material material = getStock(materialType);
        String index = getIndex(materialType);
        return crudDao.deleteStock(material.getId(), index);
    }

    /**
     * Get the index w.r.t. materialType
     * @param materialType - Enum type variable
     * @return - variable pointing to concerned index
     */
    private String getIndex(MaterialType materialType) {
        String index;
        switch (materialType) {
            case oxygenCylinder: {
                index = oxygenCylinderIndex;
            }
                break;
            case oxygenConcentrator: {
                index = oxygenConcentratorIndex;
            }
                break;
            case ICUBeds: {
                index = ICUBedsIndex;
            }
                break;
            case covidBeds: {
                index = covidBedsIndex;
            }
                break;
            case generalBeds: {
                index = generalBedsIndex;
            }
                break;
            case plasma: {
                index = plasmaIndex;
            }
                break;
            case remdesivir: {
                index = remdesivirIndex;
            }
                break;
            case ventilator: {
                index = ventilatorIndex;
            }
                break;
            default: {
                index = oxygenCylinderIndex;
            }
        }
        return index;
    }
}
