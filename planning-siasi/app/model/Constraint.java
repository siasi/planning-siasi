package model;

public class Constraint {

	private long taskId;
	private SideType side;

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
