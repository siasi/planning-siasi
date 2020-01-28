package model;

import java.time.LocalTime;

public class Task {
	private long id;
	private String name;
	private LocalTime begin, end;
	
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
	
	public LocalTime getBegin() {
		return begin;
	}
	
	public void setBegin(LocalTime begin) {
		this.begin = begin;
	}
	
	public LocalTime getEnd() {
		return end;
	}
	
	public void setEnd(LocalTime end) {
		this.end = end;
	}
	
}
