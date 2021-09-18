package fr.gatay.cedric.web;

import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.gatay.cedric.model.Account;
import fr.gatay.cedric.model.Photo;
import fr.gatay.cedric.model.Usergroup;
import fr.gatay.cedric.repository.AccountRepository;
import fr.gatay.cedric.repository.PhotoRepository;
import fr.gatay.cedric.repository.UsergroupRepository;
import fr.gatay.cedric.service.UsersService;

public class PhotosPage extends SuperPage {

    @SpringBean
    private PhotoRepository photoRepository;

    @SpringBean
    private UsersService usersService;

    @SpringBean
    private AccountRepository accountRepository;

    @SpringBean
    private UsergroupRepository usergroupRepository;

    private IModel<Account> currentUserModel;
    private IModel<Account> maintainerModel;

    public PhotosPage(PageParameters pageParameters) {
        super();

        currentUserModel = (IModel<Account>) () -> usersService.getLoggedInUser();
        if ((!pageParameters.get("user_id").isEmpty()) && accountRepository.findById(pageParameters.get("user_id").to(Long.class)).isPresent()) {
                maintainerModel = (IModel<Account>) () -> accountRepository.findById(pageParameters.get("user_id").to(Long.class)).get();
        } else {
                maintainerModel = (IModel<Account>) () -> currentUserModel.getObject();
        }

        Form<Void> form = new Form<>("form");
        form.setMultiPart(true);
        add(form);

        FileUploadField fileUploadField = new FileUploadField("fileUpload");
        fileUploadField.setOutputMarkupId(true);
        form.add(fileUploadField);

        WebMarkupContainer photosContainer = new WebMarkupContainer("photosContainer");
        photosContainer.setOutputMarkupId(true);
        add(photosContainer);

        AjaxButton submit = new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (fileUploadField.getFileUpload() != null) {
                    FileUpload fileUpload = fileUploadField.getFileUpload();
                    Photo photo = new Photo();
                    photo.setBytes(fileUpload.getBytes());
                    photoRepository.save(photo);
                    Usergroup usergroup = usergroupRepository.findUsergroupByAccount(maintainerModel.getObject()).get(0);
                    Set<Photo> photos = photoRepository.findPhotosByUsergroup(usergroup);
                    photos.add(photo);
                    usergroup.setPhotos(photos);
                    usergroupRepository.save(usergroup);
                    fileUpload.delete();
                    target.add(fileUploadField, photosContainer);
                }

            }

        };
        form.add(submit);

        RefreshingView<Photo> photosView = new RefreshingView<Photo>("photosView") {

            @Override
            protected Iterator<IModel<Photo>> getItemModels() {
                Usergroup usergroup = usergroupRepository.findUsergroupByAccount(maintainerModel.getObject()).get(0);
                return photoRepository.findPhotosByUsergroup(usergroup).stream().map(photo -> (IModel<Photo>) () -> photo).iterator();
                
            }

            @Override
            protected void populateItem(Item<Photo> item) {
                DynamicImageResource imageResource = new DynamicImageResource() {
                    @Override
                    protected byte[] getImageData(IResource.Attributes attributes) {
                        return item.getModelObject().getBytes();
                    }
                };
                item.add(new Image("img", imageResource));
            }

        };
        photosContainer.add(photosView);

    }
}
