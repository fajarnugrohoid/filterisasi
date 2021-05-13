package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PpdbOptionLookupSchoolRepository {

    List<PpdbOption> lookupPpdbOptionPpdbSchool();
    List<PpdbOption> lookupPpdbOptionPpdbSchoolByJalur(String jalurs[]);

}
