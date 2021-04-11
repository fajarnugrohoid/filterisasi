package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
public class PpdbRegistrationRepositoryImpl implements PpdbRegistrationRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PpdbRegistrationRepositoryImpl(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PpdbRegistration> getPpdbRegistrations() {
        System.out.println("lookupPpdbOptionPpdbSchool");



        Aggregation aggregation = Aggregation.newAggregation(
                match(new Criteria("type").is("nhun")),
                new AggregationOperation() {
                    @Override
                    public Document toDocument(AggregationOperationContext context) {
                        return new Document("$lookup",
                                new Document("from", "ppdb_schools")
                                        .append("let", new Document("school_id", "$school_id"))
                                        .append("pipeline", Arrays.asList(
                                                new Document("$match",
                                                        new Document("$expr",
                                                                new Document(
                                                                        "$eq", Arrays.asList("$_id", "$$school_id")
                                                                )
                                                        ))))
                                        .append("as", "ppdb_schools"));
                    }
                },
                unwind("$ppdb_schools"),
                sort(Direction.DESC, "createdAt"),
                project().andExclude("_class")
        );

        System.out.println("Aggregation: " + aggregation.toString());

        List<PpdbRegistration> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(PpdbRegistration.class), PpdbRegistration.class).getMappedResults();

        System.out.println("results:" + results.size());
        return results;
    }

}
