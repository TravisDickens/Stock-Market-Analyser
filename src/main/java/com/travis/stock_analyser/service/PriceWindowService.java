package com.travis.stock_analyser.service;

import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PriceWindowService
{
    private final int windowSize;
    // deque to store prices in order
    private final Deque<Double> window;
    // sum of elements in the window for O(1) average
    private double sum;

    // Constructor - initialize window size and data structures
    public PriceWindowService(int windowSize) {
        this.windowSize = windowSize;
        this.window = new ArrayDeque<>();
        this.sum = 0.0;
    }

    /**
     * Add a new price to the sliding window.
     * If window size exceeds limit, remove oldest price.
     */

    public void addPrice(double price) {
        window.addLast(price);
        sum += price;
        if (window.size() > windowSize) {
            // Remove oldest price and update sum
            double removed = window.removeFirst();
            sum -= removed;
        }
    }

    /**
     * Calculate and return the current moving average.
     * Returns 0 if window is empty.
     */

    public double getMovingAverage() {
        if (window.isEmpty()) return 0;
        return sum / window.size();
    }

    /**
     * Return a list of prices currently in the window.
     */

    public List<Double> getPrices() {
        return new ArrayList<>(window);
    }
}
