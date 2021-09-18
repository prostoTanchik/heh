package fr.gatay.cedric.service;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Post;
import fr.gatay.cedric.model.Usergroup;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.repository.PostRepository;
import fr.gatay.cedric.repository.UsergroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UsersService {
    private final Logger LOG = LoggerFactory.getLogger(UsersService.class);

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private UsergroupRepository usergroupRepository;

    @Resource
    private PostRepository postRepository;

    public Account getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountRepository.findByEmail(authentication.getName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addFriend(Account firstUser, Account secondUser) {
        Set<Account> friends = accountRepository.findFriendsByAccount(firstUser);
        friends.add(secondUser);
        firstUser.setFriends(friends);
        friends = accountRepository.findFriendsByAccount(secondUser);
        friends.add(firstUser);
        secondUser.setFriends(friends);
        accountRepository.save(firstUser);
        accountRepository.save(secondUser);
        LOG.info("User {} added user {} to friends", firstUser.getFullName(), secondUser.getFullName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteFriend(Account firstUser, Account secondUser) {
        Set<Account> friends = accountRepository.findFriendsByAccount(firstUser);
        friends.remove(secondUser);
        firstUser.setFriends(friends);
        friends = accountRepository.findFriendsByAccount(secondUser);
        friends.remove(firstUser);
        secondUser.setFriends(friends);
        accountRepository.save(firstUser);
        accountRepository.save(secondUser);
        LOG.info("User {} removed {} from friends", firstUser.getFullName(), secondUser.getFullName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePost(Account account, String text) {
        Post post = new Post();
        post.setText(text);
        Usergroup user = usergroupRepository.findUsergroupByAccount(account).get(0);
        post.setUser(user);
        post.setCreationDate(new Date());
        postRepository.save(post);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deletePost(Account account, Post post) {
        List<Post> posts = postRepository.findPostsbyUser(account);
        posts.remove(post);
        Usergroup usergroup = usergroupRepository.findUsergroupByAccount(account).get(0);
        usergroup.setPosts(posts);
        usergroupRepository.save(usergroup);
        LOG.info("User {} removed post with text {}", account.getFullName(), post.getText());
    }
}
