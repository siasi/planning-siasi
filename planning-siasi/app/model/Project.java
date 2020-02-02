package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Project extends Task {

	private Map<Long, Task> taskIdToTask = new HashMap<>();
	private List<Constraint> constraints = new ArrayList<>();

	public Project(String name, TaskSide begin, TaskSide end) {
		super(name, begin, end);
	}

	@JsonCreator
	public Project(@JsonProperty("id") long id, @JsonProperty("name") String name,
			@JsonProperty("begin") TaskSide begin, @JsonProperty("end") TaskSide end,
			@JsonProperty("tasks") List<Task> tasks) {
		super(id, name, begin, end, tasks);
	}

	public boolean isValid() {

		// Validate the task dates and fill the cache as side effect
		if (!validateTaskDuration(this, null)) {
			return false;
		}

		// Validate the constraints
		return validateConstraints();
	}

	private boolean validateConstraints() {
		for (Constraint c : constraints) {
			if (!c.isValid(taskIdToTask)) {
				return false;
			}
		}
		return true;
	}

	private boolean validateTaskDuration(Task currentTask, Task parent) {

		currentTask.setParent(parent);

		if (parent != null) {
			taskIdToTask.put(currentTask.getId(), currentTask);
		}

		if (currentTask.endsBefore(currentTask.getBegin())) {
			return false;
		}

		if (parent != null) {
			if (currentTask.beginsBefore(parent.getBegin())) {
				return false;
			}

			if (currentTask.endsAfter(parent)) {
				return false;
			}
		}

		buildConstraints(currentTask, currentTask.getBegin(), SideType.BEGIN);
		buildConstraints(currentTask, currentTask.getEnd(), SideType.END);

		for (Task t : currentTask.getTasks()) {
			if (!validateTaskDuration(t, currentTask)) {
				return false;
			}
		}

		return true;
	}

	private void buildConstraints(Task currentTask, TaskSide side, SideType sideType) {
		side.getConstraints()
				.forEach(c -> constraints.add(new Constraint(new ConstraintSide(currentTask.getId(), sideType), c)));
	}

	public Task updateTaskEnd(long taskId, LocalDate newEnd) {
		return getTask(taskId).setEnd(newEnd);
	}

	public Task updateTaskBegin(long taskId, LocalDate newBegin) {
		return getTask(taskId).setBegin(newBegin);
	}

	private Task getTask(long taskId) {
		Task task;
		if (taskId == getId()) {
			task = this;
		} else {
			task = taskIdToTask.get(taskId);
		}
		return task;
	}

}
