package controllers;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import model.Project;
import model.ProjectStore;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class ProjectController extends Controller {

	/**
	 * An action that renders an HTML page with a welcome message. The configuration
	 * in the <code>routes</code> file means that this method will be called when
	 * the application receives a <code>GET</code> request with a path of
	 * <code>/</code>.
	 */
	public Result index() {
		return ok(views.html.index.render());
	}

	private HttpExecutionContext context;
	private ProjectStore projectStore;

	@Inject
	public ProjectController(HttpExecutionContext context, ProjectStore projectStore) {
		this.projectStore = projectStore;
		this.context = context;
	}

	public CompletionStage<Result> create(Http.Request request) {
		JsonNode json = request.body().asJson();
		return supplyAsync(() -> {
			if (json == null) {
				return badRequest(createResponse("Expecting Json data", false));
			}

			Project aProject = Json.fromJson(json, Project.class);

			if (!aProject.isValid()) {
				return badRequest(createResponse("Project is not consistent", false));
			}

			Optional<Project> projectOptional = projectStore.addProject(aProject);
			return projectOptional.map(project -> {
				JsonNode jsonObject = Json.toJson(project);
				return created(createResponse(jsonObject, true));
			}).orElse(internalServerError(createResponse("Could not create data.", false)));
		}, context.current());
	}

	public CompletionStage<Result> retrieve(long id) {
		return supplyAsync(() -> {
			final Optional<Project> projectOptional = projectStore.getProject(id);
			return projectOptional.map(project -> {
				JsonNode jsonObjects = Json.toJson(project);
				return ok(createResponse(jsonObjects, true));
			}).orElse(notFound(createResponse("Project with id:" + id + " not found", false)));
		}, context.current());
	}

	public CompletionStage<Result> updateTaskEnd(long id, long taskId, String newDate) {

		return supplyAsync(() -> {
			Optional<Project> projectOptional = projectStore.getProject(id);
			return projectOptional.map(project -> {
				if (project == null) {
					return notFound(createResponse("Project not found", false));
				}

				project.updateTaskEnd(taskId, LocalDate.parse(newDate));
				JsonNode jsonObject = Json.toJson(project);
				return ok(createResponse(jsonObject, true));
			}).orElse(internalServerError(createResponse("Could not update task " + taskId, false)));
		}, context.current());

	}

	public CompletionStage<Result> updateTaskBegin(long id, long taskId, String newDate) {
		return null;
	}

	private static ObjectNode createResponse(Object response, boolean ok) {
		ObjectNode result = Json.newObject();
		result.put("isSuccessful", ok);
		if (response instanceof String) {
			result.put("body", (String) response);
		} else {
			result.putPOJO("body", response);
		}
		return result;
	}

}
