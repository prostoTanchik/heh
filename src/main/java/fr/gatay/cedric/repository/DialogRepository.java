package fr.gatay.cedric.repository;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Dialog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DialogRepository extends CrudRepository<Dialog, Long> {

    @Query("FROM Dialog dialog WHERE (dialog.firstUser = :fuser AND dialog.secondUser = :suser) " +
            "OR (dialog.firstUser = :suser AND dialog.secondUser = :fuser)")
    Optional<Dialog> findByUsers(@Param("fuser")Account firstUser, @Param("suser") Account secondUser);

    @Query("FROM Dialog WHERE firstUser = :user OR secondUser = :user")
    List<Dialog> findByUser(@Param("user") Account user);
}
