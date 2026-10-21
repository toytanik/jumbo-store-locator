package com.tuncayturk.storelocator.controller;

import com.tuncayturk.storelocator.dto.NearestStoresResponse;
import com.tuncayturk.storelocator.dto.StoreResponse;
import com.tuncayturk.storelocator.mapper.NearestStoresResponseMapper;
import com.tuncayturk.storelocator.model.GeoPoint;
import com.tuncayturk.storelocator.service.NearestStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final NearestStoreService nearestStoreService;

    @Operation(
            summary = "Find the nearest Jumbo stores",
            description = "Returns the nearest stores to the given position, ordered ascending by distance."
    )
    @ApiResponse(responseCode = "200", description = "Stores found, ordered ascending by distance",
            content = @Content(schema = @Schema(implementation = NearestStoresResponse.class)))
    @ApiResponse(responseCode = "400", description = "A parameter is missing, invalid, or out of range",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/nearest")
    public NearestStoresResponse nearest(
            @Parameter(description = "Latitude of the origin point", example = "52.0907")
            @RequestParam @DecimalMin("-90") @DecimalMax("90") double latitude,

            @Parameter(description = "Longitude of the origin point", example = "5.1214")
            @RequestParam @DecimalMin("-180") @DecimalMax("180") double longitude,

            @Parameter(description = "Number of stores to return (default 5)", example = "5")
            @RequestParam(defaultValue = "5") @Min(1) @Max(50) int limit
    ) {
        GeoPoint origin = new GeoPoint(latitude, longitude);
        List<StoreResponse> results = nearestStoreService.findNearest(origin, limit);

        return NearestStoresResponseMapper.toResponse(origin, results);
    }
}
