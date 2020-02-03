package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConstraintSide {

	private long taskId;
	private SideType side;

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

}
