package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class VisitedCityDto {
    private String cityName;
    private String lat;
    private String lng;
    private String date;
    private String notes;
    private boolean liked;
}
