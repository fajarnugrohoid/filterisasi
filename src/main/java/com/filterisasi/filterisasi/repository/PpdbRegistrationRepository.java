package com.filterisasi.filterisasi.repository;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PpdbRegistrationRepository {

    List<PpdbRegistration> getPpdbRegistrations();

}
