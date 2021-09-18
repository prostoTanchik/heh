package fr.gatay.cedric.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.gatay.cedric.model.Photo;
import fr.gatay.cedric.model.Usergroup;

public interface PhotoRepository extends CrudRepository<Photo, Long> {


    @Query("SELECT ug.photos FROM Usergroup ug WHERE ug = :usergroup")
    Set<Photo> findPhotosByUsergroup(@Param("usergroup") Usergroup usergroup);
}
