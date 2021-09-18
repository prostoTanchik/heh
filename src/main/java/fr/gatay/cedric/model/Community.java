package fr.gatay.cedric.model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Community extends AbstractPersistable<Long> {

    private String name;

    private Date creationDate;

    @OneToMany(mappedBy = "community")
    private List<Usergroup> members;

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

    public List<Usergroup> getMembers() {
        return members;
    }

    public void setMembers(List<Usergroup> members) {
        this.members = members;
    }
}
