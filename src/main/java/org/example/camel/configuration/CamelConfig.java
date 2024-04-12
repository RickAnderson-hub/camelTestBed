package org.example.camel.configuration;

import org.springframework.context.annotation.Configuration;
import org.apache.camel.builder.RouteBuilder;
import org.example.camel.service.CreatePdf;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class CamelConfig extends RouteBuilder {

    @Autowired
    private CreatePdf createPdf;

    @Override
    public void configure() throws Exception {
        from("direct:createPdf")
                .process(createPdf);
    }
}
