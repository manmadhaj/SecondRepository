package com.drucare.reports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket loginRegApi() {

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.drucare")).paths(PathSelectors.any()).build()
				.apiInfo(metaData());

	}

	private ApiInfo metaData() {
		String title = "Report module relatedApis";
		String description = "Report Module API  ";
		String version = "1.0";
		String termsOfServiceUrl = "";
		String devContact = "Srinivas Nangana, http://dru.care/, srinivas.n@dru.care & Srikanth Gaddamwar,http://dru.care/, srikanth.g@dru.care";
		String license = "Apache License Version 2.0";
		String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";

		@SuppressWarnings("deprecation")
		ApiInfo apiInfo = new ApiInfo(title, description, version, termsOfServiceUrl, devContact, license, licenseUrl);
		return apiInfo;
	}

}