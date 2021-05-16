package com.covidHelp.demo.model;

import java.util.Map;
import java.util.List;

public class GenericElasticSearchRequest {
    private Map<String, String> exactMatches;
    private Integer offset;
    private Integer records;
    private List<String> includeFields;
    public Boolean fetchSource;
    private Map<String, List<String>> exactMultiMatches;
    private String[] searchFields;
    private String searchKey;

    private String sortBy;
    private String sortOrder;
    private String collapseOn;


    public Map<String,String> getExactMatches() {
        return this.exactMatches;
    }

    public void setExactMatches(Map<String,String> exactMatches) {
        this.exactMatches = exactMatches;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getRecords() {
        return this.records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public List<String> getIncludeFields() {
        return this.includeFields;
    }

    public void setIncludeFields(List<String> includeFields) {
        this.includeFields = includeFields;
    }

    public Boolean isFetchSource() {
        return this.fetchSource;
    }

    public Boolean getFetchSource() {
        return this.fetchSource;
    }

    public void setFetchSource(Boolean fetchSource) {
        this.fetchSource = fetchSource;
    }

    public Map<String,List<String>> getExactMultiMatches() {
        return this.exactMultiMatches;
    }

    public void setExactMultiMatches(Map<String,List<String>> exactMultiMatches) {
        this.exactMultiMatches = exactMultiMatches;
    }

    public String[] getSearchFields() {
        return this.searchFields;
    }

    public void setSearchFields(String[] searchFields) {
        this.searchFields = searchFields;
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCollapseOn() {
        return this.collapseOn;
    }

    public void setCollapseOn(String collapseOn) {
        this.collapseOn = collapseOn;
    }


}
