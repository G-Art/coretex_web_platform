package com.coretex.server.spring;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.core.utils.PlatformUtils;
import com.coretex.server.ApplicationContextProvider;
import com.coretex.server.data.WebProject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class CortexEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

    private final static Logger LOG = LoggerFactory.getLogger(SelectQueryTransformationProcessor.class);

    private final WebProject webProject;

    public CortexEnvironmentInitializer() {
        webProject = null; // for integration test purposes
    }

    public CortexEnvironmentInitializer(WebProject webProject) {
        LOG.info("Initialization web module [{}] ", webProject.getName());
        PlatformUtils.init(webProject);
        this.webProject = webProject;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String homeDir = environment.getProperty("app.home");
        if(StringUtils.isBlank(homeDir)){
            homeDir = tryFindRelativePath();
        }

        try {
            Properties cortexProperties = new Properties();
            cortexProperties.load(new FileInputStream(homeDir + "/config/coretex.properties"));
            cortexProperties.put("coretex.config.path", new File(homeDir + "/config").getAbsolutePath());
            var propertySources = environment.getPropertySources();
            if(Objects.nonNull(webProject)){
                propertySources.addFirst(new PropertiesPropertySource("moduleProperties", webProject.getProperties()));
            }
            propertySources.addFirst(new PropertiesPropertySource("cortexProperties", cortexProperties));
            for (String profile : environment.getActiveProfiles()) {
                Properties profileProperties = new Properties();
                cortexProperties.load(new FileInputStream(homeDir + String.format("/config/coretex-%s.properties", profile)));
                propertySources.addFirst(new PropertiesPropertySource("cortexProperties-"+profile, profileProperties));
            }

        } catch (IOException ex) {
            throw new BeanInitializationException("Can't initialize coretex environment", ex);
        }

        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

    private String tryFindRelativePath() {
        Path path;
        try {
            path = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent();
        } catch (URISyntaxException e) {
            LOG.error("Relative path is not found", e);
            return null;
        }
        File relativePath = path.toFile();
        if(relativePath.exists() && relativePath.isDirectory()){
            return relativePath.toString();
        }
        return null;
    }
}
