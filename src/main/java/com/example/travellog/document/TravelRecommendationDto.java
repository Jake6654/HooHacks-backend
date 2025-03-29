package com.example.travellog.document;

import lombok.Data;

import java.util.List;

@Data
public class TravelRecommendationDto {
    private String cityName;
    private String climate;
    private double exchangeRateUsd;
    private List<String> touristSpots;
    private String reason;

}
