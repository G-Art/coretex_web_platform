package com.coretex.server.spring;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationProcessor;
import com.coretex.server.ApplicationContextProvider;
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
import java.util.Properties;

public class CortexEnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

    private Logger LOG = LoggerFactory.getLogger(SelectQueryTransformationProcessor.class);

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
            environment.getPropertySources().addFirst(new PropertiesPropertySource("cortexProperties", cortexProperties));
            for (String profile : environment.getActiveProfiles()) {
                Properties profileProperties = new Properties();
                cortexProperties.load(new FileInputStream(homeDir + String.format("/config/coretex-%s.properties", profile)));
                environment.getPropertySources().addFirst(new PropertiesPropertySource("cortexProperties-"+profile, profileProperties));
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
