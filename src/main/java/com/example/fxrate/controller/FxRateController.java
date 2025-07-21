package com.example.fxrate.controller;

import com.example.fxrate.model.FxRate;
import com.example.fxrate.service.FxRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api/fxrate")
public class FxRateController {

    @Autowired
    private FxRateService fxRateService;

    @PostMapping
    public void addFxRate(@RequestBody FxRate fxRate) {
        fxRateService.addFxRate(fxRate);
    }

    @GetMapping
    public List<FxRate> getLastTenRates(@RequestParam(required = false) String ccyPair) {
        if (ccyPair == null || ccyPair.isEmpty()) {
            return fxRateService.getLatestTenRatesAcrossAllPairs();
        }
        return fxRateService.getLastTenRates(ccyPair);
    }

    // Get all currency pairs with stored rates
    @GetMapping("/pairs")
    public Set<String> getAllPairs() {
        return fxRateService.getAllPairs();
    }

    // Delete all rates for a currency pair
    @DeleteMapping
    public void deleteRatesForPair(@RequestParam String ccyPair) {
        fxRateService.deleteRatesForPair(ccyPair);
    }

    // Delete all rates (clear all)
    @DeleteMapping("/all")
    public void deleteAllRates() {
        fxRateService.deleteAllRates();
    }

    // Get the latest FX rate for a currency pair
    @GetMapping("/latest")
    public FxRate getLatestRate(@RequestParam String ccyPair) {
        return fxRateService.getLatestRate(ccyPair);
    }

    // Generate random rates for EURUSD and GBPUSD
    @PostMapping("/generate-random")
    public void generateRandomRates() {
        Random random = new Random();
        FxRate eurusd = new FxRate(
            "EURUSD",
            1.05 + (1.15 - 1.05) * random.nextDouble(),
            Instant.now()
        );
        FxRate gbpusd = new FxRate(
            "GBPUSD",
            1.20 + (1.30 - 1.20) * random.nextDouble(),
            Instant.now()
        );
        fxRateService.addFxRate(eurusd);
        fxRateService.addFxRate(gbpusd);
    }

    @GetMapping("/exchange/status")
    public boolean isExchangeRunning() {
        return fxRateService.isExchangeRunning();
    }
}
