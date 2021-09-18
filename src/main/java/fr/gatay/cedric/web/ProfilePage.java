package fr.gatay.cedric.web;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Post;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.repository.PostRepository;
import fr.gatay.cedric.service.UsersService;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ProfilePage extends SuperPage {

    private static final long serialVersionUID = 10000L;

    private static final String URL = "data:image/png;base64,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";

    @SpringBean
    private UsersService usersService;

    @SpringBean
    private AccountRepository accountRepository;

    @SpringBean
    private PostRepository postRepository;

    private IModel<String> postModel = Model.of("");
    private ListModel<Post> postsModel;
    private IModel<Account> currentUserModel;
    private IModel<Account> maintainerModel;

    private Form<Void> form;
    private WebMarkupContainer postsContainer;

    public ProfilePage(PageParameters pageParameters) {
        super();

        currentUserModel = (IModel<Account>) () -> usersService.getLoggedInUser();
        if (!pageParameters.get("user_id").isEmpty() && accountRepository.findById(pageParameters.get("user_id").to(Long.class)).isPresent()) {
            maintainerModel = (IModel<Account>) () -> accountRepository.findById(pageParameters.get("user_id").to(Long.class)).get();
        } else {
            maintainerModel = (IModel<Account>) () -> currentUserModel.getObject();
        }

        DynamicImageResource resource = new DynamicImageResource(){

            @Override
            protected byte[] getImageData(Attributes attributes) {
                return maintainerModel.getObject().getAvatar();
            }
        };

        Image avatar;
        if (maintainerModel.getObject().getAvatar() == null) {
            avatar = new Image("avatar", "");
            avatar.add(new AttributeModifier("src", URL));
        } else {
            avatar = new Image("avatar", resource);
        }
        add(avatar);

        Label fullName = new Label("fullName", maintainerModel.getObject().getFullName());
        add(fullName);

        Label country = new Label("country", 
                "Страна: " + (maintainerModel.getObject().getCity() == null ? "не указано" : maintainerModel.getObject().getCity().getName()));
        add(country);

        Label city = new Label("city",
                "Город: " + (maintainerModel.getObject().getCity() == null ? "не указан" : maintainerModel.getObject().getTown().getName()));
        add(city);

        Label language = new Label("language",
                "Язык: " + (maintainerModel.getObject().getLanguage() == null ? "не указан" : maintainerModel.getObject().getLanguage().getLanguage()));
        add(language);

        form = new Form<>("form");
        form.setOutputMarkupPlaceholderTag(true);
        form.setVisible(currentUserModel.getObject().equals(maintainerModel.getObject()));
        add(form);

        TextArea<String> textArea = new TextArea<>("textArea", postModel);
        form.add(textArea);

        AjaxButton newPost = new AjaxButton("newPost") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                usersService.savePost(maintainerModel.getObject(), postModel.getObject());
                postsModel.setObject(postRepository.findPostsbyUser(maintainerModel.getObject()));
                target.add(postsContainer);
                target.add(form);
            }
        };
        form.add(newPost);

        postsContainer = new WebMarkupContainer("posts");
        postsContainer.setOutputMarkupId(true);
        add(postsContainer);

        postsModel = new ListModel<>(postRepository.findPostsbyUser(maintainerModel.getObject()));
        ListView<Post> postsView = new ListView<Post>("post", postsModel) {
            @Override
            protected void populateItem(ListItem<Post> item) {
                item.add(new Label("postText", item.getModelObject().getText()));
                item.add(new Label("datePost", item.getModelObject().getCreationDate()));
                item.add(new Label("author", item.getModelObject().getUser().getAccount().getFullName()));
                Form<Void> form = new Form<>("form");
                form.setOutputMarkupPlaceholderTag(true);
                form.setVisible(item.getModelObject().getUser().getAccount().equals(currentUserModel.getObject())
                        || currentUserModel.getObject().getRole().equals(Account.Role.ADMIN));
                item.add(form);
                AjaxButton edit = new AjaxButton("edit") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                        setResponsePage(EditPostPage.class, new PageParameters().add("post_id", item.getModelObject().getId()));
                    }
                };
                form.add(edit);
                AjaxButton delete = new AjaxButton("delete") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                        usersService.deletePost(maintainerModel.getObject(), item.getModelObject());
                        postRepository.delete(item.getModelObject());
                        postsModel.setObject(postRepository.findPostsbyUser(maintainerModel.getObject()));
                        target.add(postsContainer);
                    }
                };
                form.add(delete);
            }
        };
        postsView.setOutputMarkupId(true);
        postsContainer.add(postsView);
    }

    @Override
    protected void detachModel() {
        super.detachModel();
        postModel.detach();
        postsModel.detach();
        currentUserModel.detach();
        maintainerModel.detach();
    }
}

