package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
	private long id;
	private String name;
	private LocalDate begin, end;
	private List<Task> tasks;

	@JsonIgnore
	private Task parent;
	private List<Constraint> outgoingConstraints = new ArrayList<>();
	private List<Constraint> ingoingConstraints = new ArrayList<>();

	public Task(String name, LocalDate begin, LocalDate end) {
		super();

		if (begin == null) {
			throw new IllegalArgumentException("Task cannot have null begin date");
		}

		if (end == null) {
			throw new IllegalArgumentException("Task cannot have null end date");
		}

		if (begin.isAfter(end)) {
			throw new IllegalArgumentException(
					"Task begin ( +" + begin + "+ ) should be BEFORE task end ( +" + end + "+ )");
		}

		if (name == null) {
			name = "";
		}

		this.name = name;
		this.begin = begin;
		this.end = end;
		this.tasks = new ArrayList<>();
	}

	@JsonCreator
	public Task(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("begin") LocalDate begin,
			@JsonProperty("end") LocalDate end, @JsonProperty("tasks") List<Task> tasks) {
		this(name, begin, end);
		this.id = id;
		if (tasks != null) {
			this.tasks = tasks;
		}
	}

	void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void addTask(Task t) {
		tasks.add(t);
	}

	public boolean hasTasks() {
		return tasks != null && !tasks.isEmpty();
	}

	public LocalDate getDate(SideType side) {
		if (SideType.BEGIN.equals(side)) {
			return begin;
		}
		return end;
	}

	public Task getParent() {
		return this.parent;
	}

	public void setParent(Task parent) {
		this.parent = parent;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public void addIngoingConstraint(Constraint c) {
		this.ingoingConstraints.add(c);
	}

	private void removeIngoingConstraint(Constraint c) {
		this.ingoingConstraints.remove(c);
	}

	public void addOutgoingConstraint(Constraint c) {
		this.outgoingConstraints.add(c);
	}

	private void removeOutgoingConstraint(Constraint c) {
		this.outgoingConstraints.remove(c);
	}

	List<Constraint> getOutgoingConstraints() {
		return outgoingConstraints;
	}

	List<Constraint> getIngoingConstraints() {
		return ingoingConstraints;
	}

	public List<Constraint> updateEnd(LocalDate newEnd) {
		List<Constraint> invalidConstraints = new ArrayList<>();
		updateEnd(this, newEnd, invalidConstraints);
		return invalidConstraints;
	}

	private Task updateEnd(Task task, LocalDate newEnd, List<Constraint> invalidConstraints) {
		if (newEnd.isBefore(task.getEnd())) {
			task.setEnd(newEnd);
			if (newEnd.isBefore(task.getBegin())) {
				updateBegin(this, newEnd, invalidConstraints);
			}
			removeInvalidIngoingConstraints(invalidConstraints);
			for (Task c : task.getTasks()) {
				if (newEnd.isBefore(c.getEnd())) {
					updateEnd(c, newEnd, invalidConstraints);
				}
			}
			return this;
		} else if (newEnd.isAfter(task.getEnd())) {
			task.setEnd(newEnd);
			removeInvalidOutgoingConstraints(invalidConstraints);
			if (task.hasParent() && newEnd.isAfter(task.getParent().getEnd())) {
				return updateEnd(task.getParent(), newEnd, invalidConstraints);
			}
			return task;
		} else {
			return this;
		}
	}

	private void removeInvalidOutgoingConstraints(List<Constraint> invalidConstraints) {
		for (Constraint c : outgoingConstraints) {
			if (!c.isValid()) {
				invalidConstraints.add(c);
			}
		}
		// Remove from this task
		outgoingConstraints.removeAll(invalidConstraints);
		// Remove from the other task
		for (Constraint c : invalidConstraints) {
			c.getTo().getTask().removeIngoingConstraint(c);
		}
	}

	private void removeInvalidIngoingConstraints(List<Constraint> invalidConstraints) {
		for (Constraint c : ingoingConstraints) {
			if (!c.isValid()) {
				invalidConstraints.add(c);
			}
		}
		// Remove from this task
		ingoingConstraints.removeAll(invalidConstraints);
		// Remove from the other task
		for (Constraint c : invalidConstraints) {
			c.getFrom().getTask().removeOutgoingConstraint(c);
		}
	}

	private void setEnd(LocalDate newEnd) {
		this.end = newEnd;
	}

	public List<Constraint> updateBegin(LocalDate newBegin) {
		ArrayList<Constraint> invalidConstraints = new ArrayList<>();
		updateBegin(this, newBegin, invalidConstraints);
		return invalidConstraints;
	}

	private Task updateBegin(Task task, LocalDate newDate, List<Constraint> invalidConstraints) {
		if (newDate.isBefore(task.getBegin())) {
			task.setBegin(newDate);
			removeInvalidIngoingConstraints(invalidConstraints);
			if (task.hasParent() && newDate.isBefore(task.getParent().getBegin())) {
				return updateBegin(task.getParent(), newDate, invalidConstraints);
			}
			return task;
		} else if (newDate.isAfter(task.getBegin())) {
			task.setBegin(newDate);
			if (newDate.isAfter(task.getEnd())) {
				updateEnd(task, newDate, invalidConstraints);
			}
			removeInvalidOutgoingConstraints(invalidConstraints);
			for (Task c : task.getTasks()) {
				if (newDate.isAfter(c.getBegin())) {
					updateBegin(c, newDate, invalidConstraints);
				}
			}
			return this;
		} else {
			return this;
		}
	}

	private void setBegin(LocalDate newDate) {
		this.begin = newDate;
	}

	public boolean endsBefore(LocalDate otherDate) {
		return getEnd().isBefore(otherDate);
	}

	public boolean beginsBefore(LocalDate otherDate) {
		return getBegin().isBefore(otherDate);
	}

	public boolean endsAfter(Task other) {
		return getEnd().isAfter(other.getEnd());
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", begin=" + begin.toString() + ", end=" + end.toString() + "]";
	}

}
