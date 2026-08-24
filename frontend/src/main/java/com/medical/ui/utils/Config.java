package com.medical.ui.utils;

public final class Config {
    private Config() {}
    public static final String API_BASE_URL =
            System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080");
}
