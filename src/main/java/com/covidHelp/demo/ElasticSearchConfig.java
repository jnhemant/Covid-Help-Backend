package com.covidHelp.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ElasticSearchConfig{

    @Value("${elasticsearch.ip}")
    private String elasticSearchIp;

    @Value("${elasticsearch.port}")
    private String elasticSearchPort;

    @Bean
    @Scope(value = "singleton")
    public RestHighLevelClient elasticsearchClient() {
        
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            new HttpHost(elasticSearchIp, Integer.parseInt(elasticSearchPort), "http")));
        return client;
    }
}
