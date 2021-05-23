package com.covidHelp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.covidHelp.demo.config.LocalUserDetails;
import com.covidHelp.demo.dao.CrudDao;
import com.covidHelp.demo.model.GenericElasticSearchRequest;
import com.covidHelp.demo.model.Material;
import com.covidHelp.demo.model.MaterialType;
import com.covidHelp.demo.model.User;
import com.covidHelp.demo.request.MaterialRequest;
import com.covidHelp.demo.request.MaterialUpdateRequest;
import com.covidHelp.demo.response.MaterialListResponse;
import com.covidHelp.demo.service.CrudService;
import com.covidHelp.demo.service.elasticSearch.EsResponse;
import com.covidHelp.exception.ConflictException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javassist.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CrudServiceTest {
    @InjectMocks
    private CrudService crudService;

    @Mock
    private CrudDao crudDao;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContextHolder contextHolder;

    @Mock
    private SecurityContext context;

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

    /**
     * Case 1: record is not present
     * @throws Exception - ConflictException
     */
    @Test
    public void addStock_case_1() throws Exception{
        User user = new User();
        user.setId("id1");
        user.setUserName("user1");
        user.setPassword("password");
        user.setActive(true);
        user.setRoles("ROLES_USER"); 
        LocalUserDetails details = new LocalUserDetails(user);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        MaterialRequest materialRequest = new MaterialRequest();
        String address = "Address 1";
        String organization = "ABC Org";
        double quantity = 21D;
        materialRequest.setAddress(address);
        materialRequest.setOrganization(organization);
        materialRequest.setQuantity(quantity);
        List<Long> contactDetails = new ArrayList<>();
        contactDetails.add(9988774455L);
        contactDetails.add(77884459977L);
        materialRequest.setContactDetails(contactDetails);
        String district = "parsa";
        String createdBy = "id1";
        MaterialType materialType = MaterialType.plasma;
        String category = "o positive";
        materialRequest.setCreatedBy(createdBy);
        materialRequest.setDistrict(district);
        materialRequest.setMaterialType(materialType);
        materialRequest.setCategory(category);
        when(crudDao.getStock(any(), any())).thenReturn(new EsResponse());
        when(crudDao.addStock(any(), any())).thenReturn(true);
        Material material = crudService.addStock(materialRequest);
        assertNotNull(material);
        assertEquals(material.getAddress(), address);
        assertEquals(material.getCategory(), category);
        assertEquals(material.getContactDetails(), contactDetails);
        assertEquals(material.getCreatedBy(), createdBy);
        assertEquals(material.getDistrict(), district);
        assertEquals(material.getQuantity(), quantity);
        assertEquals(material.getOrganization(), organization);
    }

    /**
     * Case 2: record is already present
     * @throws Exception - ConflictException
     */
    @Test(expected = ConflictException.class)
    public void addStock_case_2() throws Exception{
        User user = new User();
        user.setId("id1");
        user.setUserName("user1");
        user.setPassword("password");
        user.setActive(true);
        user.setRoles("ROLES_USER"); 
        LocalUserDetails details = new LocalUserDetails(user);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        MaterialRequest materialRequest = new MaterialRequest();
        String address = "Address 1";
        String organization = "ABC Org";
        double quantity = 21D;
        materialRequest.setAddress(address);
        materialRequest.setOrganization(organization);
        materialRequest.setQuantity(quantity);
        List<Long> contactDetails = new ArrayList<>();
        contactDetails.add(9988774455L);
        contactDetails.add(77884459977L);
        materialRequest.setContactDetails(contactDetails);
        String district = "parsa";
        String createdBy = "id1";
        MaterialType materialType = MaterialType.plasma;
        String category = "o positive";
        materialRequest.setCreatedBy(createdBy);
        materialRequest.setDistrict(district);
        materialRequest.setMaterialType(materialType);
        materialRequest.setCategory(category);
        EsResponse esResponse = new EsResponse();
        esResponse.setTotalHits(2L);
        when(crudDao.getStock(any(), any())).thenReturn(esResponse);
        crudService.addStock(materialRequest);
    }

    /**
     * Case 3: record is not added
     * @throws Exception - IOException
     */
    @Test
    public void addStock_case_3() throws Exception{
        User user = new User();
        user.setId("id1");
        user.setUserName("user1");
        user.setPassword("password");
        user.setActive(true);
        user.setRoles("ROLES_USER"); 
        LocalUserDetails details = new LocalUserDetails(user);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        MaterialRequest materialRequest = new MaterialRequest();
        String address = "Address 1";
        String organization = "ABC Org";
        double quantity = 21D;
        materialRequest.setAddress(address);
        materialRequest.setOrganization(organization);
        materialRequest.setQuantity(quantity);
        List<Long> contactDetails = new ArrayList<>();
        contactDetails.add(9988774455L);
        contactDetails.add(77884459977L);
        materialRequest.setContactDetails(contactDetails);
        String district = "parsa";
        String createdBy = "id1";
        MaterialType materialType = MaterialType.plasma;
        String category = "o positive";
        materialRequest.setCreatedBy(createdBy);
        materialRequest.setDistrict(district);
        materialRequest.setMaterialType(materialType);
        materialRequest.setCategory(category);
        when(crudDao.getStock(any(), any())).thenReturn(new EsResponse());
        when(crudDao.addStock(any(), any())).thenReturn(false);
        Material material = crudService.addStock(materialRequest);
        assertNull(material);
    }

    /**
     * Case 1 - record not present in db
     * @throws Exception - IOException, NotFoundException
     */
    @Test(expected = NotFoundException.class)
    public void updateStock_Case_1() throws Exception{
        double quantity = 12d;
        MaterialType materialType = MaterialType.oxygenCylinder;
        List<Long> contactDetails = new ArrayList<>();
        contactDetails.add(9988774455L);
        contactDetails.add(77884459977L);
        MaterialUpdateRequest materialUpdateRequest = new MaterialUpdateRequest();
        materialUpdateRequest.setQuantity(quantity);
        materialUpdateRequest.setContactDetails(contactDetails);
        User user = new User();
        user.setId("id1");
        user.setUserName("user1");
        user.setPassword("password");
        user.setActive(true);
        user.setRoles("ROLES_USER"); 
        LocalUserDetails details = new LocalUserDetails(user);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(details);
        when(crudDao.getStock(any(), any())).thenReturn(new EsResponse());
        crudService.updateStock(materialUpdateRequest, materialType);
    }
}
