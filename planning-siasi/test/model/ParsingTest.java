package model;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ParsingTest {

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

}
