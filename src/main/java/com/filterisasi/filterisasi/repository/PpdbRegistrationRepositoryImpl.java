package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;


@Service
public class PpdbRegistrationRepositoryImpl implements PpdbRegistrationRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PpdbRegistrationRepositoryImpl(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }

    private MatchOperation getMatchOperation(float minPrice, float maxPrice) {
        Criteria priceCriteria = where("price").gt(minPrice).andOperator(where("price").lt(maxPrice));
        return match(priceCriteria);
    }

    public List<PpdbRegistration> getByFirstChoice(ObjectId firstChoice) {
        System.out.println("getPpdbRegistrations");
        Query query = new Query();
        query.addCriteria(Criteria.where("jenjang_pendaftaran").is("vocational")
                .andOperator(Criteria.where("first_choice").is(firstChoice))
                //.andOperator(Criteria.where("first_choice").lt(upperBound))
        ).with(
                Sort.by(Sort.Direction.DESC, "skor_peserta").
                        and(Sort.by(Sort.Direction.ASC, "score_jarak_1"))

        );

        return mongoTemplate.find(query, PpdbRegistration.class);

    }

}
