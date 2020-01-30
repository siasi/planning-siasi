package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {
	private long id;
	private String name;
	private Side begin, end;
	private List<Task> tasks = new ArrayList<>();

	public Task(String name, Side begin, Side end) {
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
	public Task(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("begin") Side begin,
			@JsonProperty("end") Side end, @JsonProperty("tasks") List<Task> tasks) {
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

	public Side getBegin() {
		return begin;
	}

	public Side getEnd() {
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

	@Override
	public String toString() {
		return "Task [id=" + id + ", begin=" + begin.toString() + ", end=" + end.toString() + "]";
	}

}
