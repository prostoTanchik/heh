package fr.gatay.cedric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.gatay.cedric.model.City;

public interface CityRepository extends CrudRepository<City, Long> {

    @Query("FROM City")
    List<City> findAll();
}
