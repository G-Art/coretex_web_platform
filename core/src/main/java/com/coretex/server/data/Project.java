package com.coretex.server.data;

import java.io.File;
import java.util.List;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 09-01-2017
 */
public interface Project {

    String getName();

    File getPath();

    File getSpringXml();

    List<AbstractProject> getRelatedProjects();
}
