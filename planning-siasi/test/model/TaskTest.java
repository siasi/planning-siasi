package model;

import static java.time.LocalDate.parse;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class TaskTest {

	@Test
	public void whenDatesFitThoseOfTheProject_shouldBeAValidTask() {
		Project project = new Project("project", parse("2020-06-12"), parse("2020-06-20"));
		Task task = new Task("task", parse("2020-06-13"), parse("2020-06-19"));
		project.addTask(task);
		Assert.assertTrue(project.isValid());
	}

	@Test
	public void whenBeginBeforeTheProject_shouldNotBeAValidTask() {
		Project project = new Project("project", parse("2020-06-12"), parse("2020-06-20"));

		Task task = new Task("task", parse("2020-06-11"), parse("2020-06-20"));
		project.addTask(task);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenCompleteAfterTheProject_shouldNotBeAValidTask() {
		Project project = new Project("project", parse("2020-06-12"), parse("2020-06-20"));
		Task task = new Task("task", parse("2020-06-12"), parse("2020-06-21"));
		project.addTask(task);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenTheNestedTaskBeginBefore_shouldNotBeAValidTask() {
		Project project = new Project("project", parse("2020-06-12"), parse("2020-06-20"));
		Task task = new Task("task", parse("2020-06-12"), parse("2020-06-19"));
		project.addTask(task);

		Task nestedTask = new Task("task", LocalDate.parse("2020-06-11"), LocalDate.parse("2020-06-20"));
		task.addTask(nestedTask);

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenTheNestedTaskCompleteAfter_shouldNotBeAValidTask() {
		Project project = new Project("project", parse("2020-06-12"), parse("2020-06-20"));
		Task task = new Task("task", parse("2020-06-12"), parse("2020-06-21"));
		project.addTask(task);

		Task nestedTask = new Task("task", LocalDate.parse("2020-06-12"), LocalDate.parse("2020-06-22"));
		task.addTask(nestedTask);

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenOneNestedTaskBeginAfter_shouldNotBeAValidTask() {
		Project project = new Project("project", parse("2020-06-12"), parse("2020-06-20"));
		Task task = new Task("task", parse("2020-06-12"), parse("2020-06-21"));
		project.addTask(task);

		Task nestedTaskA = new Task("task", LocalDate.parse("2020-06-12"), LocalDate.parse("2020-06-14"));
		task.addTask(nestedTaskA);

		Task nestedTaskB = new Task("task", LocalDate.parse("2020-06-11"), LocalDate.parse("2020-06-21"));
		task.addTask(nestedTaskB);

		Task nestedTaskC = new Task("task", LocalDate.parse("2020-06-13"), LocalDate.parse("2020-06-18"));
		task.addTask(nestedTaskC);

		Assert.assertFalse(project.isValid());
	}

}
