package com.covidHelp.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Material {
    private String id;
    private String category;
    private double quantity;
    private List<Long> contactDetails;
    private String district;
    private String address;
    private String createdBy;
    private long createdOn;
    private long lastUpdatedOn;
    private String organization;


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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
