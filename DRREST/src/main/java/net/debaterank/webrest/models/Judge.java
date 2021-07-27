package net.debaterank.webrest.models;

import javax.persistence.*;

@Entity
@Table
/**
 * Note: Cache is read-only, so this application shouldn't make any updates to the object. This is expected because
 * this server should function as an unmanaged, single instance scraper only. Note: if this application is changed
 * to be distributed then cache usage should either be shared across the nodes OR turned off entirely
 * Updates to this object should be make in a separate application (with moderation). The cache should be reset
 * or this server can be restarted after changes are pushed
 */
public class Judge {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String first, middle, last, suffix;
	@ManyToOne
	@JoinColumn
	private School school;

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
