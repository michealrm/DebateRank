package net.debaterank.webrest.models;

import javax.persistence.*;

@Entity
@Table
public class CXBallot extends DuoBallot<CXRound> {

	public CXBallot() {}
	public CXBallot(CXRound round) {
		super(round);
	}

}
