package com.travis.stock_analyser.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MultiSymbolPriceService
{
    // Map each symbol to its PriceWindowService
    private final ConcurrentMap<String, PriceWindowService> symbolWindows = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, PeakDipService> symbolPeakDip = new ConcurrentHashMap<>();

    // Default window size for moving average
    private static final int DEFAULT_WINDOW_SIZE = 5;

    /**
     * Add price for a symbol.
     * Creates a new PriceWindowService if symbol is new.
     */

    public void addPrice(String symbol, double price) {
        // Get or create the PriceWindowService for this symbol
        PriceWindowService windowService = symbolWindows.computeIfAbsent(symbol, k -> new PriceWindowService(DEFAULT_WINDOW_SIZE));
        windowService.addPrice(price);

        PeakDipService peakDipService = symbolPeakDip.computeIfAbsent(symbol, k -> new PeakDipService(DEFAULT_WINDOW_SIZE));
        peakDipService.addPrice(price);
    }

    /**
     * Get moving average for a symbol.
     * Returns 0 if symbol not found.
     */

    public double getMovingAverage(String symbol) {
        PriceWindowService windowService = symbolWindows.get(symbol);
        if (windowService == null) return 0.0;
        return windowService.getMovingAverage();
    }

    /**
     * Get price window for a symbol.
     * Returns empty list if symbol not found.
     */
    public List<Double> getPrices(String symbol) {
        PriceWindowService windowService = symbolWindows.get(symbol);
        if (windowService == null) return List.of();
        return windowService.getPrices();
    }

    public double getMax(String symbol) {
        PeakDipService peakDipService = symbolPeakDip.get(symbol);
        if (peakDipService == null) return Double.NaN;
        return peakDipService.getMax();
    }

    public double getMin(String symbol) {
        PeakDipService peakDipService = symbolPeakDip.get(symbol);
        if (peakDipService == null) return Double.NaN;
        return peakDipService.getMin();
    }
}
