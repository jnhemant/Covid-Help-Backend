package com.covidHelp.demo.request;

import java.util.List;

import javax.validation.constraints.NotNull;

public class MaterialUpdateRequest {
    @NotNull(message = "Quantity cannot be empty")
    private double quantity;
    @NotNull(message = "Enter at least one contact number")
    private List<Long> contactDetails;
    private long lastUpdatedOn;

    public double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public List<Long> getContactDetails() {
        return this.contactDetails;
    }

    public void setContactDetails(List<Long> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public long getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public void setLastUpdatedOn(long lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

}
