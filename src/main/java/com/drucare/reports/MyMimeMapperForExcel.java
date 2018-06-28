/*package com.drucare.reports;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MyMimeMapperForExcel implements EmbeddedServletContainerCustomizer {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		mappings.add("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		container.setMimeMappings(mappings);
	}
}
*/