package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.repository.PpdbFilteredRepository;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import com.filterisasi.filterisasi.repository.PpdbRegistrationRepository;

import java.util.ArrayList;
import java.util.List;

public class InsertFiltered {

    private PpdbRegistrationRepository ppdbRegistrationRepository;
    private PpdbFilteredRepository ppdbFilteredRepository;
    private PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository;

    public InsertFiltered(PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository, PpdbRegistrationRepository ppdbRegistrationRepository, PpdbFilteredRepository ppdbFilteredRepository) {
        this.ppdbOptionLookupSchoolRepository = ppdbOptionLookupSchoolRepository;
        this.ppdbFilteredRepository = ppdbFilteredRepository;
        this.ppdbRegistrationRepository = ppdbRegistrationRepository;
    }

    public void insertFilteredStudent(List<PpdbOption> ppdbOptions){
        for (int optionIdx = 0; optionIdx <ppdbOptions.size() ; optionIdx++) {

            List<PpdbFiltered> ppdbFiltereds = new ArrayList<>();
            List<PpdbRegistration> ppdbRegistrations = ppdbOptions.get(optionIdx).getPpdbRegistrationList();
            for (int stdIdx = 0; stdIdx <ppdbRegistrations.size() ; stdIdx++) {
                System.out.println("ppdbRegistrations:" + stdIdx + "-" + ppdbRegistrations.get(stdIdx).get_id() + " - " +
                        ppdbRegistrations.get(stdIdx).getName() + " - " +
                        ppdbRegistrations.get(stdIdx).getSkorPeserta() + " - " +
                        ppdbRegistrations.get(stdIdx).getSkorJarak1() + " - " + ppdbRegistrations.get(stdIdx).getAcceptedOptionNo());

                PpdbFiltered ppdbFiltered = new PpdbFiltered();
                ppdbFiltered.setOptionId(ppdbRegistrations.get(stdIdx).getAcceptedOptionId());
                ppdbFiltered.setRegistrationId(ppdbRegistrations.get(stdIdx).get_id());
                //ppdbFiltered.setSchoolId();
                ppdbFiltered.setLevel(ppdbRegistrations.get(stdIdx).getJenjangPendaftaran());
                ppdbFiltered.setType(ppdbRegistrations.get(stdIdx).getLevelPendaftaran());
                ppdbFiltereds.add(ppdbFiltered);

            }
            ppdbOptionLookupSchoolRepository.updateQuotaPpdbOption(ppdbOptions);
            ppdbFilteredRepository.insertStudents(ppdbFiltereds);

        }
    }
}
