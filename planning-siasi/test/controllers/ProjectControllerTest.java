package controllers;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.CREATED;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import model.Projects;
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
	public void shouldCreateASimpleModel() throws JsonProcessingException {
		ObjectNode projectNode = Projects.aValidProjects();
		Http.RequestBuilder request = new Http.RequestBuilder().method(POST).bodyJson(projectNode).uri("/project");

		Result result = route(app, request);
		assertEquals(CREATED, result.status());
	}

	@Test
	public void shouldRejectAnInvalidModel() throws JsonProcessingException {
		ObjectNode projectNode = Projects.anInvalidProject();

		Http.RequestBuilder request = new Http.RequestBuilder().method(POST).bodyJson(projectNode).uri("/project");

		Result result = route(app, request);
		assertEquals(BAD_REQUEST, result.status());
	}

}
