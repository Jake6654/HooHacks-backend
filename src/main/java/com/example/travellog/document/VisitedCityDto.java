package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class VisitedCityDto {
    private String cityId;
    private LocalDate visitedAt;
    private String description;
    private java.util.List<String> photos;
    private boolean liked;
}
