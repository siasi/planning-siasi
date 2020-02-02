package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskSide {
	private LocalDate date;
	private List<ConstraintSide> constraints = new ArrayList<>();

	public TaskSide(LocalDate date) {
		super();
		this.date = date;
	}

	@JsonCreator
	public TaskSide(@JsonProperty("date") LocalDate date,
			@JsonProperty("constraints") List<ConstraintSide> constraints) {
		super();
		this.date = date;
		this.constraints = constraints;
	}

	public void addConstraints(ConstraintSide c) {
		this.constraints.add(c);
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean isBefore(TaskSide other) {
		return date.isBefore(other.date);
	}

	public boolean isAfter(TaskSide other) {
		return date.isAfter(other.date);
	}

	public boolean hasConstraints() {
		return constraints != null && !constraints.isEmpty();
	}

	public List<ConstraintSide> getConstraints() {
		return constraints;
	}

	void setDate(LocalDate newEnd) {
		this.date = newEnd;
	}

	@Override
	public String toString() {
		return "Side [date=" + date + "]";
	}

}
