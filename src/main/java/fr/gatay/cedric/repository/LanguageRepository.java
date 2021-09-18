package fr.gatay.cedric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.gatay.cedric.model.Language;

public interface LanguageRepository extends CrudRepository<Language, Long> {

    @Query("FROM Language")
    List<Language> findAll();
}
