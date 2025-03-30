package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitedCityDTO {

    private String id;
    private String cityName;
    private String countryName;
    private String lat;
    private String lng;
    private String date;
    private String notes;
    private boolean liked;
}