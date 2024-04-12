package org.example.camel.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class DocumentResource extends RepresentationModel<DocumentResource> {
    private String id;
    private String content;
}
