package org.example.camel.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentResource extends RepresentationModel<DocumentResource> {
	private String id;
	private String content;
}
