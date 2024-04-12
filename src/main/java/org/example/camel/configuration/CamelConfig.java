package org.example.camel.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.example.camel.service.CreatePdf;
import org.example.camel.service.GetPdf;
import org.example.camel.service.ReturnReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig extends RouteBuilder {

    @Autowired
    private ReturnReceipt returnReceipt;

    @Autowired
    private CreatePdf createPdf;

    @Autowired
    private GetPdf getPdf;

    @Override
    public void configure() {
        from("direct:returnReceipt").bean(returnReceipt).id("returnReceiptProcess").end();
        from("direct:createPdf").process(createPdf).id("createPdfProcess").end();
        from("direct:getPdf").process(getPdf).id("getPdfProcess").end();
    }
}
