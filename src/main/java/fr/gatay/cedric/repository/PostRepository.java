package fr.gatay.cedric.repository;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("FROM Post WHERE user.account = :account ORDER BY creationDate DESC")
    List<Post> findPostsbyUser(@Param("account") Account account);

    @Query("SELECT DISTINCT post FROM Post post, Usergroup usergroup, Account account  " +
            "WHERE (:account IN elements(account.friends) OR :account = account) " +
            "AND (usergroup.account = account OR usergroup.account = :account) " +
            "AND post.user = usergroup " +
            "AND account.deleted = FALSE ORDER BY post.creationDate DESC")
    List<Post> findFriendsPostsByFriends( @Param("account") Account account);
}
