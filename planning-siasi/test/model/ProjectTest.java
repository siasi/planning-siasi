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

		project.updateTaskEnd(0, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getEnd());
	}

	@Test
	public void shouldPosticipateTaskEnd_NestedTask_ParentModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(100, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getTasks().get(0).getEnd());
	}

	@Test
	public void shouldPosticipateTaskEnd_NestedTask_Parent_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(100, LocalDate.parse("2020-06-19"));
		Assert.assertEquals(LocalDate.parse("2020-06-19"), project.getTasks().get(0).getEnd());
		Assert.assertEquals(project.getEnd(), LocalDate.parse("2020-06-20"));
	}

	@Test
	public void shouldAnticipateTaskEnd_SingleTask_AnticipateAfterTheBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(0, LocalDate.parse("2020-06-18"));
		Assert.assertEquals(LocalDate.parse("2020-06-18"), project.getEnd());
	}

	@Test
	public void shouldAnticipateTaskEnd_SingleTask_AnticipateAtTheBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(0, LocalDate.parse("2020-06-12"));
		Assert.assertEquals(LocalDate.parse("2020-06-12"), project.getEnd());
	}

	@Test
	public void shouldAnticipateTaskEnd_SingleTask_AnticipateBeforeTheBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(0, LocalDate.parse("2020-06-11"));
		Assert.assertEquals(LocalDate.parse("2020-06-11"), project.getEnd());
		Assert.assertEquals(project.getEnd(), project.getBegin());
	}

	@Test
	public void shouldAnticipateTaskEnd_NestedTask_ChildrenModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(10, LocalDate.parse("2020-06-17"));
		Assert.assertEquals(LocalDate.parse("2020-06-17"), project.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-17"), project.getTasks().get(0).getEnd());
	}

	@Test
	public void shouldAnticipateTaskEnd_NestedTask_Children_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskEnd(10, LocalDate.parse("2020-06-19"));
		Assert.assertEquals(LocalDate.parse("2020-06-19"), project.getEnd());
		Assert.assertEquals(LocalDate.parse("2020-06-18"), project.getTasks().get(0).getEnd());
	}

	@Test
	public void shouldAnticipateTaskBegin_SingleTask() throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(0, LocalDate.parse("2020-06-10"));
		Assert.assertEquals(LocalDate.parse("2020-06-10"), project.getBegin());
		// Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldAnticipateTaskBegin_NestedTask_ParentModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(100, LocalDate.parse("2020-06-10"));
		Assert.assertEquals(LocalDate.parse("2020-06-10"), project.getTasks().get(0).getBegin());
		// Assert.assertEquals(project, highestModifiedTask);
		// Assert.assertEquals(LocalDate.parse("2020-06-10"),
		// highestModifiedTask.getBegin());
	}

	@Test
	public void shouldAnticipateTaskBegin_NestedTask_Parent_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(100, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(LocalDate.parse("2020-06-13"), project.getTasks().get(0).getBegin());
		// Assert.assertEquals(project.getTasks().get(0), highestModifiedTask);
		// Assert.assertEquals(highestModifiedTask.getBegin(),
		// LocalDate.parse("2020-06-13"));
		Assert.assertEquals(project.getBegin(), LocalDate.parse("2020-06-12"));
	}

	/// TODO

	@Test
	public void shouldPosticipateTaskBegin_SingleTask_PosticipateBeforeTheEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(0, LocalDate.parse("2020-06-14"));
		Assert.assertEquals(LocalDate.parse("2020-06-14"), project.getBegin());
		// Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskBegin_SingleTask_PosticipateAtTheEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(0, LocalDate.parse("2020-06-20"));
		Assert.assertEquals(LocalDate.parse("2020-06-20"), project.getBegin());
		// Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskBegin_SingleTask_PosticipateAfterTheEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(0, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(LocalDate.parse("2020-06-21"), project.getBegin());
		Assert.assertEquals(project.getBegin(), project.getEnd());
		// Assert.assertEquals(project, highestModifiedTask);
	}

	@Test
	public void shouldPosticipateTaskBegin_NestedTask_ChildrenModified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(10, LocalDate.parse("2020-06-15"));
		Assert.assertEquals(LocalDate.parse("2020-06-15"), project.getBegin());
		// Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(LocalDate.parse("2020-06-15"), project.getTasks().get(0).getBegin());
	}

	@Test
	public void shouldPosticipateTaskBegin_NestedTask_Children_NOT_Modified()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/oneNestedTaskProject.json");
		Assert.assertTrue(project.isValid());

		project.updateTaskBegin(10, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(LocalDate.parse("2020-06-13"), project.getBegin());
		// Assert.assertEquals(project, highestModifiedTask);
		Assert.assertEquals(LocalDate.parse("2020-06-14"), project.getTasks().get(0).getBegin());
	}

	@Test
	public void shouldLoadConstraintsProperly() throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/simpleProjectWithConstraints.json");
		Assert.assertTrue(project.isValid());

		// Assert the constraint 100/BEGIN -> 102/END is properly loaded
		Constraint expected100_102 = new Constraint(new ConstraintSide(100, SideType.BEGIN),
				new ConstraintSide(102, SideType.END));

		Assert.assertEquals(1, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(expected100_102, project.getTask(100).getOutgoingConstraints().get(0));

		Assert.assertEquals(1, project.getTask(102).getIngoingConstraints().size());
		Assert.assertEquals(expected100_102, project.getTask(102).getIngoingConstraints().get(0));

		// Assert the constraint 202/END -> 300/BEGIN is properly loaded
		Constraint expected202_300 = new Constraint(new ConstraintSide(202, SideType.END),
				new ConstraintSide(300, SideType.BEGIN));

		Assert.assertEquals(1, project.getTask(202).getOutgoingConstraints().size());
		Assert.assertEquals(expected202_300, project.getTask(202).getOutgoingConstraints().get(0));

		Assert.assertEquals(1, project.getTask(300).getIngoingConstraints().size());
		Assert.assertEquals(expected202_300, project.getTask(300).getIngoingConstraints().get(0));
	}

	/*
	 * Verify constraints are removed
	 */

	@Test
	public void shouldRemoveInvalidConstraints_BEGIN_BEGIN_posticipateBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(200).getIngoingConstraints().size());
		project.updateTaskBegin(100, LocalDate.parse("2020-06-18"));
		Assert.assertEquals(0, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(200).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_BEGIN_BEGIN_anticipateBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(200).getIngoingConstraints().size());
		project.updateTaskBegin(200, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(0, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(200).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_END_END_posticipateBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(300).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(400).getIngoingConstraints().size());
		project.updateTaskBegin(300, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(0, project.getTask(300).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(400).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_BEGIN_END_anticipatedEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(300).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(400).getIngoingConstraints().size());
		project.updateTaskEnd(400, LocalDate.parse("2020-06-13"));
		Assert.assertEquals(0, project.getTask(300).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(400).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_END_BEGIN_posticipateEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(500).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(600).getIngoingConstraints().size());
		project.updateTaskEnd(500, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(0, project.getTask(500).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(600).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_END_BEGIN_anticipatedBegin()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(500).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(600).getIngoingConstraints().size());
		project.updateTaskEnd(600, LocalDate.parse("2020-06-15"));
		Assert.assertEquals(0, project.getTask(500).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(600).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_END_END_posticipateEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(700).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(800).getIngoingConstraints().size());
		project.updateTaskEnd(700, LocalDate.parse("2020-06-21"));
		Assert.assertEquals(0, project.getTask(700).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(800).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraints_END_END_anticipatedEnd()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(700).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(800).getIngoingConstraints().size());
		project.updateTaskEnd(800, LocalDate.parse("2020-06-15"));
		Assert.assertEquals(0, project.getTask(700).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(800).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	@Test
	public void shouldRemoveInvalidConstraint_BEGIN_BEGIN_posticipateBeginAndCollapse()
			throws JsonParseException, JsonMappingException, IOException {
		Project project = parse("test/resources/unidirectionalConstraints.json");
		Assert.assertTrue(project.isValid());

		Assert.assertEquals(4, project.getConstraints().size());
		Assert.assertEquals(1, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(1, project.getTask(200).getIngoingConstraints().size());
		project.updateTaskBegin(100, LocalDate.parse("2020-06-20"));
		Assert.assertEquals(0, project.getTask(100).getOutgoingConstraints().size());
		Assert.assertEquals(0, project.getTask(200).getIngoingConstraints().size());
		Assert.assertEquals(3, project.getConstraints().size());
	}

	// TODO Add the other tests for the collapse scenario (when moving one side of
	// the task
	// also the other is updated)

}
