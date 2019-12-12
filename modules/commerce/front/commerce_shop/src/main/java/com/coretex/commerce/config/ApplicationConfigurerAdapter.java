package com.coretex.commerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
public class ApplicationConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        final String[] staticLocations = {"/resources/static/app/"};
        final String[] indexLocations = new String[staticLocations.length];
        for (int i = 0; i < staticLocations.length; i++) {
            indexLocations[i] = staticLocations[i] + "index.html";
        }

        registry.addResourceHandler(
                "/*.css",
                "/*.js",
                "/**/*.css",
                "/**/*.ico",
                "/**/*.js",
                "/**/*.map",
                "/**/*.png",
                "/**/*.jpg",
                "/**/*.svg",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.eot",
                "/**/*.otf",
                "/**/*.ttf")
                .addResourceLocations(staticLocations)
                .setCacheControl(CacheControl.maxAge(100000, TimeUnit.SECONDS));

        registry.addResourceHandler(
                "/*.html",
                "/*.json")
                .addResourceLocations(staticLocations)
                .setCacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS));

        registry.addResourceHandler("/**")
                .addResourceLocations(indexLocations)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) {
                        return location.exists() && location.isReadable() ? location : null;
                    }
                });
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}