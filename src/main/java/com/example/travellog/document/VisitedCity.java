package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VisitedCity {

    private String cityID;
    private LocalDate visitedAT;
    private String description;
    private List<String> photos;
    private boolean liked;
}
