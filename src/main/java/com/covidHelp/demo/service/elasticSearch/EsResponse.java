package com.covidHelp.demo.service.elasticSearch;

import org.elasticsearch.search.SearchHit;

public class EsResponse {
    
    private SearchHit[] hits = new SearchHit[0];
    private long totalHits;
    private String message;
    private String docId;

    public SearchHit[] getHits() {
        return this.hits;
    }

    public void setHits(SearchHit[] hits) {
        this.hits = hits;
    }

    public long getTotalHits() {
        return this.totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
