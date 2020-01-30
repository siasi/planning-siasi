package model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Project extends Task {

	public Project(String name, Side begin, Side end) {
		super(name, begin, end);
	}

	@JsonCreator
	public Project(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("begin") Side begin,
			@JsonProperty("end") Side end, @JsonProperty("tasks") List<Task> tasks) {
		super(id, name, begin, end, tasks);
	}

	public boolean isValid() {

		if (!hasTasks()) {
			return false;
		}

		return validateTask(this, null);
	}

	private boolean validateTask(Task currentTask, Task parent) {
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

		if (currentTask.hasTasks()) {
			for (Task t : currentTask.getTasks()) {
				if (!validateTask(t, currentTask)) {
					return false;
				}
			}
		}

		return true;
	}

}
