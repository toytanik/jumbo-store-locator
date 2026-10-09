package com.tuncayturk.storelocator.model;

public record GeoPoint(double latitude, double longitude) {

    public GeoPoint {
        requireInRange("latitude", latitude, 90);
        requireInRange("longitude", longitude, 180);
    }

    private static void requireInRange(String name, double value, double limit) {
        if (!Double.isFinite(value) || value < -limit || value > limit) {
            throw new IllegalArgumentException(
                    "%s must be between -%s and %s, was: %s".formatted(name, limit, limit, value));
        }
    }
}
