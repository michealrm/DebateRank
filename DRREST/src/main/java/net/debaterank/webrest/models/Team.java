package net.debaterank.webrest.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne
	@JoinColumn
	private Debater one;
	@ManyToOne
	@JoinColumn
	private Debater two;

	public Team() {}

	public Team(Debater one, Debater two) {
		this.one = one;
		this.two = two;
	}

	public boolean equalsByLastName(Team team) {
		return (one.getLast().equals(team.getOne().getLast()) && two.getLast().equals(team.getTwo().getLast())) || (two.getLast().equals(team.getOne().getLast()) && one.getLast().equals(team.getTwo().getLast()));
	}

	public boolean equals(Team team) {
		return Objects.equals(one, team.getOne()) && Objects.equals(two, team.getTwo());
	}

	public Debater getOne() {
		return one;
	}

	public void setOne(Debater one) {
		this.one = one;
	}

	public Debater getTwo() {
		return two;
	}

	public void setTwo(Debater two) {
		this.two = two;
	}

}
