package com.coretex.core.utils;

import com.coretex.server.data.Project;
import com.coretex.server.data.WebProject;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.io.File;

public class PlatformUtils implements EnvironmentAware {

	private static Environment environment;
	private static WebProject webProject;

	public static void init(WebProject webProject){
		PlatformUtils.webProject = webProject;
	}

	@Override
	public void setEnvironment(Environment environment) {
		PlatformUtils.environment = environment;
	}

	public static File getModulePath(String name){
		if (webProject.getName().equals(name)) {
			return webProject.getPath();
		}
		var project = webProject.getRelatedProjects()
				.stream()
				.filter(p -> p.getName().equals(name))
				.findAny();
		return project.map(Project::getPath).orElse(null);
	}
}
