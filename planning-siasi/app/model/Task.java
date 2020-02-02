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
	private TaskSide begin, end;
	private List<Task> tasks = new ArrayList<>();

	@JsonIgnore
	private Task parent;

	public Task(String name, TaskSide begin, TaskSide end) {
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
	public Task(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("begin") TaskSide begin,
			@JsonProperty("end") TaskSide end, @JsonProperty("tasks") List<Task> tasks) {
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

	public TaskSide getBegin() {
		return begin;
	}

	public TaskSide getEnd() {
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
			return begin.getDate();
		}
		return end.getDate();
	}

	void setEnd(LocalDate newEnd) {
		this.end.setDate(newEnd);
	};

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
		if (newEnd.isBefore(task.getEnd().getDate())) {
			anticipateEnd(task, newEnd);
			for (Task c : task.getTasks()) {
				if (newEnd.isBefore(c.getEnd().getDate())) {
					updateEnd(c, newEnd);
				}
			}
			return this;
		} else if (newEnd.isAfter(task.getEnd().getDate())) {
			task.getEnd().setDate(newEnd);
			if (task.hasParent() && newEnd.isAfter(task.getParent().getEnd().getDate())) {
				return updateEnd(task.getParent(), newEnd);
			}
			return task;
		} else {
			return this;
		}
	}

	private void anticipateEnd(Task task, LocalDate newDate) {
		task.getEnd().setDate(newDate);
		if (newDate.isBefore(task.getBegin().getDate())) {
			task.getBegin().setDate(newDate);
		}
	}

	public Task updateBegin(LocalDate newBegin) {
		return updateBegin(this, newBegin);
	}

	private Task updateBegin(Task task, LocalDate newDate) {
		if (newDate.isBefore(task.getBegin().getDate())) {
			task.getBegin().setDate(newDate);
			if (task.hasParent() && newDate.isBefore(task.getParent().getBegin().getDate())) {
				return updateBegin(task.getParent(), newDate);
			}
			return task;
		} else if (newDate.isAfter(task.getBegin().getDate())) {
			posticipateBegin(task, newDate);
			for (Task c : task.getTasks()) {
				if (newDate.isAfter(c.getBegin().getDate())) {
					updateBegin(c, newDate);
				}
			}
			return this;
		} else {
			return this;
		}
	}

	private void posticipateBegin(Task task, LocalDate newDate) {
		task.getBegin().setDate(newDate);
		if (newDate.isAfter(task.getEnd().getDate())) {
			task.getEnd().setDate(newDate);
		}
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", begin=" + begin.toString() + ", end=" + end.toString() + "]";
	}

}
