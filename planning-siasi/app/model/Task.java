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
	private List<Task> tasks = new ArrayList<>();

	@JsonIgnore
	private Task parent;

	public Task(String name, LocalDate begin, LocalDate end) {
		super();

		if (begin == null) {
			throw new IllegalArgumentException("Task cannot have null begin date");
		}

		if (end == null) {
			throw new IllegalArgumentException("Task cannot have null end date");
		}

		if (name == null) {
			name = "";
		}

		this.name = name;
		this.begin = begin;
		this.end = end;
	}

	@JsonCreator
	public Task(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("begin") LocalDate begin,
			@JsonProperty("end") LocalDate end, @JsonProperty("tasks") List<Task> tasks) {
		super();
		this.id = id;
		this.name = name;
		this.begin = begin;
		this.end = end;
		if (tasks == null) {
			tasks = new ArrayList<>();
		} else {
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

	public Task updateEnd(LocalDate newEnd) {
		return updateEnd(this, newEnd);
	}

	private Task updateEnd(Task task, LocalDate newEnd) {
		if (newEnd.isBefore(task.getEnd())) {
			anticipateEnd(task, newEnd);
			for (Task c : task.getTasks()) {
				if (newEnd.isBefore(c.getEnd())) {
					updateEnd(c, newEnd);
				}
			}
			return this;
		} else if (newEnd.isAfter(task.getEnd())) {
			task.setEnd(newEnd);
			if (task.hasParent() && newEnd.isAfter(task.getParent().getEnd())) {
				return updateEnd(task.getParent(), newEnd);
			}
			return task;
		} else {
			return this;
		}
	}

	private void setEnd(LocalDate newEnd) {
		this.end = newEnd;
	}

	private void anticipateEnd(Task task, LocalDate newDate) {
		task.setEnd(newDate);
		if (newDate.isBefore(task.getBegin())) {
			task.updateBegin(newDate);
		}
	}

	public Task updateBegin(LocalDate newBegin) {
		return updateBegin(this, newBegin);
	}

	private Task updateBegin(Task task, LocalDate newDate) {
		if (newDate.isBefore(task.getBegin())) {
			task.setBegin(newDate);
			if (task.hasParent() && newDate.isBefore(task.getParent().getBegin())) {
				return updateBegin(task.getParent(), newDate);
			}
			return task;
		} else if (newDate.isAfter(task.getBegin())) {
			posticipateBegin(task, newDate);
			for (Task c : task.getTasks()) {
				if (newDate.isAfter(c.getBegin())) {
					updateBegin(c, newDate);
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

	private void posticipateBegin(Task task, LocalDate newDate) {
		task.setBegin(newDate);
		if (newDate.isAfter(task.getEnd())) {
			task.updateEnd(newDate);
		}
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
