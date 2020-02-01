package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
	private long id;
	private String name;
	private TaskSide begin, end;
	private List<Task> tasks = new ArrayList<>();

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
		this.tasks = tasks;
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

	@Override
	public String toString() {
		return "Task [id=" + id + ", begin=" + begin.toString() + ", end=" + end.toString() + "]";
	}

}
