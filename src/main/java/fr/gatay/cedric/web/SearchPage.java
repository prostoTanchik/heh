package fr.gatay.cedric.web;

import org.apache.wicket.markup.html.panel.Panel;

public class SearchPage extends SuperPage {

    private Panel panel;

    public SearchPage() {
        super();

        panel = new UsersPanel("panel");
        panel.setOutputMarkupId(true);
        add(panel);
    }
}
