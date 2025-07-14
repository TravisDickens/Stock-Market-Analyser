package com.travis.stock_analyser.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceFetcherService
{
    @Value("${stock.api-key}")
    private String apiKey;

    @Value("${stock.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public double fetchLatestPrice(String symbol) {
        String url = String.format(
                "%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                baseUrl, symbol, apiKey
        );

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("API Response for symbol " + symbol + ": " + response.getBody());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String priceString = root.path("Global Quote").path("05. price").asText();
            return Double.parseDouble(priceString);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
