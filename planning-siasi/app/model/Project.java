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

		// if (!hasTasks()) {
		// return false;
		// }

		// Validate the task dates and fill the cache as side effect
		if (!validateTaskDuration(this, null)) {
			return false;
		}

		// Validate the constraints
		return validateConstraints();
		// return true;
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
		if (currentTask.getEnd().isBefore(currentTask.getBegin())) {
			return false;
		}

		if (parent != null) {
			if (currentTask.getBegin().isBefore(parent.getBegin())) {
				return false;
			}

			if (currentTask.getEnd().isAfter(parent.getEnd())) {
				return false;
			}
		}

		buildConstraints(currentTask, currentTask.getBegin(), SideType.BEGIN);
		buildConstraints(currentTask, currentTask.getEnd(), SideType.END);

		if (currentTask.hasTasks()) {
			for (Task t : currentTask.getTasks()) {
				if (!validateTaskDuration(t, currentTask)) {
					return false;
				}
			}
		}

		return true;
	}

	private void buildConstraints(Task currentTask, TaskSide side, SideType sideType) {
		side.getConstraints()
				.forEach(c -> constraints.add(new Constraint(new ConstraintSide(currentTask.getId(), sideType), c)));
	}

	public Task updateTaskEnd(long taskId, LocalDate newDate) {
		return updateTaskEnd(getTask(taskId), newDate);
	}

	private Task updateTaskEnd(Task task, LocalDate newDate) {
		if (newDate.isBefore(task.getEnd().getDate())) {
			anticipateTaskEnd(task, newDate);
			for (Task c : task.getTasks()) {
				if (newDate.isBefore(c.getEnd().getDate())) {
					anticipateTaskEnd(c, newDate);
				}
			}
			return this;
		} else if (newDate.isAfter(task.getEnd().getDate())) {
			task.getEnd().setDate(newDate);
			if (task.hasParent() && newDate.isAfter(task.getParent().getEnd().getDate())) {
				return updateTaskEnd(task.getParent(), newDate);
			}
			return task;
		} else {
			return this;
		}
	}

	private void anticipateTaskEnd(Task task, LocalDate newDate) {
		if (newDate.isBefore(task.getBegin().getDate())) {
			task.getEnd().setDate(task.getBegin().getDate()); // TODO collapse()
		} else {
			task.getEnd().setDate(newDate);
		}
	}

	public Task updateTaskBegin(long taskId, LocalDate newDate) {
		return updateTaskBegin(getTask(taskId), newDate);
	}

	private Task updateTaskBegin(Task task, LocalDate newDate) {
		task.getBegin().setDate(newDate);
		if (task.hasParent() && newDate.isBefore(task.getParent().getBegin().getDate())) {
			return updateTaskBegin(task.getParent(), newDate);
		}

		return task;
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
