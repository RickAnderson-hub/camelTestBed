package org.example.camel.routes;

import io.vavr.control.Either;
import org.apache.camel.builder.RouteBuilder;
import org.example.camel.dto.PdfError;
import org.example.camel.exceptions.DocumentNotFoundException;
import org.example.camel.exceptions.ReceiptNotFoundException;
import org.example.camel.service.CreatePdf;
import org.example.camel.service.GetPdf;
import org.example.camel.service.ReturnReceipt;
import org.springframework.stereotype.Component;

/**
 * Camel routes using functional DSL with lambda expressions
 */
@Component
public class PdfRoute extends RouteBuilder {

	private final ReturnReceipt returnReceipt;
	private final CreatePdf createPdf;
	private final GetPdf getPdf;

	public PdfRoute(ReturnReceipt returnReceipt, CreatePdf createPdf, GetPdf getPdf) {
		this.returnReceipt = returnReceipt;
		this.createPdf = createPdf;
		this.getPdf = getPdf;
	}

	@Override
	public void configure() {
		from("direct:returnReceipt")
				.routeId("returnReceiptProcess")
				.process(exchange -> {
					String receiptId = returnReceipt.get()
							.getOrElseThrow(e -> new RuntimeException("Failed to generate receipt", e));
					exchange.getIn().setBody(receiptId);
				});

		from("direct:createPdf")
				.routeId("createPdfProcess")
				.process(exchange -> {
					String receiptId = exchange.getIn().getHeader("receiptId", String.class);
					createPdf.apply(receiptId)
							.getOrElseThrow(e -> new RuntimeException("Failed to create PDF", e));
				});

		from("direct:getPdf")
				.routeId("getPdfProcess")
				.process(exchange -> {
					String receiptId = exchange.getIn().getBody(String.class);
					Either<PdfError, byte[]> result = getPdf.apply(receiptId);
					byte[] pdfData = result.getOrElseThrow(this::mapPdfErrorToException);
					exchange.getIn().setBody(pdfData);
				});
	}

	private RuntimeException mapPdfErrorToException(PdfError error) {
		return switch (error) {
			case PdfError.DocumentNotFound e -> new DocumentNotFoundException(e.message());
			case PdfError.ReceiptNotFound e -> new ReceiptNotFoundException(e.message());
			case PdfError.PdfCreationFailed e -> new RuntimeException(e.message(), e.cause());
		};
	}
}
