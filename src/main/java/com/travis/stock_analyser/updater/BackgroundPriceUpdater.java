package com.travis.stock_analyser.updater;

import com.travis.stock_analyser.fetcher.StockPriceFetcherService;
import com.travis.stock_analyser.service.MultiSymbolPriceService;
import com.travis.stock_analyser.service.WatchedSymbolsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackgroundPriceUpdater
{
    private final WatchedSymbolsService watchedSymbolsService;
    private final StockPriceFetcherService fetcherService;
    private final MultiSymbolPriceService priceService;

    public BackgroundPriceUpdater(
            WatchedSymbolsService watchedSymbolsService,
            StockPriceFetcherService fetcherService,
            MultiSymbolPriceService priceService) {
        this.watchedSymbolsService = watchedSymbolsService;
        this.fetcherService = fetcherService;
        this.priceService = priceService;
    }

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void updateWatchedSymbols() {
        for (String symbol : watchedSymbolsService.getWatchedSymbols()) {
            double price = fetcherService.fetchLatestPrice(symbol);
            if (price > 0) {
                priceService.addPrice(symbol, price);
                System.out.printf("[UPDATE] %s -> %.2f%n", symbol, price);
            } else {
                System.out.println("[ERROR] Could not fetch price for: " + symbol);
            }
        }
    }
}