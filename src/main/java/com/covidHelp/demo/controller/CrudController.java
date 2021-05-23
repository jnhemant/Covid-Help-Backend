package com.covidHelp.demo.controller;

import java.io.IOException;

import javax.validation.Valid;

import com.covidHelp.demo.model.Material;
import com.covidHelp.demo.model.MaterialType;
import com.covidHelp.demo.request.MaterialRequest;
import com.covidHelp.demo.request.MaterialUpdateRequest;
import com.covidHelp.demo.response.GenericSuccessResponse;
import com.covidHelp.demo.response.MaterialListResponse;
import com.covidHelp.demo.service.CrudService;
import com.jsoniter.output.JsonStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudController {

    @Autowired
    CrudService crudService;

    /**
     * POST mapping for "/members" endpoint, adds records of given material type
     * @param materialRequest - Contains details of the record like address, contact details, quantity
     * @return - Created Record
     * @throws Exception - if record is not created
     */
    @PostMapping(value = "/members", consumes = MediaType.APPLICATION_JSON_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Material addStock(@Valid @RequestBody MaterialRequest materialRequest) throws Exception{
        System.out.println(JsonStream.serialize(materialRequest));
        return crudService.addStock(materialRequest);
    }

    /**
     * Get the record of specified material type for current user
     * @param materialType - Type of material for which record is required
     * @return - Record fetched from db
     * @throws IOException - if record is not fetched
     */
    @GetMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public Material getStock(@RequestParam MaterialType materialType) throws IOException{
        return crudService.getStock(materialType);
    }

    /**
     * Get list of records by filtering with district, materialType and category
     * @param district - filter with provided district
     * @param materialType - filter with provided materialType
     * @param category - Valid in case of plasma, filter on the basis of category
     * @return - list of fetched records
     * @throws IOException - if exception is occurred while fetching records
     */
    @GetMapping(value = "/records", produces = MediaType.APPLICATION_JSON_VALUE)
    public MaterialListResponse getMaterialList(@RequestParam MaterialType materialType,
     @RequestParam String district, @RequestParam String category) throws IOException{
        return crudService.getMaterialList(district, materialType, category);
    }

    /**
     * Update an existing records
     * @param materialUpdateRequest - POJO to replace the existing record
     * @param materialType -Type of material to be updated
     * @return - True if record is updated, otherwise false
     * @throws Exception - if no record as such exists
     */
    @PutMapping(value = "/members/{materialType}", consumes = MediaType.APPLICATION_JSON_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericSuccessResponse updateStock(@Valid @RequestBody 
        MaterialUpdateRequest materialUpdateRequest, @PathVariable MaterialType materialType) throws Exception{
        System.out.println(JsonStream.serialize(materialUpdateRequest));
        if(crudService.updateStock(materialUpdateRequest, materialType)){
            return new GenericSuccessResponse("Stock was updated successfully");
        }
        return new GenericSuccessResponse("Unable to update the stock");
    }

    /**
     * Delete record of given materialType associated with current user
     * @param materialType - Type of material to be deleted
     * @return - true if record is deleted, else false
     * @throws IOException - if no record as such is found
     */
    @DeleteMapping(value = "/members", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericSuccessResponse deleteStock(@RequestParam MaterialType materialType)
     throws IOException{
        if(crudService.deleteStock(materialType)){
            return new GenericSuccessResponse("Stock deleted successfully");
        }
        return new GenericSuccessResponse("Unable to delete the stock");
    }
}
