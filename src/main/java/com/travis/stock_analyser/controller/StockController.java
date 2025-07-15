package com.travis.stock_analyser.controller;

import com.travis.stock_analyser.dto.PriceRecord;
import com.travis.stock_analyser.fetcher.StockPriceFetcherService;
import com.travis.stock_analyser.service.MultiSymbolPriceService;
import com.travis.stock_analyser.service.PriceWindowService;
import com.travis.stock_analyser.service.WatchedSymbolsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController
{
    private final StockPriceFetcherService priceFetcherService;
    private final MultiSymbolPriceService  priceWindowService;
    private final WatchedSymbolsService watchedSymbolsService ;

    // Constructor injection for services
    public StockController(StockPriceFetcherService priceFetcherService, MultiSymbolPriceService multiSymbolPriceService, WatchedSymbolsService watchedSymbolsService) {
        this.priceFetcherService = priceFetcherService;
        this.priceWindowService = multiSymbolPriceService;
        this.watchedSymbolsService = watchedSymbolsService;
    }

    /**
     * Fetch latest price from external API,
     * Add price to sliding window for the symbol,
     * Return latest price, moving average, and window.
     */

    @GetMapping("/{symbol}/update")
    public ResponseEntity<Map<String, Object>> updateAndGetAverage(@PathVariable String symbol) {
        double price = priceFetcherService.fetchLatestPrice(symbol);

        // Handle API failure (negative price)
        if (price < 0) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Failed to fetch price for symbol: " + symbol));
        }

        // Add price to sliding window for this symbol
        priceWindowService.addPrice(symbol, price);

        // Get moving average and window for response
        double avg = priceWindowService.getMovingAverage(symbol);
        List<Double> window = priceWindowService.getPrices(symbol);
        double max = priceWindowService.getMax(symbol);
        double min = priceWindowService.getMin(symbol);

        Map<String, Object> response = new HashMap<>();
        response.put("symbol", symbol);
        response.put("latestPrice", price);
        response.put("movingAverage", avg);
        response.put("window", window);
        response.put("max", max);
        response.put("min", min);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/watch/{symbol}")
    public ResponseEntity<String> watchSymbol(@PathVariable String symbol) {
        watchedSymbolsService.addSymbol(symbol);
        return ResponseEntity.ok("Started watching: " + symbol.toUpperCase());
    }

    @DeleteMapping("/watch/{symbol}")
    public ResponseEntity<String> unwatchSymbol(@PathVariable String symbol) {
        watchedSymbolsService.removeSymbol(symbol);
        return ResponseEntity.ok("Stopped watching: " + symbol.toUpperCase());
    }

    @GetMapping("/watchlist")
    public ResponseEntity<Set<String>> getWatchedSymbols() {
        return ResponseEntity.ok(watchedSymbolsService.getWatchedSymbols());
    }
    @GetMapping("/{symbol}/ohlc")
    public ResponseEntity<List<PriceRecord>> getHistoricalPrices(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1min") String interval) {

        System.out.println("Fetching OHLC data for " + symbol + " with interval " + interval);

        List<PriceRecord> history = priceFetcherService.fetchHistoricalPrices(symbol, interval);

        if (history.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }

        return ResponseEntity.ok(history);
    }



}
