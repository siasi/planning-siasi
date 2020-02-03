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
	public void shouldParseAConstraintSide() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//@formatter:off
		ConstraintSide side = objectMapper.readValue("{ \n" + 
				"					\"taskId\" : 102, \n" + 
				"					\"side\" : \"END\"\n" + 
				"				}", ConstraintSide.class);
		//@formatter:on
		Assert.assertEquals(102, side.getTaskId());
		Assert.assertEquals(SideType.END, side.getSide());

	}

	@Test
	public void shouldParseAConstraint() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//@formatter:off
		Constraint c = objectMapper.readValue("{\n" + 
				"         \"from\":{\n" + 
				"            \"taskId\":100,\n" + 
				"            \"side\":\"BEGIN\"\n" + 
				"         },\n" + 
				"         \"to\":{\n" + 
				"            \"taskId\":102,\n" + 
				"            \"side\":\"END\"\n" + 
				"         }\n" + 
				"      }", Constraint.class);
		//@formatter:on
		Assert.assertEquals(100, c.getFrom().getTaskId());
		Assert.assertEquals(SideType.BEGIN, c.getFrom().getSide());
		Assert.assertEquals(102, c.getTo().getTaskId());
		Assert.assertEquals(SideType.END, c.getTo().getSide());

	}

	// @Test
	// public void shouldParseASide() throws JsonParseException,
	// JsonMappingException, IOException {
	// ObjectMapper objectMapper = new ObjectMapper();
	// objectMapper.registerModule(new JavaTimeModule());
//		//@formatter:off
//		TaskSide side = objectMapper.readValue("{\n" + 
//				"      \"date\":\"2020-06-20\",\n" + 
//				"      \"constraints\":[]\n" + 
//				"   }", TaskSide.class);
//		//@formatter:on
	// Assert.assertNotNull(side.getDate());
	// Assert.assertFalse(side.hasConstraints());
	// Assert.assertEquals(LocalDate.parse("2020-06-20"), side.getDate());
	// }

	@Test
	public void shouldParseATask() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//@formatter:off
		Task task = objectMapper.readValue("{" + 
				"           \"id\":300," + 
				"           \"name\":\"Third Big Task\"," + 
				"           \"begin\":\"2020-06-18\"," + 
				"           \"end\":\"2020-06-20\"" +
				"         }," + 
				"         \"tasks\":[]" +  
				"      }", Task.class);
		//@formatter:on
		Assert.assertNotNull(task.getBegin());
		Assert.assertNotNull(task.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-18"), task.getBegin());
		Assert.assertEquals(LocalDate.parse("2020-06-20"), task.getEnd());
	}

	@Test
	public void shouldParseAProject() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//@formatter:off
		Project project = objectMapper.readValue("{\n" + 
				"   \"id\":10,\n" + 
				"   \"name\":\"My Project\",\n" + 
				"   \"begin\":\"2020-06-12\",\n" + 
				"   \"end\":\"2020-06-20\",\n" + 
				"   \"tasks\":[\n" + 
				"      {\n" + 
				"         \"id\":100,\n" + 
				"         \"name\":\"First Big Task\",\n" + 
				"         \"begin\":\"2020-06-12\",\n" + 
				"         \"end\":\"2020-06-20\",\n" + 
				"         \"tasks\":[\n" + 
				"            {\n" + 
				"               \"id\":101,\n" + 
				"               \"name\":\"First Big Task - A\",\n" + 
				"               \"begin\":\"2020-06-12\",\n" + 
				"               \"end\":\"2020-06-15\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"id\":102,\n" + 
				"               \"name\":\"First Big Task - B\",\n" + 
				"               \"begin\":\"2020-06-14\",\n" + 
				"               \"end\":\"2020-06-14\"\n" + 
				"            }\n" + 
				"         ]\n" + 
				"      }\n" + 
				"   ]\n" + 
				"}", Project.class);
		//@formatter:on
		Assert.assertNotNull(project.getBegin());
		Assert.assertNotNull(project.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-12"), project.getBegin());
		Assert.assertEquals(LocalDate.parse("2020-06-20"), project.getEnd());
		Assert.assertEquals(1, project.getTasks().size());
		Assert.assertEquals(2, project.getTasks().get(0).getTasks().size());
	}

	@Test
	public void shouldParseAProject_withConstraints() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		//@formatter:off
		Project project = objectMapper.readValue("{\n" + 
				"   \"id\":10,\n" + 
				"   \"name\":\"My Project\",\n" + 
				"   \"begin\":\"2020-06-12\",\n" + 
				"   \"end\":\"2020-06-20\",\n" + 
				"   \"tasks\":[\n" + 
				"      {\n" + 
				"         \"id\":100,\n" + 
				"         \"name\":\"First Big Task\",\n" + 
				"         \"begin\":\"2020-06-12\",\n" + 
				"         \"end\":\"2020-06-20\",\n" + 
				"         \"tasks\":[\n" + 
				"            {\n" + 
				"               \"id\":101,\n" + 
				"               \"name\":\"First Big Task - A\",\n" + 
				"               \"begin\":\"2020-06-12\",\n" + 
				"               \"end\":\"2020-06-15\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"id\":102,\n" + 
				"               \"name\":\"First Big Task - B\",\n" + 
				"               \"begin\":\"2020-06-14\",\n" + 
				"               \"end\":\"2020-06-14\"\n" + 
				"            }\n" + 
				"         ]\n" + 
				"      }\n" + 
				"   ]\n," +
				"\"constraints\":[ {" + 
				"         \"from\":{\n" + 
				"            \"taskId\":100,\n" + 
				"            \"side\":\"BEGIN\"\n" + 
				"         },\n" + 
				"         \"to\":{\n" + 
				"            \"taskId\":102,\n" + 
				"            \"side\":\"END\"\n" + 
				"         }\n" + 
				"      }\n" +  
				"   ]" +
				"}", Project.class);
		//@formatter:on
		Assert.assertNotNull(project.getBegin());
		Assert.assertNotNull(project.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-12"), project.getBegin());
		Assert.assertEquals(LocalDate.parse("2020-06-20"), project.getEnd());
		Assert.assertEquals(1, project.getTasks().size());
		Assert.assertEquals(2, project.getTasks().get(0).getTasks().size());
		Assert.assertEquals(1, project.getConstraints().size());
	}

}
