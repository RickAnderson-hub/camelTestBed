package org.example.camel.controller;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.camel.ProducerTemplate;
import org.example.camel.model.DocumentResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.function.Function;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class Document {

	private final ProducerTemplate producerTemplate;

	public Document(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}

	/**
	 * Generate a document
	 *
	 * @return document
	 */
	@PostMapping(value = "/")
	public ResponseEntity<DocumentResource> generate() {
		return Try.of(() -> producerTemplate.requestBody("direct:returnReceipt", null, String.class))
				.flatMap(this::createPdfForReceipt)
				.map(this::buildDocumentResponse)
				.fold(
						_ -> ResponseEntity.internalServerError().build(),
						Function.identity()
				     );
	}

	private Try<String> createPdfForReceipt(String receiptId) {
		return Try.run(() -> producerTemplate.request("direct:createPdf",
		                                              exchange -> exchange.getMessage().setHeader("receiptId", receiptId)))
				.map(_ -> receiptId);
	}

	private ResponseEntity<DocumentResource> buildDocumentResponse(String receiptId) {
		return Option.of(receiptId)
				.map(id -> {
					DocumentResource resource = new DocumentResource();
					resource.add(linkTo(methodOn(Document.class).getPdf(id)).withSelfRel());
					return resource;
				})
				.map(resource -> ResponseEntity.ok().body(resource))
				.getOrElse(() -> ResponseEntity.internalServerError().build());
	}

	/**
	 * Get PDF by id
	 *
	 * @param id receipt id
	 * @return pdf
	 */
	@GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> getPdf(@PathVariable String id) {
		return Option.of(id)
				.map(receiptId -> producerTemplate.requestBody("direct:getPdf", receiptId, byte[].class))
				.map(this::createPdfResponse)
				.getOrElse(() -> ResponseEntity.notFound().build());
	}

	private ResponseEntity<InputStreamResource> createPdfResponse(byte[] pdfData) {
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(new ByteArrayInputStream(pdfData)));
	}
}