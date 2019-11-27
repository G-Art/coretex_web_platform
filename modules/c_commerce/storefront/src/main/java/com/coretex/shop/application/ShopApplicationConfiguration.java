package com.coretex.shop.application;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.facebook.security.FacebookAuthenticationService;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.coretex.shop", "com.coretex.core.business"})
@EnableWebSecurity
@EnableWebMvc
@EnableSocial
public class ShopApplicationConfiguration implements WebMvcConfigurer, SocialConfigurer {

	protected final Log logger = LogFactory.getLog(getClass());

	@Value("${facebook.app.id}")
	private String facebookAppId;

	@Value("${facebook.app.secret}")
	private String facebookAppSecret;

	@Resource
	private DataSource dataSource;

	@Resource
	private TextEncryptor textEncryptor;

	@EventListener(ContextStartedEvent.class)
	public void applicationReadyCode() {
		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);
	}


	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);

		return mapper;
	}

	/**
	 * Configure TilesConfigurer.
	 */
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions("/WEB-INF/tiles/tiles-admin.xml", "/WEB-INF/tiles/tiles-shop.xml");
		tilesConfigurer.setCheckRefresh(true);
		return tilesConfigurer;
	}

	/**
	 * Configure ViewResolvers to deliver preferred views.
	 */

	@Bean
	public TilesViewResolver tilesViewResolver() {
		final TilesViewResolver resolver = new TilesViewResolver();
		resolver.setViewClass(TilesView.class);
		resolver.setOrder(0);
		return resolver;
	}

    
/*    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();

        registry.addConnectionFactory(new FacebookConnectionFactory(
        		facebookAppId,
        		facebookAppSecret));

        return registry;
    }*/


	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(new FacebookConnectionFactory(
				facebookAppId,
				facebookAppSecret)
		);
	}

	@Bean
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return socialUsersConnectionRepository();
	}

	@Bean
	@Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
	public SocialAuthenticationServiceLocator authenticationServiceLocator() {

		try {

			logger.debug("Creating social authenticators");


			SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();
			registry.addAuthenticationService(
					new FacebookAuthenticationService(
							facebookAppId,
							facebookAppSecret));

			// registry.addConnectionFactory(new
			// FacebookConnectionFactory(environment
			// .getProperty("facebook.clientId"), environment
			// .getProperty("facebook.clientSecret")));

			return registry;

		} catch (Exception e) {
			logger.error("Eror while creating social authenticators");
			return null;
		}
	}

	@Bean
	public UsersConnectionRepository socialUsersConnectionRepository() {
		JdbcUsersConnectionRepository conn = new JdbcUsersConnectionRepository(dataSource, authenticationServiceLocator(),
				textEncryptor);
		return conn;

	}


}
