package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PpdbFilteredRepository {

    boolean insertStudents(List<PpdbFiltered> ppdbFiltereds);
}
