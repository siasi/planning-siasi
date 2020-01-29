package model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ProjectTest {

	@Test
	public void emptyProjectShouldNotBeValid() {
		Project project = new Project();
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void projectWithNoDateSet_shouldNotBeValid() {
		Project project = new Project();
		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		task.setEnd(LocalDate.parse("2020-06-20"));
		project.addTask(task);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void projectWithOneTaskAndDateSet_shouldBeValid() {
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
	public void projectWithOneTaskAndNoDateNotSet_shouldNotBeValid() {
		Project project = new Project();
		project.addTask(new Task());
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void projectWithOneTaskAndBeginNotSet_shouldNotBeValid() {
		Project project = new Project();
		Task task = new Task();
		task.setEnd(LocalDate.parse("2020-06-20"));
		project.addTask(task);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void projectWithOneTaskAndEndNotSet_shouldNotBeValid() {
		Project project = new Project();
		Task task = new Task();
		task.setBegin(LocalDate.parse("2020-06-12"));
		project.addTask(task);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void shouldDetectAValidModel() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/simpleProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());
	}

	@Test
	public void shouldDetectAnInvalidModel() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/invalidProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertFalse(project.isValid());
	}

}
