package fr.gatay.cedric.web;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Dialog;
import fr.gatay.cedric.model.Message;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.repository.DialogRepository;
import fr.gatay.cedric.repository.MessageRepository;
import fr.gatay.cedric.service.UsersService;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

public class MessagesPage extends SuperPage {

    @SpringBean
    private AccountRepository accountRepository;

    @SpringBean
    private DialogRepository dialogRepository;

    @SpringBean
    private MessageRepository messageRepository;

    @SpringBean
    private UsersService usersService;

    private static final String URL = "data:image/png;base64,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";

    private IModel<Account> currentUserModel;
    private IModel<Account> receiverModel;
    private IModel<String> messageModel = Model.of("");
    private IModel<Dialog> dialogModel = () -> null;
    private IModel<Message> messageObjModel = () -> new Message();

    public MessagesPage(PageParameters pageParameters) {
        super();

        currentUserModel = (IModel<Account>) () -> usersService.getLoggedInUser();
        if ((!pageParameters.get("user_id").isEmpty()) && accountRepository.findById(pageParameters.get("user_id").to(Long.class)).isPresent()) {
            receiverModel = (IModel<Account>) () -> accountRepository.findById(pageParameters.get("user_id").to(Long.class)).get();
        } else {
            receiverModel = (IModel<Account>) () -> null;
        }

        boolean dialogIsOpen = receiverModel.getObject() != null;

        Link<String> back = new Link<String>("back") {
            @Override
            public void onClick() {
                setResponsePage(MessagesPage.class);
            }
        };
        back.setOutputMarkupPlaceholderTag(true);
        back.setVisible(dialogIsOpen);
        add(back);

        WebMarkupContainer messagesContainer = new WebMarkupContainer("messagesContainer");
        messagesContainer.setOutputMarkupPlaceholderTag(true);
        messagesContainer.setVisible(dialogIsOpen);
        add(messagesContainer);

        RefreshingView<Message> messagesView = new RefreshingView<Message>("messages") {

            private IModel<Optional<Dialog>> optionalDialog = () -> Optional.empty();

            @Override

            protected Iterator<IModel<Message>> getItemModels() {
               optionalDialog = (IModel<Optional<Dialog>>) () -> (dialogRepository.findByUsers(currentUserModel.getObject(), receiverModel.getObject()));
                if (!optionalDialog.getObject().isPresent()) {
                    Dialog dialog = new Dialog();
                    dialog.setCreationDate(new Date());
                    dialog.setFirstUser(currentUserModel.getObject());
                    dialog.setSecondUser(receiverModel.getObject());
                    dialogRepository.save(dialog);
                    dialogModel = (IModel<Dialog>) () -> dialog;
                    return messageRepository.findByDialog(dialog).stream().map(message -> (IModel<Message>) () -> message).iterator();
                }
                dialogModel = (IModel<Dialog>) () -> optionalDialog.getObject().get();
                return messageRepository.findByDialog(optionalDialog.getObject().get()).stream().map(message -> (IModel<Message>) () -> message).iterator();
            }

            @Override
            protected void populateItem(Item<Message> item) {
                DynamicImageResource imageResource = new DynamicImageResource() {
                    @Override
                    protected byte[] getImageData(IResource.Attributes attributes) {
                        return item.getModelObject().getAuthor().getAvatar();
                    }
                };

                Image avatar;
                if (item.getModelObject().getAuthor().getAvatar() == null) {
                    avatar = new Image("avatar", "");
                    avatar.add(new AttributeModifier("src", URL));
                } else {
                    avatar = new Image("avatar", imageResource);
                }

                item.add(avatar);
                item.add(new Label("author", item.getModelObject().getAuthor().getFullName()));
                item.add(new Label("message", item.getModelObject().getText()));
                item.add(new Label("date", item.getModelObject().getCreationDate()));
            }

            @Override
            protected void onDetach() {
                super.onDetach();
                dialogModel.detach();
            }
        };
        messagesContainer.add(messagesView);

        Form<Void> form = new Form<>("form");
        messagesContainer.add(form);

        TextArea<String> messageArea = new TextArea<>("message", messageModel);
        messageArea.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {}
        });
        form.add(messageArea);
        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Message message = messageObjModel.getObject();
                message.setAuthor(currentUserModel.getObject());
                message.setCreationDate(new Date());
                message.setText(messageModel.getObject());
                message.setDialog(dialogModel.getObject());
                messageRepository.save(message);
                target.add(messagesContainer);
            }
        });

        WebMarkupContainer dialogsContainer = new WebMarkupContainer("dialogsContainer");
        dialogsContainer.setOutputMarkupId(true);
        dialogsContainer.setVisible(!dialogIsOpen);
        add(dialogsContainer);


        RefreshingView<Dialog> dialogsView = new RefreshingView<Dialog>("dialogs") {
            @Override
            protected Iterator<IModel<Dialog>> getItemModels() {
                return dialogRepository.findByUser(currentUserModel.getObject()).stream().map(dialog -> (IModel<Dialog>) () -> dialog).iterator();
            }

            @Override
            protected void populateItem(Item<Dialog> item) {
                item.add(new Label("username",
                        item.getModelObject().getFirstUser().equals(currentUserModel.getObject()) ?
                                item.getModelObject().getSecondUser().getFullName() :
                                item.getModelObject().getFirstUser().getFullName()));
                item.add(new Label("lastMessage", messageRepository.findFirstByDialogOrderByCreationDateDesc(item.getModelObject()) == null ?
                        "" :
                        messageRepository.findFirstByDialogOrderByCreationDateDesc(item.getModelObject()).getText()));
                item.add(new Link<Void>("write") {
                    @Override
                    public void onClick() {
                        setResponsePage(MessagesPage.class,
                                new PageParameters().add("user_id", item.getModelObject().getFirstUser().equals(currentUserModel.getObject()) ?
                                        item.getModelObject().getSecondUser().getId() :
                                        item.getModelObject().getFirstUser().getId()));
                    }
                });
            }
        };
        dialogsContainer.add(dialogsView);
    }

    @Override
    public void detachModels() {
        super.detachModels();
        currentUserModel.detach();
        receiverModel.detach();
        messageModel.detach();
        dialogModel.detach();
        messageObjModel.detach();
    }
}
