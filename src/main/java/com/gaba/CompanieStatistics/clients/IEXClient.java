package com.gaba.CompanieStatistics.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaba.CompanieStatistics.entities.CompanyDTO;
import com.gaba.CompanieStatistics.entities.Stock;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class IEXClient {

    private final String SECRET_KEY;
    private final String ACCESS_KEY;
    private final String stockUrl;

    private final WebClient companiesClient;
    private final WebClient stocksClient;

    private final ObjectMapper mapper;

    public IEXClient(@Value("${iex.access.key}") String accessKey,
                     @Value("${iex.secret.key}") String secretKey,
                     @Value("${iex.companies.url}") String companiesUrl,
                     @Value("${iex.stock.url}") String stockUrl,
                     @Value("${webclient.connection.timeout}") int connectionTimeout,
                     @Value("${webclient.response.timeout}") int responseTimeout,
                     @Value("${iex.base.url}") String baseUrl) {

        this.SECRET_KEY = secretKey;
        this.ACCESS_KEY = accessKey;

        this.stockUrl = stockUrl;
        mapper = new ObjectMapper();

        companiesClient = WebClient.builder()
                .baseUrl(baseUrl + companiesUrl)
                .defaultUriVariables(Collections.singletonMap("token", accessKey))
                .build();
        stocksClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultUriVariables(Collections.singletonMap("token", accessKey))
                .build();
    }

    public Flux<CompanyDTO> getEnabledCompanies() {
        return companiesClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("token", ACCESS_KEY).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(resp -> resp.bodyToFlux(CompanyDTO.class))
                .filter(CompanyDTO::isEnabled);
    }

    public Flux<Stock> getStock(String company) {
        return stocksClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path(String.format(stockUrl, company))
                                .queryParam("token", ACCESS_KEY)
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(resp -> resp.bodyToFlux(String.class))
                .map(stock -> {
                    try {
                        return mapper.readValue(stock, Stock.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).onErrorComplete();
    }
}
