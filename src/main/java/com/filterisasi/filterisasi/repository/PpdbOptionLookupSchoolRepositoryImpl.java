package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbSchool;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
public class PpdbOptionLookupSchoolRepositoryImpl implements PpdbOptionLookupSchoolRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PpdbOptionLookupSchoolRepositoryImpl(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PpdbOption> lookupPpdbOptionPpdbSchool() {
        System.out.println("lookupPpdbOptionPpdbSchool");



        Aggregation aggregation = Aggregation.newAggregation(
                match(new Criteria("type").is("nhun")),
                //lookup("users", "postedBy", "_id", "user")
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
                unwind("$ppdb_schools"), /*
                new AggregationOperation() {

                    @Override
                    public Document toDocument(AggregationOperationContext context) {
                        return new Document("$addFields",
                                new Document("id", new Document("$toString", "$_id"))
                                        .append("name", "$ppdb_school.name")
                                        /*.append("upvotes", new Document("$size", "$upvotesBy"))
                                        .append("isUpvoted", new Document("$in", Arrays.asList(userId, "$upvotesBy")))
                                        .append("isPinned", new Document("$cond",
                                                Arrays.asList(new Document("$gte",
                                                        Arrays.asList(new Document("$size", "$upvotesBy"), 3)), Boolean.TRUE, Boolean.FALSE)))
                                        .append("createdAt", new Document("$dateToString",
                                                new Document("format", "%H:%M %d-%m-%Y")
                                                        .append("timezone", "+01")
                                                        .append("date", "$createdAt")
                                        ))
                                );
                    }
                },*/
                sort(Direction.DESC, "createdAt"),
                project().andExclude("_class")
        );

        System.out.println("Aggregation: " + aggregation.toString());

        List<PpdbOption> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(PpdbOption.class), PpdbOption.class).getMappedResults();

        System.out.println("results:" + results.size());
        return results;
    }

}
