package model;

public class Project extends Task {

	public boolean isValid() {

		if (getTasks() == null || getTasks().isEmpty()) {
			return false;
		}

		return validateTask(this, null);
	}

	private boolean validateTask(Task currentTask, Task parent) {
		if (currentTask.getBegin() == null || currentTask.getEnd() == null) {
			return false;
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

		if (currentTask.getTasks() != null && !currentTask.getTasks().isEmpty()) {
			for (Task t : currentTask.getTasks()) {
				if (!validateTask(t, currentTask)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return "Project [getId()=" + getId() + ", getTasks()=" + getTasks() + "]";
	}

}
