package model;

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

		if (!hasTasks()) {
			return false;
		}

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

}
