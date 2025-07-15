package com.travis.stock_analyser.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travis.stock_analyser.dto.PriceRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StockPriceFetcherService {

    @Value("${stock.api-key}")
    private String apiKey;

    @Value("${stock.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<PriceRecord> fetchHistoricalPrices(String symbol) {
        return fetchHistoricalPrices(symbol, "1min", 60);
    }

    public List<PriceRecord> fetchHistoricalPrices(String symbol, String interval) {
        return fetchHistoricalPrices(symbol, interval, 60);
    }

    public List<PriceRecord> fetchHistoricalPrices(String symbol, String interval, int outputsize) {
        try {
            String url = String.format(
                    "%s/time_series?symbol=%s&interval=%s&outputsize=%d&apikey=%s",
                    baseUrl, symbol, interval, outputsize, apiKey
            );

            System.out.println("Fetching: " + url); // Debug log

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            // Check for API errors
            if (root.has("status") && "error".equals(root.path("status").asText())) {
                System.err.println("[ERROR] API Error: " + root.path("message").asText());
                return Collections.emptyList();
            }

            List<PriceRecord> history = new ArrayList<>();
            JsonNode values = root.path("values");

            if (values.isMissingNode() || values.isEmpty()) {
                System.err.println("[ERROR] No 'values' in response for " + symbol);
                System.err.println("Response: " + response.getBody());
                return Collections.emptyList();
            }

            for (JsonNode node : values) {
                PriceRecord record = new PriceRecord();
                record.setTimestamp(node.path("datetime").asText());
                record.setOpen(Double.parseDouble(node.path("open").asText()));
                record.setHigh(Double.parseDouble(node.path("high").asText()));
                record.setLow(Double.parseDouble(node.path("low").asText()));
                record.setClose(Double.parseDouble(node.path("close").asText()));
                history.add(record);
            }

            // Reverse to oldest → newest
            Collections.reverse(history);
            System.out.println("Fetched " + history.size() + " records for " + symbol);
            return history;

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch historical prices for " + symbol + ": " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public double fetchLatestPrice(String symbol) {
        List<PriceRecord> history = fetchHistoricalPrices(symbol);
        if (!history.isEmpty()) {
            return history.get(history.size() - 1).getClose();
        } else {
            return -1;
        }
    }
}
