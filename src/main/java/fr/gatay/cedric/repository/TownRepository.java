package fr.gatay.cedric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.gatay.cedric.model.Town;


public interface TownRepository extends CrudRepository<Town, Long> {

    @Query("FROM Town")
    List<Town> findAll();
}
