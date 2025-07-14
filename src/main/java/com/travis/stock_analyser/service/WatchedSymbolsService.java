package com.travis.stock_analyser.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class WatchedSymbolsService
{
    private final Set<String> symbols = Collections.synchronizedSet(new HashSet<>());

    public void addSymbol(String symbol) {
        symbols.add(symbol.toUpperCase());
    }

    public void removeSymbol(String symbol) {
        symbols.remove(symbol.toUpperCase());
    }

    public Set<String> getWatchedSymbols() {
        return Set.copyOf(symbols);
    }

    public void clearAll() {
        symbols.clear();
    }
}
