package com.example.service;

public enum Regex {
    PHONE("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$"),
    MEDIALINK("(?:https?:)?\\/\\/(?:www\\.)?(?:facebook|fb|twitter|instagram|linkedin)\\.com\\/.*");

    public final String label;

    private Regex(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
