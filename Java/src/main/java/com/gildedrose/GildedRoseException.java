package com.gildedrose;

public class GildedRoseException extends RuntimeException {

    private final int statusCode;
    private final String statusMessage;

    public GildedRoseException(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
