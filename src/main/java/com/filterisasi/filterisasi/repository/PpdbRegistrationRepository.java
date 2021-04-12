package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PpdbRegistrationRepository  {

    List<PpdbRegistration> getByFirstChoice();

}
