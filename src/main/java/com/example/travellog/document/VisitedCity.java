package com.example.travellog.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VisitedCity {

    private String id = UUID.randomUUID().toString(); // 💡 자동 생성 ID

    private String cityName;
    private String lat;
    private String lng;
    private LocalDateTime date;
    private String notes;
    private String countryName;
    private boolean liked;
}
