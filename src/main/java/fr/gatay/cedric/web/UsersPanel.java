package fr.gatay.cedric.web;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.service.UsersService;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

class UsersPanel extends Panel {

    @SpringBean
    private AccountRepository accountRepository;

    @SpringBean
    private UsersService usersService;

    UsersPanel(String id) {
        super(id);

        Account currentUser = usersService.getLoggedInUser();
        List<Account> users = accountRepository.findTest();
        users.remove(usersService.getLoggedInUser());

        ListView<Account> view = new ListView<Account>("listview", users) {
            @Override
            protected void populateItem(ListItem<Account> item) {
                item.add(new Label("username", item.getModelObject().getFullName()));
                item.add(new Link<Void>("goto") {
                    @Override
                    public void onClick() {
                        setResponsePage(new ProfilePage(new PageParameters().add("user_id", item.getModelObject().getId())));
                    }
                });
                item.add(new Link<Void>("addFriend") {
                    @Override
                    @Transactional
                    public void onClick() {
                        usersService.addFriend(currentUser, item.getModelObject());
                    }
                });
                item.add(new Link<Void>("deleteFriend") {
                    @Override
                    @Transactional
                    public void onClick() {
                        usersService.deleteFriend(currentUser, item.getModelObject());
                    }
                });
                Link<Void> deleteUser = new Link<Void>("deleteUser") {
                    @Override
                    public void onClick() {
                        item.getModelObject().setDeleted(true);
                        accountRepository.save(item.getModelObject());
                        setResponsePage(SearchPage.class);
                    }
                };
                deleteUser.setVisible(currentUser.getRole().equals(Account.Role.ADMIN));
                item.add(deleteUser);
            }
        };
        add(view);

    }
}
