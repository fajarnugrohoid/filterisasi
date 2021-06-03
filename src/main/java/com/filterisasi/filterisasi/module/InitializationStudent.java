package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.dto.PpdbSchool;
import com.filterisasi.filterisasi.model.PpdbHistory;
import com.filterisasi.filterisasi.repository.PpdbRegistrationRepository;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class InitializationStudent {

    PpdbRegistrationRepository ppdbRegistrationRepository;

    public InitializationStudent(PpdbRegistrationRepository ppdbRegistrationRepository) {
        this.ppdbRegistrationRepository = ppdbRegistrationRepository;
    }

    public List<PpdbOption> initializationRegistrationOptionsAndStudents(List<PpdbOption> ppdbOptions){
        System.out.println("ppdbOptions:" + ppdbOptions.size());
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("ppdbSchools:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                    " Add: " + ppdbOptions.get(iOpt).getPpdb_schools().getAddress());

            List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice(ppdbOptions.get(iOpt).get_id());

            List<PpdbRegistration> ppdbRegistrationList = setInitilizationRegistrationStudents(ppdbOptions, iOpt, ppdbRegistrations);
            List<PpdbHistory> ppdbRegistrationHistoryList = setInitilizationHistoryStudents(ppdbOptions, iOpt, ppdbRegistrations);

            ppdbOptions.get(iOpt).setPpdbRegistrationList(ppdbRegistrationList);
            ppdbOptions.get(iOpt).setPpdbRegistrationHistories(ppdbRegistrationHistoryList);

            System.out.println("===============================================================================");
        }
        return ppdbOptions;
    }

    public List<PpdbOption> initializationOutcastOptionsAndStudents(List<PpdbOption> ppdbOptions){
        String foo = "SekolahBuangan";

        ObjectId idSekolahBuangan = new ObjectId("5eec2ca2b2e4ce405929310f");
        String nameSekolahBuangan = "SekolahBuangan";
        String typeSekolahBuangan = "";
        int rombelSekolahBuangan = 0;
        int quotaSekolahBuangan = 0;
        int quotaForignerSekolahBuangan = 0;
        int totalQuotaSekolahBuangan = 0;
        boolean filteredSekolahBuangan = false;

        List<PpdbRegistration> siswaBuangan = new ArrayList<>();
        List<PpdbHistory> siswaHistoryBuangan = new ArrayList<>();
        PpdbOption sekolahBuangan = new PpdbOption();

        sekolahBuangan.setPpdbRegistrationList(siswaBuangan);
        sekolahBuangan.setPpdbRegistrationHistories(siswaHistoryBuangan);
        sekolahBuangan.set_id(idSekolahBuangan);
        sekolahBuangan.setName(nameSekolahBuangan);
        PpdbSchool tempSchool = new PpdbSchool();
        tempSchool.setId(new ObjectId("5c7fab1e4b9f621fd05374eb"));
        tempSchool.setType("open");
        tempSchool.setLevel("vocational");
        tempSchool.setCode("400232");
        tempSchool.setName("SekolahBuangan");
        tempSchool.setAddress("Jl.");
        sekolahBuangan.setPpdb_schools(tempSchool);
        sekolahBuangan.setQuota(quotaSekolahBuangan);
        sekolahBuangan.setQuota_foreigner(quotaForignerSekolahBuangan);
        sekolahBuangan.setRombel(rombelSekolahBuangan);
        sekolahBuangan.setSchoolId(new ObjectId("5c7fab1e4b9f621fd05374eb"));
        sekolahBuangan.setTotal_quota(totalQuotaSekolahBuangan);
        sekolahBuangan.setType("type");
        sekolahBuangan.setNeedFilter(filteredSekolahBuangan);
        ppdbOptions.add(sekolahBuangan);
        return ppdbOptions;
    }

    public List<PpdbRegistration> setInitilizationRegistrationStudents(List<PpdbOption> ppdbOptions, int optionIdx,  List<PpdbRegistration> ppdbRegistrations){
        List<PpdbRegistration> ppdbRegistrationList = new ArrayList<>();
        PpdbRegistration ppdbRegistration = new PpdbRegistration();

        for (int std = 0; std <ppdbRegistrations.size() ; std++) {
            ppdbRegistration = new PpdbRegistration();
            ppdbRegistration.set_id(ppdbRegistrations.get(std).get_id());
            ppdbRegistration.setName(ppdbRegistrations.get(std).getName());

            ppdbRegistration.setJenjangPendaftaran(ppdbRegistrations.get(std).getJenjangPendaftaran());
            ppdbRegistration.setLevelPendaftaran(ppdbRegistrations.get(std).getLevelPendaftaran());

            ppdbRegistration.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
            ppdbRegistration.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
            ppdbRegistration.setThirdChoice(ppdbRegistrations.get(std).getThirdChoice());

            ppdbRegistration.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
            ppdbRegistration.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
            ppdbRegistration.setPriority(ppdbRegistrations.get(std).getPriority());

            ppdbRegistration.setAcceptedOptionNo(0); //diterima dipilihan 1
            ppdbRegistration.setAcceptedOptionId(ppdbOptions.get(optionIdx).get_id());
            ppdbRegistration.setStudentExist(true);
            ppdbRegistrationList.add(ppdbRegistration);
        }
        return ppdbRegistrationList;
    }

    public List<PpdbHistory> setInitilizationHistoryStudents(List<PpdbOption> ppdbOptions, int optionIdx,  List<PpdbRegistration> ppdbRegistrations){
        List<PpdbHistory> ppdbRegistrationHistories = new ArrayList<>();
        PpdbHistory ppdbRegistrationHistory = new PpdbHistory();

        for (int std = 0; std <ppdbRegistrations.size() ; std++) {
            ppdbRegistrationHistory = new PpdbHistory();
            ppdbRegistrationHistory.set_id(ppdbRegistrations.get(std).get_id());
            ppdbRegistrationHistory.setName(ppdbRegistrations.get(std).getName());

            ppdbRegistrationHistory.setJenjangPendaftaran(ppdbRegistrations.get(std).getJenjangPendaftaran());
            ppdbRegistrationHistory.setLevelPendaftaran(ppdbRegistrations.get(std).getLevelPendaftaran());

            ppdbRegistrationHistory.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
            ppdbRegistrationHistory.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
            ppdbRegistrationHistory.setThirdChoice(ppdbRegistrations.get(std).getThirdChoice());

            ppdbRegistrationHistory.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
            ppdbRegistrationHistory.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
            ppdbRegistrationHistory.setPriority(ppdbRegistrations.get(std).getPriority());

            ppdbRegistrationHistory.setAcceptedOptionNo(0); //diterima dipilihan 1
            ppdbRegistrationHistory.setAcceptedOptionId(ppdbOptions.get(optionIdx).get_id());
            ppdbRegistrationHistory.setStudentExist(true);

            ppdbRegistrationHistories.add(ppdbRegistrationHistory);
        }
        return ppdbRegistrationHistories;
    }

}
