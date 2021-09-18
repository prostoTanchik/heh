package fr.gatay.cedric.repository;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Language;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT COUNT(acc) > 0 FROM Account acc WHERE acc.deleted = FALSE")
    boolean existsByEmail(@Param("email") String email);
    
    @Query("FROM Account WHERE email = :email AND deleted = FALSE")
    Account findByEmail(@Param("email") String email);

    @Query("FROM Account WHERE deleted = FALSE")
    List<Account> findTest();

    @Query("SELECT acc.friends FROM Account as acc WHERE acc = :account AND acc.deleted = FALSE")
    Set<Account> findFriendsByAccount(@Param("account") Account account);
}
