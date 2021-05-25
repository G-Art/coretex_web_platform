package com.coretex.server.data;


import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 09-01-2017
 */
public abstract class AbstractProject implements Project {

    private static final String CORE_PROJECT_NAME = "core";

    private String name;
    private File path;
    private File springXml;
    private List<AbstractProject> relatedProjects;

    private Object object;

    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean isCore() {
        return !StringUtils.isEmpty(getName()) && getName().toLowerCase().equals(CORE_PROJECT_NAME);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public File getPath() {
        return path;
    }

    @Override
    public File getSpringXml() {
        return springXml;
    }

    @Override
    public List<AbstractProject> getRelatedProjects() {
        return relatedProjects;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public void setPath(String path){
        setPath(new File(path));
    }

    public void setSpringXml(File springXml) {
        this.springXml = springXml;
    }

    public void setSpringXml(String absolutePath) throws FileNotFoundException {
        File springXml = new File(absolutePath);
        if(springXml.exists() && springXml.isFile()){
            this.springXml = springXml;
        }else {
            throw new FileNotFoundException("File: "+absolutePath+" not found");
        }
    }


    public void setRelatedProjects(List<AbstractProject> relatedProjects) {
        this.relatedProjects = relatedProjects;
    }

    public void addRelatedProject(AbstractProject relatedProject){
        if(this.relatedProjects == null){
            this.relatedProjects = new LinkedList<>();
        }
        this.relatedProjects.add(relatedProject);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
