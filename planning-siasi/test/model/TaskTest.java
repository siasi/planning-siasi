package model;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class TaskTest {

	@Test
	public void whenDatesFitThoseOfTheProject_shouldBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		task.setEnd(LocalDate.parse("2020-06-20"));

		project.addTask(task);
		Assert.assertTrue(project.isValid());
	}

	@Test
	public void whenBeginBeforeTheProject_shouldNotBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-11"));
		task.setEnd(LocalDate.parse("2020-06-20"));

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenCompleteAfterTheProject_shouldNotBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		task.setEnd(LocalDate.parse("2020-06-21"));

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenDatesAreNotSet_shouldNotBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenTheNestedTaskBeginBefore_shouldNotBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		task.setEnd(LocalDate.parse("2020-06-21"));

		Task nestedTask = new Task();
		nestedTask.setBegin(LocalDate.parse("2020-06-11"));
		nestedTask.setEnd(LocalDate.parse("2020-06-21"));
		task.addTask(nestedTask);

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenTheNestedTaskCompleteAfter_shouldNotBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		task.setEnd(LocalDate.parse("2020-06-21"));

		Task nestedTask = new Task();
		nestedTask.setBegin(LocalDate.parse("2020-06-12"));
		nestedTask.setEnd(LocalDate.parse("2020-06-22"));
		task.addTask(nestedTask);

		Assert.assertFalse(project.isValid());
	}

	@Test
	public void whenOneNestedTaskBeginAfter_shouldNotBeAValidTask() {
		Project project = new Project();
		project.setBegin(LocalDate.parse("2020-06-12"));
		project.setEnd(LocalDate.parse("2020-06-20"));

		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		task.setEnd(LocalDate.parse("2020-06-20"));

		Task nestedTaskA = new Task();
		nestedTaskA.setBegin(LocalDate.parse("2020-06-12"));
		nestedTaskA.setEnd(LocalDate.parse("2020-06-14"));
		task.addTask(nestedTaskA);

		Task nestedTaskB = new Task();
		nestedTaskB.setBegin(LocalDate.parse("2020-06-11"));
		nestedTaskB.setEnd(LocalDate.parse("2020-06-21"));
		task.addTask(nestedTaskB);

		Task nestedTaskC = new Task();
		nestedTaskC.setBegin(LocalDate.parse("2020-06-13"));
		nestedTaskC.setEnd(LocalDate.parse("2020-06-18"));
		task.addTask(nestedTaskC);

		Assert.assertFalse(project.isValid());
	}

}
