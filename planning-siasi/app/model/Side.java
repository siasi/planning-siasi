package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Side {
	private LocalDate date;
	private List<Constraint> constraints = new ArrayList<>();

	public Side(LocalDate date) {
		super();
		this.date = date;
	}

	@JsonCreator
	public Side(@JsonProperty("date") LocalDate date, @JsonProperty("constraints") List<Constraint> constraints) {
		super();
		this.date = date;
		this.constraints = constraints;
	}

	public void addConstraints(Constraint c) {
		this.constraints.add(c);
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean isBefore(Side other) {
		return date.isBefore(other.date);
	}

	public boolean isAfter(Side other) {
		return date.isAfter(other.date);
	}

	public boolean hasConstraints() {
		return constraints != null && !constraints.isEmpty();
	}

	@Override
	public String toString() {
		return "Side [date=" + date + "]";
	}

}
