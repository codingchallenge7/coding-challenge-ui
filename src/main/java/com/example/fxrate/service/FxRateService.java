package com.example.fxrate.service;

import com.example.fxrate.model.FxRate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FxRateService {
    private final Map<String, Deque<FxRate>> ratesMap = new ConcurrentHashMap<>();
    private final AtomicInteger latestRateCallCount = new AtomicInteger(0);
    private volatile boolean exchangeRunning = true;

    public void addFxRate(FxRate fxRate) {
        String key = fxRate.getCcyPair();
        ratesMap.computeIfAbsent(key, k -> new ArrayDeque<>());
        Deque<FxRate> deque = ratesMap.get(key);
        if (deque.size() == 10) {
            deque.removeFirst();
        }
        deque.addLast(fxRate);
    }

    public List<FxRate> getLastTenRates(String ccyPair) {
        Deque<FxRate> deque = ratesMap.getOrDefault(ccyPair, new ArrayDeque<>());
        return new ArrayList<>(deque);
    }

    // Get all currency pairs with stored rates
    public Set<String> getAllPairs() {
        return ratesMap.keySet();
    }

    // Delete all rates for a currency pair
    public void deleteRatesForPair(String ccyPair) {
        ratesMap.remove(ccyPair);
    }

    // Delete all rates (clear all)
    public void deleteAllRates() {
        ratesMap.clear();
    }

    // Get the latest FX rate for a currency pair, generating a random rate each time.
    public FxRate getLatestRate(String ccyPair) {
        // Introduce artificial delay up to 7 seconds
        try {
            long delay = (long) (Math.random() * 7000L);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int count = latestRateCallCount.incrementAndGet();
        FxRate fxRate;

        if (count % 5 == 0) {
            // Every 5th call, use GBPUSD rate but return with requested ccyPair
            FxRate rate = generateRandomRate("GBPUSD");
            addFxRate(rate);
            return rate;
        } else {
            fxRate = generateRandomRate(ccyPair);
            addFxRate(fxRate);
            return fxRate;
        }
    }

    private FxRate generateRandomRate(String ccyPair) {
        double rate;
        if ("EURUSD".equalsIgnoreCase(ccyPair)) {
            rate = 1.05 + (1.15 - 1.05) * Math.random();
        } else if ("GBPUSD".equalsIgnoreCase(ccyPair)) {
            rate = 1.20 + (1.30 - 1.20) * Math.random();
        } else {
            rate = 0.5 + (1.5 - 0.5) * Math.random();
        }
        return new FxRate(ccyPair, rate, Instant.now());
    }

    public void setExchangeRunning(boolean running) {
        this.exchangeRunning = running;
    }

    public boolean isExchangeRunning() {
        return this.exchangeRunning;
    }

    public List<FxRate> getLatestTenRatesAcrossAllPairs() {
        List<FxRate> allRates = new ArrayList<>();
        for (Deque<FxRate> deque : ratesMap.values()) {
            allRates.addAll(deque);
        }
        allRates.sort(Comparator.comparing(FxRate::getEventTime).reversed());
        return allRates.stream().limit(10).collect(Collectors.toList());
    }
}
