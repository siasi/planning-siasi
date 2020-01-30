package controllers;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

public class ProjectControllerTest extends WithApplication {

	@Override
	protected Application provideApplication() {
		return new GuiceApplicationBuilder().build();
	}

	@Test
	public void shouldCreateASimpleModel() throws IOException {
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

	private JsonNode parseJson(String fileName) throws IOException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		File file = new File(fileName);
		JsonNode projectNode = objectMapper.readTree(file);
		return projectNode;
	}

}
