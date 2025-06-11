package com.nutrition.mx.utils;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.nutrition.mx.model.sequences.DatabaseSequence;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    public String generateSequence(String seqName) {
    	DatabaseSequence counter = mongoOperations.findAndModify(
    	        Query.query(Criteria.where("_id").is(seqName)),
    	        new Update().inc("seq", 1),
    	        FindAndModifyOptions.options().returnNew(true).upsert(true),
    	        DatabaseSequence.class);

    	    long nextSeq = counter != null ? counter.getSeq() : 1;

    	    // Formatea el número con ceros a la izquierda (10 dígitos)
    	    return String.format("%010d", nextSeq);
    }
}

