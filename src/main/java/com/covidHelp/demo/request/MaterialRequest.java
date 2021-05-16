package com.covidHelp.demo.request;

import java.util.*;

import javax.validation.constraints.NotNull;

import com.covidHelp.demo.model.MaterialType;

public class MaterialRequest {
    @NotNull(message = "Please enter the material type")
    private MaterialType materialType;
    private String category;
    @NotNull(message = "Quantity cannot be empty")
    private double quantity;
    @NotNull(message = "Enter at least one contact number")
    private List<Long> contactDetails;
    @NotNull(message = "District cannot be empty")
    private String district;
    @NotNull(message = "Address cannot be empty")
    private String address;
    private String createdBy;
    private long createdOn;
    private long lastUpdatedOn;
    @NotNull(message = "Organization cannot be empty")
    private String organization;


    public MaterialType getMaterialType() {
        return this.materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }


    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public List<Long> getContactDetails() {
        return this.contactDetails == null ? new ArrayList<Long>():this.contactDetails;
    }

    public void setContactDetails(List<Long> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public long getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public void setLastUpdatedOn(long lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

}
