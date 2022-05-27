package com.elasticsearch.authentication.config;

import lombok.extern.java.Log;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log
public class ElasticSearchConnector {

    @Value("${elasticsearch.host}")
    public String host;

    @Value("${elasticsearch.username}")
    public String username;

    @Value("${elasticsearch.password}")
    public String password;

    @Bean
    public RestHighLevelClient client() {

        log.info("Host -- " + host);
        log.info("username : "+username);
        log.info("password : "+password);

        HttpHost httpHost = HttpHost.create(host);

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));

//        return new RestHighLevelClient(RestClient.builder(httpHost));
        return new RestHighLevelClient(RestClient.builder(httpHost).setHttpClientConfigCallback(httpAsyncClientBuilder ->

            httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
        ).setRequestConfigCallback(request->request.setConnectionRequestTimeout(6000000).setSocketTimeout(6000000)));
    }
}
