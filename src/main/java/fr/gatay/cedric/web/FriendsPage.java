package fr.gatay.cedric.web;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.service.UsersService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class FriendsPage extends SuperPage {

    @SpringBean
    private AccountRepository accountRepository;

    @SpringBean
    private UsersService usersService;

    public FriendsPage(PageParameters pageParameters) {
        super();

        Account currentUser = usersService.getLoggedInUser();

        List<Account> friends = new ArrayList<>(accountRepository.findFriendsByAccount(currentUser)).stream()
            .filter(acc -> !acc.isDeleted())
            .collect(Collectors.toList());
        ListView<Account> friendsView = new ListView<Account>("listview", friends) {
            @Override
            protected void populateItem(ListItem<Account> item) {
                item.add(new Label("fullname", item.getModelObject().getFullName()));
                item.add(new Link<Void>("goto") {
                    @Override
                    public void onClick() {
                        setResponsePage(ProfilePage.class, new PageParameters().add("user_id", item.getModelObject().getId()));
                    }
                });
                item.add(new Link<Void>("deleteFriend") {
                    @Override
                    @Transactional
                    public void onClick() {
                        usersService.deleteFriend(currentUser, item.getModelObject());
                        setResponsePage(FriendsPage.class);
                    }
                });
                item.add(new Link<Void>("sendMessage") {
                    @Override
                    public void onClick() {
                        setResponsePage(MessagesPage.class, new PageParameters().add("user_id", item.getModelObject().getId()));
                    }
                });
            }
        };
        add(friendsView);
    }
}
