package model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ProjectTest {

	@Test
	public void projectWithOneTaskAndDateSet_shouldBeValid() {
		Project project = new Project("", new TaskSide(LocalDate.parse("2020-06-12")),
				new TaskSide(LocalDate.parse("2020-06-20")));
		Task task = new Task("", new TaskSide(LocalDate.parse("2020-06-12")),
				new TaskSide(LocalDate.parse("2020-06-20")));
		project.addTask(task);

		Assert.assertTrue(project.isValid());
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
	public void shouldParseAConstraint() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		ConstraintSide side = objectMapper.readValue("{ \n" + "					\"taskId\" : 102, \n"
				+ "					\"side\" : \"END\"\n" + "				}", ConstraintSide.class);
		Assert.assertEquals(102, side.getTaskId());
		Assert.assertEquals(SideType.END, side.getSide());

	}

	@Test
	public void shouldParseASide() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		TaskSide side = objectMapper.readValue(
				"{\n" + "      \"date\":\"2020-06-20\",\n" + "      \"constraints\":[]\n" + "   }", TaskSide.class);
		Assert.assertNotNull(side.getDate());
		Assert.assertFalse(side.hasConstraints());
		Assert.assertEquals(LocalDate.parse("2020-06-20"), side.getDate());
	}

	@Test
	public void shouldParseATask() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Task task = objectMapper.readValue("{\n" + "         \"id\":300,\n" + "         \"name\":\"Third Big Task\",\n"
				+ "         \"begin\":{\n" + "            \"date\":\"2020-06-18\",\n"
				+ "            \"constraints\":[\n" + "\n" + "            ]\n" + "         },\n"
				+ "         \"end\":{\n" + "            \"date\":\"2020-06-20\",\n" + "            \"constraints\":[\n"
				+ "\n" + "            ]\n" + "         },\n" + "         \"tasks\":[\n" + "\n" + "         ]\n"
				+ "      }", Task.class);
		Assert.assertNotNull(task.getBegin());
		Assert.assertNotNull(task.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-18"), task.getBegin().getDate());
		Assert.assertEquals(LocalDate.parse("2020-06-20"), task.getEnd().getDate());
		Assert.assertFalse(task.getBegin().hasConstraints());
		Assert.assertFalse(task.getEnd().hasConstraints());
	}

	@Test
	public void shouldParseAProject() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Project project = objectMapper.readValue("{\n" + "   \"id\":10,\n" + "   \"name\":\"My Project\",\n"
				+ "   \"begin\":{\n" + "      \"date\":\"2020-06-12\",\n" + "      \"constraints\":[]\n" + "   },\n"
				+ "   \"end\":{\n" + "      \"date\":\"2020-06-20\",\n" + "      \"constraints\":[]\n" + "   },\n"
				+ "   \"tasks\":[\n" + "      {\n" + "         \"id\":100,\n"
				+ "         \"name\":\"First Big Task\",\n" + "         \"begin\":{\n"
				+ "            \"date\":\"2020-06-12\",\n" + "            \"constraints\":[\n" + "\n"
				+ "            ]\n" + "         },\n" + "         \"end\":{\n"
				+ "            \"date\":\"2020-06-20\",\n" + "            \"constraints\":[\n" + "\n"
				+ "            ]\n" + "         },\n" + "         \"tasks\":[\n" + "            {\n"
				+ "               \"id\":101,\n" + "               \"name\":\"First Big Task - A\",\n"
				+ "               \"begin\":{\n" + "                  \"date\":\"2020-06-12\",\n"
				+ "                  \"constraints\":[\n" + "\n" + "                  ]\n" + "               },\n"
				+ "               \"end\":{\n" + "                  \"date\":\"2020-06-15\",\n"
				+ "                  \"constraints\":[\n" + "\n" + "                  ]\n" + "               }\n"
				+ "            }\n" + "         ]\n" + "      }]\n" + "}}", Project.class);
		Assert.assertNotNull(project.getBegin());
		Assert.assertNotNull(project.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-12"), project.getBegin().getDate());
		Assert.assertEquals(LocalDate.parse("2020-06-20"), project.getEnd().getDate());
		Assert.assertEquals(1, project.getTasks().size());
		Assert.assertEquals(1, project.getTasks().get(0).getTasks().size());
	}

	@Test
	public void shouldDetectAnInvalidModel_BecauseOfDates()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/invalidProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void shouldDetectAnInvalidModel_BecauseOfConstraints()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/invalidProject_bacause_of_constraints.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertFalse(project.isValid());
	}

	@Test
	public void shouldUpdateTaskEnd_SingleTask() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/oneTaskProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(0, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldUpdateTaskEnd_NestedTask_ParentModified()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/oneNestedTaskProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(100, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getTasks().get(0).getEnd().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getEnd().getDate(), LocalDate.parse("2020-06-21"));
	}

	@Test
	public void shouldUpdateTaskEnd_NestedTask_Parent_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/oneNestedTaskProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskEnd(100, LocalDate.parse("2020-06-19"));
		Assert.assertEquals(LocalDate.parse("2020-06-19"), project.getTasks().get(0).getEnd().getDate());
		Assert.assertEquals(project.getTasks().get(0), highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getEnd().getDate(), LocalDate.parse("2020-06-19"));
		Assert.assertEquals(project.getEnd().getDate(), LocalDate.parse("2020-06-20"));
	}

	@Test
	public void shouldAllowTaskBeginUpdate_SingleTask() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/oneTaskProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(0, LocalDate.parse("2020-06-10"));
		Assert.assertEquals(LocalDate.parse("2020-06-10"), project.getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldUpdateTaskBegin_NestedTask_ParentModified()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/oneNestedTaskProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(100, LocalDate.parse("2020-06-10"));
		Assert.assertEquals(LocalDate.parse("2020-06-10"), project.getTasks().get(0).getBegin().getDate());
		Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getBegin().getDate(), LocalDate.parse("2020-06-10"));
	}

	@Test
	public void shouldUpdateTaskBegin_NestedTask_Parent_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File("test/resources/oneNestedTaskProject.json");
		Project project = objectMapper.readValue(file, Project.class);
		Assert.assertTrue(project.isValid());

		Task highestModifiedTask = project.updateTaskBegin(100, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(LocalDate.parse("2020-06-13"), project.getTasks().get(0).getBegin().getDate());
		Assert.assertEquals(project.getTasks().get(0), highestModifiedTask);
		Assert.assertEquals(highestModifiedTask.getBegin().getDate(), LocalDate.parse("2020-06-13"));
		Assert.assertEquals(project.getBegin().getDate(), LocalDate.parse("2020-06-12"));
	}

}
