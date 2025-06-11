package com.nutrition.mx.model.sequences;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "database_sequences")
@Data
public class DatabaseSequence {
    @Id
    private String id;

    private long seq;
}

