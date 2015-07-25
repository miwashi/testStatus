package com.miwashi.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
public class SwaggerConfig {

	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
				.apiInfo(apiInfo())
				.apiVersion(getApiVersion())
				.includePatterns(
						ApplicationConfig.getVersionedEndpointURL("/requirement.*")
                        ,ApplicationConfig.getVersionedEndpointURL("/settings.*")
                        ,"/requirement.*"
                        ,"/settings.*"
                        ,"/requirements.*"
                        ,"/group.*"
                        ,"/meta.*"
                        //,"/users.*"
                        //,"/.*"
						);
	}

	private SpringSwaggerConfig springSwaggerConfig;

	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("Requirement Status", "A HTTP/JSON API for requirements",
				"", // API terms of service
				"", // API Contact Email
				"", // API Licence Type
				""  // API License URL
		);
		return apiInfo;
	}

	private String getApiVersion() {
		return "0.1";
	}
}
