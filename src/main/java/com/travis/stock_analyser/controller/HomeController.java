package com.travis.stock_analyser.controller;

import com.travis.stock_analyser.service.MultiSymbolPriceService;
import com.travis.stock_analyser.service.WatchedSymbolsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class HomeController {

    private final WatchedSymbolsService watchedSymbolsService;
    private final MultiSymbolPriceService priceService;

    public HomeController(WatchedSymbolsService watchedSymbolsService,
                          MultiSymbolPriceService priceService) {
        this.watchedSymbolsService = watchedSymbolsService;
        this.priceService = priceService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        Set<String> symbols = watchedSymbolsService.getWatchedSymbols();

        List<Map<String, Object>> stats = new ArrayList<>();
        for (String symbol : symbols) {
            Map<String, Object> data = new HashMap<>();
            data.put("symbol", symbol);
            data.put("latestPrice", priceService.getPrices(symbol).stream().reduce((a, b) -> b).orElse(0.0));
            data.put("average", priceService.getMovingAverage(symbol));
            data.put("max", priceService.getMax(symbol));
            data.put("min", priceService.getMin(symbol));
            stats.add(data);
        }

        model.addAttribute("stats", stats);
        return "dashboard";  // maps to dashboard.html
    }

    @PostMapping("/watch")
    public String watchSymbol(@RequestParam String symbol) {
        watchedSymbolsService.addSymbol(symbol);
        return "redirect:/";
    }

    @PostMapping("/unwatch")
    public String unwatchSymbol(@RequestParam String symbol) {
        watchedSymbolsService.removeSymbol(symbol);
        return "redirect:/";
    }
}
