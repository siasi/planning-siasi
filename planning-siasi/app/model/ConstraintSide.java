package model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConstraintSide {

	private long taskId;
	private SideType side;
	private Task task;

	public ConstraintSide(@JsonProperty("taskId") long taskId, @JsonProperty("side") SideType side) {
		super();
		this.taskId = taskId;
		this.side = side;
	}

	public long getTaskId() {
		return taskId;
	}

	public SideType getSide() {
		return side;
	}

	@Override
	public String toString() {
		return "Constraint [taskId=" + taskId + ", side=" + side + "]";
	}

	public void setTask(Task t) {
		this.task = t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		result = prime * result + (int) (taskId ^ (taskId >>> 32));
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
		ConstraintSide other = (ConstraintSide) obj;
		if (side != other.side)
			return false;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	public Task getTask() {
		return task;
	}

	public LocalDate getDate() {
		return task.getDate(side);
	}

}
