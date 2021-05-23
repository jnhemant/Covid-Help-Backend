package com.covidHelp.demo.dao;

import java.util.List;
import java.util.Map;

import com.covidHelp.demo.model.GenericElasticSearchRequest;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchQueryHelper {
    
    /**
     * Build boolquery with several options
     * @param request - filters to be used in the query
     * @return - BoolqueryBuilder object containing all given filters
     */
    public BoolQueryBuilder buildBoolQuery(GenericElasticSearchRequest request){
        Map<String, String> exactMatches = request.getExactMatches();
        Map<String, List<String>> exactMultiMatches = request.getExactMultiMatches();
        
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(exactMatches != null){
            for(Map.Entry<String, String> exactMatch : exactMatches.entrySet()){
                boolQueryBuilder.must(QueryBuilders.termQuery(exactMatch.getKey(), exactMatch.getValue()));
            }
        }

        if(exactMultiMatches != null){
            for(Map.Entry<String, List<String>> multiMatch : exactMultiMatches.entrySet()){
                boolQueryBuilder.must(QueryBuilders.termsQuery(multiMatch.getKey(), multiMatch.getValue()));
            }
        }

        if(request.getSearchKey() != null && !request.getSearchKey().isEmpty()){
            boolQueryBuilder.must(
                QueryBuilders
                    .multiMatchQuery(request.getSearchKey(), request.getSearchFields())
                    .operator(Operator.AND)
                    .analyzer("standard"));
        }

        return boolQueryBuilder;
    }
}
