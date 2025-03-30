package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VisitedCity {

    @Id
    private String id;

    private String cityName;
    private String lat;
    private String lng;
    private LocalDateTime date;
    private String notes;
    private String countryName;
    private boolean liked;
}
