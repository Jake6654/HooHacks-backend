package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VisitedCity {

    private String cityName;
    private String lat;
    private String lng;
    private LocalDateTime date;
    private String notes;
    private boolean liked;
}
