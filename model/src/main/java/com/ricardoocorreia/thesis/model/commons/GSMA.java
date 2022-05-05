package com.ricardoocorreia.thesis.model.commons;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;
import java.util.List;

@Value
@Builder
public class GSMA implements Serializable {

    URI id;

    Instant dateCreated;

    Instant dateModified;

    String source;

    String name;

    String alternateName;

    String description;

    String dataProvider;

    List<URI> owner;
}
