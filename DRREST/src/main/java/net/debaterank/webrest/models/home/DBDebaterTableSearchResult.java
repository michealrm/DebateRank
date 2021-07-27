package net.debaterank.webrest.models.home;

import net.debaterank.webrest.models.Debater;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class DBDebaterTableSearchResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    public String first;
    public String last;
    public String schoolName;
    public int ranking;
}
