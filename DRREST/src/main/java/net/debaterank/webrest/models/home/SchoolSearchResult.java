package net.debaterank.webrest.models.home;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SchoolSearchResult {
    @Id
    public long id;
    public String schoolName;
}
