package fr.gatay.cedric.web;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.gatay.cedric.model.Post;
import fr.gatay.cedric.repository.PostRepository;

public class EditPostPage extends SuperPage {

    @SpringBean
    private PostRepository postRepository;

    private IModel<String> postModel = Model.of("");

    public EditPostPage(PageParameters parameters) {
        super();

        long postId = parameters.get("post_id").toLong();
        Post post = postRepository.findById(postId).get();
        postModel.setObject(post.getText());

        Form<Void> form = new Form<>("form");
        add(form);

        TextArea<String> textArea = new TextArea<>("textArea", postModel);
        form.add(textArea);

        AjaxButton submit = new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                post.setText(postModel.getObject());
                post.setCreationDate(new Date());
                postRepository.save(post);
                setResponsePage(ProfilePage.class);
            }
        };
        form.add(submit);

    }
}
