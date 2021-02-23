package com.coretex.commerce.config;

import com.coretex.commerce.config.security.AuthenticationManager;
import com.coretex.commerce.config.security.SecurityContextRepository;
import com.coretex.commerce.web.resolvers.ParamMapHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ApplicationConfigurerAdapter implements WebFluxConfigurer {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SecurityContextRepository securityContextRepository;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        final String[] staticLocations = {"/resources/static/"};
        final String[] indexLocations = new String[staticLocations.length];
        for (int i = 0; i < staticLocations.length; i++) {
            indexLocations[i] = staticLocations[i] + "/app/index.html";
        }

        registry.addResourceHandler("/**",
                "/*.json",
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
                "/*/*.json",
                "/*/*.css",
                "/*/*.ico",
                "/*/*.js",
                "/*/*.gif",
                "/*/*.map",
                "/*/*.png",
                "/*/*.jpg",
                "/*/*.svg",
                "/*/*.woff",
                "/*/*.woff2",
                "/*/*.eot",
                "/*/*.otf",
                "/*/*.ttf")
                .addResourceLocations(staticLocations)
                .setCacheControl(CacheControl.maxAge(100000, TimeUnit.SECONDS));

        registry.addResourceHandler(
                "/*.html",
                "/*.json",
                "/*/*.html",
                "/*/*.json")
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .anyExchange().permitAll()
                .and().build();
    }


    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new ParamMapHandlerMethodArgumentResolver());
    }
}