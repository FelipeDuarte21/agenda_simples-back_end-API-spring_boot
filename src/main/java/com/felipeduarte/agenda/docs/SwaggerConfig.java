package com.felipeduarte.agenda.docs;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.google.common.net.HttpHeaders;

import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport{
	
	@Value("${swagger.base.path}")
	private String basePath;

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).host(basePath)
          .select()
          .apis(RequestHandlerSelectors.basePackage("com.felipeduarte.agenda.resource"))
          .paths(PathSelectors.any())
          .build().useDefaultResponseMessages(false)
          .securitySchemes(Arrays.asList(new ApiKey("Token Acess", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
          .securityContexts(Arrays.asList(securityContext()))
          .apiInfo(metaData());
    }
	
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("API Agenda Simples de Contatos")
				.description("API backend do projeto de agenda simples de contatos")
				.version("1.0")
				.license("Licença Pública Geral GNU")
				.licenseUrl("https://www.gnu.org/licenses/gpl-3.0.html")
				.contact(new Contact("Felipe Duarte", "", "felipe15lfde@gmail.com"))
				.build();
	}
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
		.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
		.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }

	
	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	        .securityReferences(defaultAuth())
	        .forPaths(PathSelectors.ant("/api/**"))
	        .build();
	}

	List<SecurityReference> defaultAuth() {
	    AuthorizationScope authorizationScopeAdmin
	        = new AuthorizationScope("ADMIN", "accessEverything");
	    AuthorizationScope authorizationScopeUser
        	= new AuthorizationScope("USER", "accessEverything");
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
	    authorizationScopes[0] = authorizationScopeAdmin;
	    authorizationScopes[1] = authorizationScopeUser;
	    return Arrays.asList(
	        new SecurityReference("Token Access", authorizationScopes));
	}
	
}
