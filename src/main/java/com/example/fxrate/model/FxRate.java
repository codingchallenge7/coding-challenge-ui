package com.example.fxrate.model;

import java.time.Instant;

public class FxRate {
    private String ccyPair;
    private double rate;
    private Instant eventTime;

    public FxRate() {}

    public FxRate(String ccyPair, double rate, Instant eventTime) {
        this.ccyPair = ccyPair;
        this.rate = rate;
        this.eventTime = eventTime;
    }

    // getters and setters
    public String getCcyPair() { return ccyPair; }
    public void setCcyPair(String ccyPair) { this.ccyPair = ccyPair; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public Instant getEventTime() { return eventTime; }
    public void setEventTime(Instant eventTime) { this.eventTime = eventTime; }
}
