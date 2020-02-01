package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProjectStore {
	private Map<Long, Project> projects = new HashMap<>();

	public Optional<Project> addProject(Project project) {
		long id = projects.size();
		project.setId(id);
		projects.put(id, project);
		return Optional.ofNullable(project);
	}

	public Optional<Project> getProject(long id) {
		return Optional.ofNullable(projects.get(id));
	}

}
