package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import play.libs.Json;

public class Projects {

	public static ObjectNode aValidProjects() throws JsonProcessingException {
		final ObjectMapper mapper = Json.mapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		ObjectNode myProjectTask = newTask(mapper, "10", "MyProject", "2020-06-12", "2020-06-20");

		ObjectNode firstBigTask = newTask(mapper, "100", "FirstBigTask", "2020-06-12", "2020-06-20");

		ObjectNode firstBigTask_A = newTask(mapper, "101", "FirstBigTask - A", "2020-06-12", "2020-06-15");
		ObjectNode firstBigTask_B = newTask(mapper, "102", "FirstBigTask - B", "2020-06-14", "2020-06-16");
		ObjectNode firstBigTask_C = newTask(mapper, "103", "FirstBigTask - C", "2020-06-16", "2020-06-20");

		addTaks(firstBigTask, firstBigTask_A);
		addTaks(firstBigTask, firstBigTask_B);
		addTaks(firstBigTask, firstBigTask_C);

		ObjectNode secondBigTask = newTask(mapper, "200", "SecondBigTask", "2020-06-14", "2020-06-18");

		ObjectNode secondBigTask_A = newTask(mapper, "200", "SecondBigTask - A", "2020-06-14", "2020-06-15");
		ObjectNode secondBigTask_B = newTask(mapper, "200", "SecondBigTask - B", "2020-06-15", "2020-06-18");

		addTaks(secondBigTask, secondBigTask_A);
		addTaks(secondBigTask, secondBigTask_B);

		ObjectNode thirdBigTask = newTask(mapper, "300", "ThirdBigTask", "2020-06-18", "2020-06-20");

		addTaks(myProjectTask, firstBigTask);
		addTaks(myProjectTask, secondBigTask);
		addTaks(myProjectTask, thirdBigTask);

		return myProjectTask;
	}

	public static ObjectNode aValidProjectWithConstraints() throws JsonProcessingException {
		final ObjectMapper mapper = Json.mapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		ObjectNode myProjectTask = newTask(mapper, "10", "MyProject", "2020-06-12", "2020-06-20");

		ObjectNode firstBigTask = newTask(mapper, "100", "FirstBigTask", "2020-06-12", "2020-06-20");

		ObjectNode firstBigTask_A = newTask(mapper, "101", "FirstBigTask - A", "2020-06-12", "2020-06-15");
		ObjectNode firstBigTask_B = newTask(mapper, "102", "FirstBigTask - B", "2020-06-14", "2020-06-16");
		ObjectNode firstBigTask_C = newTask(mapper, "103", "FirstBigTask - C", "2020-06-16", "2020-06-20");

		addTaks(firstBigTask, firstBigTask_A);
		addTaks(firstBigTask, firstBigTask_B);
		addTaks(firstBigTask, firstBigTask_C);

		ObjectNode secondBigTask = newTask(mapper, "200", "SecondBigTask", "2020-06-14", "2020-06-18");

		ObjectNode secondBigTask_A = newTask(mapper, "200", "SecondBigTask - A", "2020-06-14", "2020-06-15");
		ObjectNode secondBigTask_B = newTask(mapper, "200", "SecondBigTask - B", "2020-06-15", "2020-06-18");

		addTaks(secondBigTask, secondBigTask_A);
		addTaks(secondBigTask, secondBigTask_B);

		ObjectNode thirdBigTask = newTask(mapper, "300", "ThirdBigTask", "2020-06-18", "2020-06-20");

		addTaks(myProjectTask, firstBigTask);
		addTaks(myProjectTask, secondBigTask);
		addTaks(myProjectTask, thirdBigTask);

		return myProjectTask;
	}

	public static ObjectNode anInvalidProject() throws JsonProcessingException {
		final ObjectMapper mapper = Json.mapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		ObjectNode myProjectTask = newTask(mapper, "10", "MyProject", "2020-06-12", "2020-06-20");

		ObjectNode firstBigTask = newTask(mapper, "100", "FirstBigTask", "2020-06-12", "2020-06-20");

		ObjectNode firstBigTask_A = newTask(mapper, "101", "FirstBigTask - A", "2020-06-12", "2020-06-15");
		ObjectNode firstBigTask_B = newTask(mapper, "102", "FirstBigTask - B", "2020-06-14", "2020-06-16");
		ObjectNode firstBigTask_C = newTask(mapper, "103", "FirstBigTask - C", "2020-06-16", "2020-06-20");

		addTaks(firstBigTask, firstBigTask_A);
		addTaks(firstBigTask, firstBigTask_B);
		addTaks(firstBigTask, firstBigTask_C);

		ObjectNode secondBigTask = newTask(mapper, "200", "SecondBigTask", "2020-06-14", "2020-06-18");

		ObjectNode secondBigTask_A = newTask(mapper, "201", "SecondBigTask - A", "2020-06-14", "2020-06-15");
		/*
		 * ERROR - This task completes after its parent!
		 */
		ObjectNode secondBigTask_B = newTask(mapper, "202", "SecondBigTask - B", "2020-06-15", "2020-06-21");

		addTaks(secondBigTask, secondBigTask_A);
		addTaks(secondBigTask, secondBigTask_B);

		ObjectNode thirdBigTask = newTask(mapper, "300", "ThirdBigTask", "2020-06-18", "2020-06-20");

		addTaks(myProjectTask, firstBigTask);
		addTaks(myProjectTask, secondBigTask);
		addTaks(myProjectTask, thirdBigTask);

		return myProjectTask;
	}

	public static ObjectNode aSimpleInvalidProject() {
		final ObjectMapper mapper = Json.mapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		ObjectNode secondBigTask = newTask(mapper, "200", "SecondBigTask", "2020-06-14", "2020-03-18");

		ObjectNode secondBigTask_A = newTask(mapper, "200", "SecondBigTask - A", "2020-06-14", "2020-03-15");
		/*
		 * ERROR - This task completes after its parent!
		 */
		ObjectNode secondBigTask_B = newTask(mapper, "200", "SecondBigTask - B", "2020-06-15", "2020-03-21");

		addTaks(secondBigTask, secondBigTask_A);
		addTaks(secondBigTask, secondBigTask_B);
		return secondBigTask;
	}

	private static void addTaks(ObjectNode firstBigTask, ObjectNode firstBigTask_A) {
		((ArrayNode) firstBigTask.get("tasks")).add(firstBigTask_A);
	}

	private static ObjectNode newTask(final ObjectMapper mapper, String id, String name, String begin, String end) {
		ObjectNode taskNode = mapper.createObjectNode();
		taskNode.put("id", id);
		taskNode.put("name", name);
		taskNode.put("begin", begin);
		taskNode.put("end", end);
		taskNode.set("tasks", mapper.createArrayNode());
		return taskNode;
	}
}
