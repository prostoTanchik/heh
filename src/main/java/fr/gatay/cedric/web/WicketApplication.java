package fr.gatay.cedric.web;

import org.apache.wicket.bean.validation.BeanValidationConfiguration;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.URI;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication implements ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(WicketApplication.class);

    private ApplicationContext applicationContext;

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<NewsPage> getHomePage() {
        return NewsPage.class;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        mount(new MountedMapper("/profile", ProfilePage.class));
        mount(new MountedMapper("/messages", MessagesPage.class));
        mount(new MountedMapper("/friends", FriendsPage.class));
        mount(new MountedMapper("/photos", PhotosPage.class));
        mount(new MountedMapper("/registration", RegistrationPage.class));
        mount(new MountedMapper("/search", SearchPage.class));
        mount(new MountedMapper("/settings", SettingsPage.class));
        mount(new MountedMapper("/edit", EditPostPage.class));

        try {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            System.setProperty("hibernate.connection.url", dbUrl);
            System.setProperty("hibernate.connection.user", username);
            System.setProperty("hibernate.connection.password", password);
            LOG.info("JDBC URL : {}", System.getProperty("hibernate.connection.url"));
            LOG.info("JDBC User : {}", System.getProperty("hibernate.connection.user"));
            LOG.info("JDBC Password : {}", System.getProperty("hibernate.connection.password"));
        } catch (Exception e) {
            LOG.error("Unable to extract database url");
        }

        getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext, true));
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

        new BeanValidationConfiguration().configure(this);
    }

}
