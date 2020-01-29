package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
	private long id;
	private String name;
	private LocalDate begin, end;
	private List<Task> tasks;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBegin() {
		return begin;
	}

	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void addTask(Task task) {
		if (tasks == null) {
			tasks = new ArrayList<>();
		}
		this.tasks.add(task);
	}

	@Override
	public String toString() {
		return "Task [id=" + id + "]";
	}

}
