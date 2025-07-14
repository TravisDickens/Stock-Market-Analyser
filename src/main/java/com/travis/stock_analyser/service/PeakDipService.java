package com.travis.stock_analyser.service;

import java.util.ArrayDeque;
import java.util.Deque;

public class PeakDipService
{
    private final int windowSize;
    // stores candidates for max decreasing order
    private final Deque<Double> maxDeque;
    // stores candidates for min, increasing order
    private final Deque<Double> minDeque;
    // tracks full window
    private final Deque<Double> window;

    public PeakDipService(int windowSize)
    {
        this.windowSize = windowSize;
        this.maxDeque = new ArrayDeque<>();
        this.minDeque = new ArrayDeque<>();
        this.window = new ArrayDeque<>();
    }

    public void addPrice(double price)
    {
        window.addLast(price);

        // Remove smaller elements from maxDeque tail
        while (!maxDeque.isEmpty() && maxDeque.peekLast() < price) {
            maxDeque.removeLast();
        }
        maxDeque.addLast(price);

        // Remove larger elements from minDeque tail
        while (!minDeque.isEmpty() && minDeque.peekLast() > price) {
            minDeque.removeLast();
        }
        minDeque.addLast(price);

        // Remove from window if size exceeded
        if (window.size() > windowSize) {
            double removed = window.removeFirst();
            // Remove from maxDeque if equal to removed
            if (!maxDeque.isEmpty() && maxDeque.peekFirst() == removed) {
                maxDeque.removeFirst();
            }
            // Remove from minDeque if equal to removed
            if (!minDeque.isEmpty() && minDeque.peekFirst() == removed) {
                minDeque.removeFirst();
            }
        }
    }

    public double getMax() {
        return maxDeque.isEmpty() ? Double.NaN : maxDeque.peekFirst();
    }

    public double getMin() {
        return minDeque.isEmpty() ? Double.NaN : minDeque.peekFirst();
    }

}
