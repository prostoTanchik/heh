package fr.gatay.cedric.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Dialog extends AbstractPersistable<Long> {

    private String name;

    private Date creationDate;

    @OneToMany(mappedBy = "dialog")
    private List<Message> messages;

    @ManyToOne
    private Account firstUser;

    @ManyToOne
    private Account secondUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> userdialogs) {
        this.messages = userdialogs;
    }

    public Account getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(Account firstUser) {
        this.firstUser = firstUser;
    }

    public Account getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(Account secondUser) {
        this.secondUser = secondUser;
    }
}
