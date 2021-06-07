package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbSchool;
import com.mongodb.BasicDBObject;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


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

        String names[]
                = new String[] {
                            "SMK NEGERI 4 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - PERPINDAHAN",
                            "SMK NEGERI 4 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - ANAK GURU",
                            "SMK NEGERI 4 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - PRESTASI NILAI RAPOR UNGGULAN",
                            "SMK NEGERI 4 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - PRESTASI NILAI RAPOR UMUM",
                "SMK NEGERI 2 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - PERPINDAHAN",
                "SMK NEGERI 2 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - ANAK GURU",
                "SMK NEGERI 2 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - PRESTASI NILAI RAPOR UNGGULAN",
                "SMK NEGERI 2 BANDUNG - TEKNIK KOMPUTER DAN INFORMASI - PRESTASI NILAI RAPOR UMUM"
        };

        Aggregation aggregation = Aggregation.newAggregation(
                /*match(
                       new Criteria("type").is("nhun-unggulan").
                               andOperator(new Criteria("name").in(Arrays.asList(a)))

                ), */
                match(
                        new Criteria("name").in(Arrays.asList(names))

                ),
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

    @Override
    public List<PpdbOption> lookupPpdbOptionPpdbSchoolByJalur(String jalurs[]) {
        System.out.println("lookupPpdbOptionPpdbSchool");

        ObjectId schoolId[] = new ObjectId[]{
                new ObjectId("5c7fab1e4b9f621fd05374ef"),
                new ObjectId("5c7fab1e4b9f621fd05374ed"),
        };

        Aggregation aggregation = Aggregation.newAggregation(

                match(
                        new Criteria("type").in(Arrays.asList(jalurs)).
                                andOperator(new Criteria("school_id").in(Arrays.asList(schoolId)))

                ),
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
                sort(Direction.ASC, "name"),sort(Direction.ASC, "type"),
                project().andExclude("_class")
        );

        List<PpdbOption> results = mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(PpdbOption.class), PpdbOption.class).getMappedResults();

        return results;
    }

    @Override
    public int updateQuotaPpdbOption(List<PpdbOption> ppdbOptions) {
        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, PpdbOption.class);
        for (PpdbOption ppdbOption : ppdbOptions) {
            Update update = new Update();
            update.set("quota", ppdbOption.getQuota());
            ops.updateOne(Query.query(where("_id").is(ppdbOption.get_id())), update);
        }
        BulkWriteResult x = ops.execute();
        return x.getModifiedCount();

        /*
        MongoCollection collection = mongoTemplate.getCollection("collection");
        BulkOperations bulk = ;

        bulk.find(new BasicDBObject("status","D"))
                .update(new BasicDBObject(
                        new BasicDBObject(
                                "$set",new BasicDBObject(
                                "status", "I"
                        ).append(
                                "points", 0
                        )
                        )
                ));

        bulk.find(new BasicDBObject("item",null))
                .update(new BasicDBObject(
                        new BasicDBObject(
                                "$set", new BasicDBObject("item","TBD")
                        )
                ));


        BulkWriteResult writeResult = bulk.execute();
        System.out.println(writeResult); */

    }

}
