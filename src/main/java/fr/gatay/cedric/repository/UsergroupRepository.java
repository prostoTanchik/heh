package fr.gatay.cedric.repository;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Usergroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsergroupRepository extends CrudRepository<Usergroup, Long> {

    @Query("FROM Usergroup WHERE account = :account")
    List<Usergroup> findUsergroupByAccount(@Param("account") Account account);
}
