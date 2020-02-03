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

	public void setFromTask(Task t) {
		from.setTask(t);
	}

	public void setToTask(Task t) {
		to.setTask(t);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Constraint other = (Constraint) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Constraint [from=" + from + ", to=" + to + "]";
	}

}
