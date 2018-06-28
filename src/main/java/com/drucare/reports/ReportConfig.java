package com.drucare.reports;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = {"com.drucare.reports","com.drucare.app"})
@PropertySource("classpath:sql.properties")
public class ReportConfig {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(ReportConfig.class, args);
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager dsTransactionManager = new DataSourceTransactionManager(dataSource);
		return dsTransactionManager;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// registry.addMapping("/api/**")
				registry.addMapping("/**")
						// .allowedOrigins("/*")
						.allowedMethods("PUT", "DELETE", "POST", "GET")
						// .allowedHeaders("*")
						// .exposedHeaders("*")
						.allowCredentials(false).maxAge(3600);
			}
		};
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
