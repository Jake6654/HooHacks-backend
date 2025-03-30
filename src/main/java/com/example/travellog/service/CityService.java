package com.example.travellog.service;

import com.example.travellog.document.City;
import com.example.travellog.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City saveCity(City city){
        return cityRepository.save(city);
    }
}