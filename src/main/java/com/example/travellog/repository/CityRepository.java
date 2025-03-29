package com.example.travellog.repository;

import com.example.travellog.document.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityRepository extends MongoRepository<City, String> {
    City findByCityName(String name);
}
