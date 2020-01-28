package model;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class ProjectStoreTest {

	@Test
	public void shouldStoreAProject() {
		ProjectStore store = new ProjectStore();
		Project project = new Project();
		store.addProject(project);

		Assert.assertNotEquals(Optional.empty(), store.getProject(0));
		Assert.assertEquals(project, store.getProject(0).get());
	}
}
