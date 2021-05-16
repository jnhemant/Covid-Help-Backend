package com.covidHelp.demo.service.elasticSearch;

import java.util.Map;

import org.elasticsearch.index.query.QueryBuilder;

public class EsRequest {

    QueryBuilder queryBuilder;
    private Integer offset;
    private Integer noOfRecords;
    private String[] indices;
    private String[] includeFields;
    private String[] excludeFields;
    private boolean fetchSource=true;
    private String docId;
    private String index;
    private boolean refreshPolicy;
    private Object docObject;
    private Map<String, String> sortMap;


    public QueryBuilder getQueryBuilder() {
        return this.queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getNoOfRecords() {
        return this.noOfRecords;
    }

    public void setNoOfRecords(Integer noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    public String[] getIndices() {
        return this.indices;
    }

    public void setIndices(String[] indices) {
        this.indices = indices;
    }

    public String[] getIncludeFields() {
        return this.includeFields;
    }

    public void setIncludeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

    public String[] getExcludeFields() {
        return this.excludeFields;
    }

    public void setExcludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

    public boolean isFetchSource() {
        return this.fetchSource;
    }

    public boolean getFetchSource() {
        return this.fetchSource;
    }

    public void setFetchSource(boolean fetchSource) {
        this.fetchSource = fetchSource;
    }

    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getIndex() {
        return this.index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isRefreshPolicy() {
        return this.refreshPolicy;
    }

    public boolean getRefreshPolicy() {
        return this.refreshPolicy;
    }

    public void setRefreshPolicy(boolean refreshPolicy) {
        this.refreshPolicy = refreshPolicy;
    }

    public Object getDocObject() {
        return this.docObject;
    }

    public void setDocObject(Object docObject) {
        this.docObject = docObject;
    }

    public Map<String,String> getSortMap() {
        return this.sortMap;
    }

    public void setSortMap(Map<String,String> sortMap) {
        this.sortMap = sortMap;
    }

    public EsRequest queryBuilder(QueryBuilder bq){
        setQueryBuilder(bq);
        return this;
    }

    public EsRequest sortMap(Map<String, String> sortMap){
        setSortMap(sortMap);
        this.sortMap = sortMap;
        return this;
    }

    public EsRequest offset(Integer offset){
        setOffset(offset);
        return this;
    }

    public EsRequest noOfRecords(Integer noOfRecords){
        setNoOfRecords(noOfRecords);
        return this;
    }

    public EsRequest indices(String[] indices){
        setIndices(indices);
        return this;
    }

    public EsRequest includeFields(String[] includeFields){
        setIncludeFields(includeFields);
        return this;
    }

    public EsRequest excludeFields(String[] excludeFields){
        setExcludeFields(excludeFields);
        return this;
    }

    public EsRequest fetchSource(boolean fetchSource){
        setFetchSource(fetchSource);
        return this;
    }

    public EsRequest index(String index){
        setIndex(index);
        return this;
    }

    public EsRequest docObject(Object docObject){
        setDocObject(docObject);
        return this;
    }

    public EsRequest refreshPolicy(boolean refreshPolicy){
        setRefreshPolicy(refreshPolicy);
        return this;
    }

    public EsRequest docId(String docId){
        setDocId(docId);
        return this;
    }
}
