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

	private List<Constraint> constraints;

	public Project(String name, LocalDate begin, LocalDate end) {
		super(name, begin, end);
		this.constraints = new ArrayList<>();
	}

	@JsonCreator
	public Project(@JsonProperty("id") long id, @JsonProperty("name") String name,
			@JsonProperty("begin") LocalDate begin, @JsonProperty("end") LocalDate end,
			@JsonProperty("tasks") List<Task> tasks, @JsonProperty("constraints") List<Constraint> constraints) {
		super(id, name, begin, end, tasks);
		this.constraints = constraints;
		if (this.constraints == null) {
			this.constraints = new ArrayList<>();
		}
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public boolean isValid() {

		// Validate the task dates and fill the cache as side effect
		if (!validateTaskDuration(this, null)) {
			return false;
		}

		// Validate the constraints and fill the ingoing/outgoing constraint lists in
		// each task as side effect
		return validateConstraints();
	}

	private boolean validateConstraints() {
		for (Constraint c : constraints) {
			Task from = taskIdToTask.get(c.getFrom().getTaskId());
			Task to = taskIdToTask.get(c.getTo().getTaskId());
			c.setFromTask(from);
			from.addOutgoingConstraint(c);
			c.setToTask(to);
			to.addIngoingConstraint(c);

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

		if (parent != null) {
			if (currentTask.beginsBefore(parent.getBegin())) {
				return false;
			}

			if (currentTask.endsAfter(parent)) {
				return false;
			}
		}

		for (Task t : currentTask.getTasks()) {
			if (!validateTaskDuration(t, currentTask)) {
				return false;
			}
		}

		return true;
	}

	public Task updateTaskEnd(long taskId, LocalDate newEnd) {
		return getTask(taskId).updateEnd(newEnd);
	}

	public Task updateTaskBegin(long taskId, LocalDate newBegin) {
		return getTask(taskId).updateBegin(newBegin);
	}

	Task getTask(long taskId) {
		Task task;
		if (taskId == getId()) {
			task = this;
		} else {
			task = taskIdToTask.get(taskId);
		}
		return task;
	}

}
