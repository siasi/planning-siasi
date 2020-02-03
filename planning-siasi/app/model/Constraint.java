package model;

import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Constraint {
	private ConstraintSide from, to;

	@JsonCreator
	public Constraint(@JsonProperty("from") ConstraintSide from, @JsonProperty("to") ConstraintSide to) {
		super();
		this.from = from;
		this.to = to;
	}

	public ConstraintSide getFrom() {
		return from;
	}

	public ConstraintSide getTo() {
		return to;
	}

	public boolean isValid(Map<Long, Task> taskIdToTask) {
		Task taskFrom = taskIdToTask.get(from.getTaskId());
		Task taskTo = taskIdToTask.get(to.getTaskId());

		LocalDate fromDate = taskFrom.getDate(from.getSide());
		LocalDate toDate = taskTo.getDate(to.getSide());
		return fromDate.isBefore(toDate);
	}
}
