package fr.gatay.cedric.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.City;
import fr.gatay.cedric.model.Language;
import fr.gatay.cedric.model.Town;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.repository.CityRepository;
import fr.gatay.cedric.repository.LanguageRepository;
import fr.gatay.cedric.repository.TownRepository;
import fr.gatay.cedric.service.UsersService;

public class SettingsPage extends SuperPage {

    private static final String URL = "data:image/png;base64,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";

    @SpringBean
    private UsersService usersService;

    @SpringBean
    private CityRepository cityRepository;

    @SpringBean
    private TownRepository townRepository;

    @SpringBean
    private LanguageRepository languageRepository;

    @SpringBean
    private AccountRepository accountRepository;

    private IModel<Account> currentUserModel;

    private City city;
    private Town town;
    private Language language;

    private IModel<City> cityModel = new PropertyModel<>(this, "city");
    private IModel<Town> townModel = new PropertyModel<>(this, "town");
    private IModel<Language> languageModel = new PropertyModel<>(this, "language");

    public SettingsPage(PageParameters parameters) {
        super();

        currentUserModel = usersService::getLoggedInUser;
        city = currentUserModel.getObject().getCity();
        town = currentUserModel.getObject().getTown();
        language = currentUserModel.getObject().getLanguage();

        Form<Void> form = new Form<Void>("form");
        add(form);

        FileUploadField fileUploadField = new FileUploadField("fileUpload");
        fileUploadField.setOutputMarkupId(true);
        form.add(fileUploadField);

        AjaxButton submit = new AjaxButton("savePhoto") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (fileUploadField.getFileUpload() != null) {
                    FileUpload fileUpload = fileUploadField.getFileUpload();
                    Account account = currentUserModel.getObject();
                    account.setAvatar(fileUpload.getBytes());
                    accountRepository.save(account);
                    fileUpload.delete();
                    target.add(fileUploadField);
                }
            }
        };
        form.add(submit);

        DynamicImageResource resource = new DynamicImageResource(){
            @Override
            protected byte[] getImageData(Attributes attributes) {
                return currentUserModel.getObject().getAvatar();
            }
        };

        Image avatar;
        if (currentUserModel.getObject().getAvatar() == null) {
            avatar = new Image("avatar", "");
            avatar.add(new AttributeModifier("src", URL));
        } else {
            avatar = new Image("avatar", resource);
        }
        form.add(avatar);

        DropDownChoice<City> city = new DropDownChoice<>("city", cityModel, cityRepository.findAll(), new ChoiceRenderer<>("name"));
        form.add(city);

        DropDownChoice<Town> town = new DropDownChoice<>("town", townModel, townRepository.findAll(), new ChoiceRenderer<>("name"));
        form.add(town);

        DropDownChoice<Language> language = new DropDownChoice<>("language", languageModel, languageRepository.findAll(), new ChoiceRenderer<>("language"));
        form.add(language);

        AjaxButton saveButton = new AjaxButton("save") {
            protected void onSubmit(AjaxRequestTarget target) {
                Account account = currentUserModel.getObject();
                account.setLanguage(languageModel.getObject());
                account.setCity(cityModel.getObject());
                account.setTown(townModel.getObject());
                accountRepository.save(account);
            }
        };
        form.add(saveButton);

    }

    @Override
    public void detachModels() {
        super.detachModels();
        currentUserModel.detach();
        cityModel.detach();
        townModel.detach();
        languageModel.detach();
    }

    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
