package com.tuncayturk.storelocator.geo;

import com.tuncayturk.storelocator.model.GeoPoint;

public final class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0088;

    private DistanceCalculator() {
    }

    public static double distanceKm(GeoPoint from, GeoPoint to) {
        double lat1 = Math.toRadians(from.latitude());
        double lat2 = Math.toRadians(to.latitude());
        double deltaLat = Math.toRadians(to.latitude() - from.latitude());
        double deltaLon = Math.toRadians(to.longitude() - from.longitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
