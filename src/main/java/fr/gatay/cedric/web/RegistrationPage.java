package fr.gatay.cedric.web;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Usergroup;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.repository.UsergroupRepository;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RegistrationPage extends WebPage {

    private IModel<String> firstNameModel = Model.of("");
    private IModel<String> secondNameModel = Model.of("");
    private IModel<String> patronymicModel = Model.of("");
    private IModel<String> emailModel = Model.of("");
    private IModel<String> passwordModel = Model.of("");
    private IModel<String> confirmPasswordModel = Model.of("");

    @SpringBean
    private AccountRepository accountRepository;

    @SpringBean
    private UsergroupRepository usergroupRepository;

    public RegistrationPage() {
        super();

        Form<Void> form = new Form<>("form");
        add(form);

        TextField<String> firstNameField = new TextField<>("firstName", firstNameModel);
        firstNameField.setRequired(true);
        form.add(firstNameField);

        TextField<String> secondNameField = new TextField<>("secondName", secondNameModel);
        secondNameField.setRequired(true);
        form.add(secondNameField);

        TextField<String> patronymicField = new TextField<>("patronymic", patronymicModel);
        form.add(patronymicField);

        TextField<String> emailField = new EmailTextField("email", emailModel);
        emailField.setRequired(true);
        form.add(emailField);

        PasswordTextField passwordField = new PasswordTextField("password", passwordModel);
        form.add(passwordField);

        PasswordTextField confirmPasswordField = new PasswordTextField("confirmPassword", confirmPasswordModel);
        form.add(confirmPasswordField);

        form.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        Button submitButton = new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                if (accountRepository.existsByEmail(emailModel.getObject())) {
                    error("Аккаунт с такой почтой уже существует!");
                    return;
                }
                Account account = new Account();
                account.setFirstName(firstNameModel.getObject());
                account.setSecondName(secondNameModel.getObject());
                account.setPatronymic(patronymicModel.getObject());
                account.setEmail(emailModel.getObject());
                account.setPassword(passwordModel.getObject());
                accountRepository.save(account);
                Usergroup usergroup = new Usergroup();
                usergroup.setAccount(account);
                usergroupRepository.save(usergroup);
                System.out.println("Account with name" + account.getFirstName() + "was saved");
                setResponsePage(new RedirectPage("/authorization.html"));
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                super.onError(target);
                target.add(feedback);
            }
        };
        form.add(submitButton);

    }

    @Override
    protected void detachModel() {
        super.detachModel();
        firstNameModel.detach();
        secondNameModel.detach();
        patronymicModel.detach();
        emailModel.detach();
        passwordModel.detach();
        confirmPasswordModel.detach();
    }
}
