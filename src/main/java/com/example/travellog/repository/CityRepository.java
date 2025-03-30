package com.example.travellog.repository;

import com.example.travellog.document.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends MongoRepository<City, String> {
    City findByCityName(String name);
}