package org.example.camel.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.example.camel.service.CreatePdf;
import org.example.camel.service.ReturnReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig extends RouteBuilder {

    @Autowired
    private CreatePdf createPdf;

    @Autowired
    private ReturnReceipt returnReceipt;

    @Override
    public void configure() {
        from("direct:createPdf")
                .process(createPdf);
        from("direct:returnReceipt")
                .process(returnReceipt);
    }
}
