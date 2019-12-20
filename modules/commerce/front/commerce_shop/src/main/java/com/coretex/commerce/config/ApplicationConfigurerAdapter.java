package com.coretex.commerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.PathResourceResolver;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebFlux
public class ApplicationConfigurerAdapter implements WebFluxConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        final String[] staticLocations = {"/resources/static/app/"};
        final String[] indexLocations = new String[staticLocations.length];
        for (int i = 0; i < staticLocations.length; i++) {
            indexLocations[i] = staticLocations[i] + "index.html";
        }

        registry.addResourceHandler("/**",
                "/*.css",
                "/*.js",
                "/*.gif",
                "/*.css",
                "/*.ico",
                "/*.js",
                "/*.map",
                "/*.png",
                "/*.jpg",
                "/*.svg",
                "/*.woff",
                "/*.woff2",
                "/*.eot",
                "/*.otf",
                "/*.ttf",
                "/**/*.css",
                "/**/*.ico",
                "/**/*.js",
                "/**/*.gif",
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
                .setCacheControl(CacheControl.maxAge(600, TimeUnit.SECONDS));

//        registry.addResourceHandler("/**")
//                .addResourceLocations(indexLocations)
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver() {
//                    @Override
//                    protected Mono<Resource> getResource(String resourcePath, Resource location) {
//                        return Mono.just(location)
//                                .filter(Resource::exists)
//                                .filter(Resource::isReadable);
//                    }
//                });
    }

}