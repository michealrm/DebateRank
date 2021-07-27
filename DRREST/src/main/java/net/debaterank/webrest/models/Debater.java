package net.debaterank.webrest.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Table
/**
 * Note: Cache is read-only, so this application shouldn't make any updates to the object. This is expected because
 * this server should function as an unmanaged, single instance scraper only. Note: if this application is changed
 * to be distributed then cache usage should either be shared across the nodes OR turned off entirely
 * Updates to this object should be make in a separate application (with moderation). The cache should be reset
 * or this server can be restarted after changes are pushed
 */
@NamedQueries({
		@NamedQuery(
				name = "getDebaterFromLastName",
				query = "from Debater where last = :n and school = :s"
		),
})
public class Debater implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String first;
	private String middle;
	private String last;
	private String suffix;
	@ManyToOne
	@JoinColumn
	private School school;
	@OneToOne
	@JoinColumn
	private Debater pointsTo;

	public Long getId() {
		return id;
	}

	public String getFirst() {
		return first;
	}

	public String getMiddle() {
		return middle;
	}

	public String getLast() {
		return last;
	}

	public String getSuffix() {
		return suffix;
	}

	public School getSchool() {
		return school;
	}

	public Debater getPointsTo() {
		return pointsTo;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(first != null)
			sb.append(first + " ");
		if(middle != null)
			sb.append(middle + " ");
		if(last != null)
			sb.append(last + " ");
		if(suffix != null)
			sb.append(suffix);

		return sb.toString().trim();
	}

}
