package model;

import java.time.LocalDate;
import java.util.Map;

public class Constraint {
	private ConstraintSide from, to;

	public Constraint(ConstraintSide from, ConstraintSide to) {
		super();
		this.from = from;
		this.to = to;
	}

	public boolean isValid(Map<Long, Task> taskIdToTask) {
		Task taskFrom = taskIdToTask.get(from.getTaskId());
		Task taskTo = taskIdToTask.get(to.getTaskId());

		LocalDate fromDate = taskFrom.getDate(from.getSide());
		LocalDate toDate = taskTo.getDate(to.getSide());
		return fromDate.isBefore(toDate);
	}

}
