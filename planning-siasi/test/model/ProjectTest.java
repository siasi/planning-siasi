package model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ProjectTest {

	private ObjectMapper objectMapper;

	private Project parse(String string) throws IOException, JsonParseException, JsonMappingException {
		File file = new File(string);
		Project project = objectMapper.readValue(file, Project.class);
		return project;
	}

	@Before
	public void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	public void shouldDetectAValidModel() throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/simpleProject.json");
		Assert.assertTrue(project.isValid());
	}

	@Test
	public void shouldDetectAnInvalidModel_BecauseOfDates()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/invalidProject.json");
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void shouldDetectAnInvalidModel_BecauseOfConstraints()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/invalidProject_bacause_of_constraints.json");
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void shouldPosticipateTaskEnd_SingleTask() throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(0, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskEnd_NestedTask_ParentModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(100, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getTasks().get(0).getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getEnd().getDate(), LocalDate.parse("2020-06-21"));
	}

	@Test
	public void shouldPosticipateTaskEnd_NestedTask_Parent_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(100, LocalDate.parse("2020-06-19"));
		Assert.assertEquals(LocalDate.parse("2020-06-19"), project.getTasks().get(0).getEnd().getDate());
		Assert.assertEquals(project.getTasks().get(0), highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getEnd().getDate(), LocalDate.parse("2020-06-19"));
		Assert.assertEquals(project.getEnd().getDate(), LocalDate.parse("2020-06-20"));
	}

	@Test
	public void shouldAnticipateTaskEnd_SingleTask_AnticipateAfterTheBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(0, LocalDate.parse("2020-06-18"));
		Assert.assertEquals(LocalDate.parse("2020-06-18"), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldAnticipateTaskEnd_SingleTask_AnticipateAtTheBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(0, LocalDate.parse("2020-06-12"));
		Assert.assertEquals(LocalDate.parse("2020-06-12"), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldAnticipateTaskEnd_SingleTask_AnticipateBeforeTheBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(0, LocalDate.parse("2020-06-11"));
		Assert.assertEquals(LocalDate.parse("2020-06-11"), project.getEnd().getDate());
		Assert.assertEquals(project.getEnd().getDate(), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldAnticipateTaskEnd_NestedTask_ChildrenModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(10, LocalDate.parse("2020-06-17"));
		Assert.assertEquals(LocalDate.parse("2020-06-17"), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(LocalDate.parse("2020-06-17"), project.getTasks().get(0).getEnd().getDate());
	}

	@Test
	public void shouldAnticipateTaskEnd_NestedTask_Children_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(10, LocalDate.parse("2020-06-19"));
		Assert.assertEquals(LocalDate.parse("2020-06-19"), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(LocalDate.parse("2020-06-18"), project.getTasks().get(0).getEnd().getDate());
	}

	@Test
	public void shouldAnticipateTaskBegin_SingleTask() throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(0, LocalDate.parse("2020-06-10"));
		Assert.assertEquals(LocalDate.parse("2020-06-10"), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldAnticipateTaskBegin_NestedTask_ParentModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(100, LocalDate.parse("2020-06-10"));
		Assert.assertEquals(LocalDate.parse("2020-06-10"), project.getTasks().get(0).getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getBegin().getDate(), LocalDate.parse("2020-06-10"));
	}

	@Test
	public void shouldAnticipateTaskBegin_NestedTask_Parent_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(100, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(LocalDate.parse("2020-06-13"), project.getTasks().get(0).getBegin().getDate());
		Assert.assertEquals(project.getTasks().get(0), highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getBegin().getDate(), LocalDate.parse("2020-06-13"));
		Assert.assertEquals(project.getBegin().getDate(), LocalDate.parse("2020-06-12"));
	}

	/// TODO

	@Test
	public void shouldPosticipateTaskBegin_SingleTask_PosticipateBeforeTheEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(0, LocalDate.parse("2020-06-14"));
		Assert.assertEquals(LocalDate.parse("2020-06-14"), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskBegin_SingleTask_PosticipateAtTheEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(0, LocalDate.parse("2020-06-20"));
		Assert.assertEquals(LocalDate.parse("2020-06-20"), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskBegin_SingleTask_PosticipateAfterTheEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(0, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getBegin().getDate());
		Assert.assertEquals(project.getBegin().getDate(), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskBegin_NestedTask_ChildrenModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(10, LocalDate.parse("2020-06-15"));
		Assert.assertEquals(LocalDate.parse("2020-06-15"), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(LocalDate.parse("2020-06-15"), project.getTasks().get(0).getBegin().getDate());
	}

	@Test
	public void shouldPosticipateTaskBegin_NestedTask_Children_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(10, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(LocalDate.parse("2020-06-13"), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(LocalDate.parse("2020-06-14"), project.getTasks().get(0).getBegin().getDate());
	}

}
