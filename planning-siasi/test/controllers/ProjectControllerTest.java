package controllers;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.PUT;
import static play.test.Helpers.route;

import java.io.File;
import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectControllerTest extends WithApplication {

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().build();
	}

	@Test
	public void a_shouldCreateASimpleModel() throws IOException {
		// ObjectNode projectNode = Projects.aValidProjects();
		JsonNode projectNode = parseJson("test/resources/simpleProject.json");

		Http.RequestBuilder request = new Http.RequestBuilder().method(POST).bodyJson(projectNode).uri("/project");

		Result result = route(app, request);
		assertEquals(CREATED, result.status());
	}

	@Test
	public void shouldRejectAnInvalidModel() throws IOException {
		// ObjectNode projectNode = Projects.anInvalidProject();
		JsonNode projectNode = parseJson("test/resources/invalidProject.json");

		Http.RequestBuilder request = new Http.RequestBuilder().method(POST).bodyJson(projectNode).uri("/project");

		Result result = route(app, request);
		assertEquals(BAD_REQUEST, result.status());
	}

	@Test
	public void b_shouldRetrieveAnExistingModel() throws IOException {
		// ObjectNode projectNode = Projects.aValidProjects();
		JsonNode projectNode = parseJson("test/resources/simpleProject.json");

		Http.RequestBuilder createRequest = new Http.RequestBuilder().method(POST).bodyJson(projectNode)
				.uri("/project");

		Result createResult = route(app, createRequest);
		assertEquals(CREATED, createResult.status());

		Http.RequestBuilder retrieveRequest = new Http.RequestBuilder().method(GET).uri("/project/0");

		Result retrieveResult = route(app, retrieveRequest);
		assertEquals(OK, retrieveResult.status());

	}

	@Test
	public void allowTaskEndUpdate() throws IOException {
		// ObjectNode projectNode = Projects.aValidProjects();
		JsonNode projectNode = parseJson("test/resources/oneTaskProject.json");

		Http.RequestBuilder createRequest = new Http.RequestBuilder().method(POST).bodyJson(projectNode)
				.uri("/project");

		Result createResult = route(app, createRequest);
		assertEquals(CREATED, createResult.status());

		Http.RequestBuilder retrieveRequest = new Http.RequestBuilder().method(PUT)
				.uri("/project/0/task/10/end/2020-01-22");

		Result retrieveResult = route(app, retrieveRequest);
		assertEquals(OK, retrieveResult.status());

	}

	// TODO allow task begin update
	//

	private JsonNode parseJson(String fileName) throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File(fileName);
		JsonNode projectNode = objectMapper.readTree(file);
		return projectNode;
	}

}
