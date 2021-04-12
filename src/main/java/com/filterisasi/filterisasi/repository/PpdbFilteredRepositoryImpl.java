package com.filterisasi.filterisasi.repository;


import com.filterisasi.filterisasi.dto.PpdbFiltered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PpdbFilteredRepositoryImpl implements PpdbFilteredRepository {


    private final MongoTemplate mongoTemplate;

    @Autowired
    public PpdbFilteredRepositoryImpl(MongoTemplate mongoTemplate) {

        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public boolean insertStudents(List<PpdbFiltered> ppdbFiltereds) {
        System.out.println("insertStudents");
        mongoTemplate.insertAll(ppdbFiltereds);
        return true;
    }
}
