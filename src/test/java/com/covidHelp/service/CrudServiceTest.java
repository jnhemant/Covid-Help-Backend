package com.covidHelp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.covidHelp.demo.dao.CrudDao;
import com.covidHelp.demo.model.GenericElasticSearchRequest;
import com.covidHelp.demo.model.MaterialType;
import com.covidHelp.demo.response.MaterialListResponse;
import com.covidHelp.demo.service.CrudService;
import com.covidHelp.demo.service.elasticSearch.EsResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class CrudServiceTest {
    @InjectMocks
    private CrudService crudService;

    @Mock
    public CrudDao crudDao;

    @Captor
    ArgumentCaptor<GenericElasticSearchRequest> argCaptor;

    @Test
    public void getMaterialList_test() throws IOException{
        String district = "parsa";
        MaterialType materialType = MaterialType.plasma;
        String category = "o positive";
        Mockito.when(crudDao.getStock(any(), any())).thenReturn(new EsResponse());        
        MaterialListResponse listResponse = crudService.getMaterialList(district, materialType, category);
        assertTrue(listResponse.getData().isEmpty());
        assertEquals(listResponse.getCategory(), category);
        assertEquals(listResponse.getDistrict(), district);
        assertEquals(listResponse.getMaterialType(), materialType);
        Mockito.verify(crudDao).getStock(argCaptor.capture(), any());
        GenericElasticSearchRequest elasticSearchRequest = argCaptor.getValue();
        Map exactMatches = elasticSearchRequest.getExactMatches();
        assertEquals(exactMatches.get("district"), district);
        assertEquals(exactMatches.get("category"), category);
    }
}
