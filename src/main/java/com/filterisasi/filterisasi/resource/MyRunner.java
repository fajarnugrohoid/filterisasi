package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.*;
import com.filterisasi.filterisasi.model.PpdbHistory;
import com.filterisasi.filterisasi.module.CheckQuotaBalance;
import com.filterisasi.filterisasi.module.CutBasedOnQuota;
import com.filterisasi.filterisasi.module.InitializationStudent;
import com.filterisasi.filterisasi.repository.PpdbFilteredRepository;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import com.filterisasi.filterisasi.repository.PpdbRegistrationRepository;
import com.filterisasi.filterisasi.utility.PpdbView;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MyRunner implements CommandLineRunner {

    private PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository;
    private PpdbRegistrationRepository ppdbRegistrationRepository;
    private PpdbFilteredRepository ppdbFilteredRepository;
    private PpdbView ppdbView;
    private InitializationStudent initializationStudent;
    private FindData findData;
    private TransformData transformData;
    private CheckQuotaBalance checkQuotaBalance;
    private StudentHistory studentHistory;
    private UpdateData updateData;
    private InsertFiltered insertFiltered;
    private CutBasedOnQuota cutBasedOnQuota;
    private TransferQuota transferQuota;
    public MyRunner(PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository,
                    PpdbRegistrationRepository ppdbRegistrationRepository,
                    PpdbFilteredRepository ppdbFilteredRepository
                    ) {

        this.ppdbOptionLookupSchoolRepository = ppdbOptionLookupSchoolRepository;
        this.ppdbRegistrationRepository = ppdbRegistrationRepository;
        this.ppdbFilteredRepository = ppdbFilteredRepository;
        this.ppdbView = new PpdbView();
        this.initializationStudent = new InitializationStudent(ppdbRegistrationRepository);
        this.findData = new FindData();
        this.transformData = new TransformData();
        this.updateData = new UpdateData();
        this.studentHistory = new StudentHistory(findData, updateData);
        this.checkQuotaBalance = new CheckQuotaBalance(this.findData, this.studentHistory, this.ppdbView);
        this.insertFiltered = new InsertFiltered(ppdbOptionLookupSchoolRepository, ppdbRegistrationRepository, ppdbFilteredRepository);
        this.cutBasedOnQuota = new CutBasedOnQuota(this.findData, this.studentHistory, this.checkQuotaBalance, this.updateData);
        this.transferQuota = new TransferQuota(this.findData);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("run filter ppdb SMA...");
        List<PpdbFiltered> ppdbFiltereds = new ArrayList<>();
        List<PpdbOption> ppdbOptions = new ArrayList<>();
        String jalur1 = "perpindahan";
        String jalur2 = "anak-guru";
        String jalur3 = "nhun";
        String jalur4 = "nhun-unggulan";
        String jalurs[] = new String[] { jalur1, jalur2, jalur3, jalur4};
        ppdbOptions.addAll(ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchoolByJalur(jalurs));

        ppdbOptions = initializationStudent.initializationRegistrationOptionsAndStudents(ppdbOptions);

        ppdbOptions = initializationStudent.initializationOutcastOptionsAndStudents(ppdbOptions);

        //List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice();
        ppdbView.displayOption(ppdbOptions);
        System.out.println("==============================================================");
        this.cutBasedOnQuota.potongBerdasarkanQuota(ppdbOptions, jalur1, jalur2);
        //pelimpahanQuota(ppdbOptions);
        System.out.println("==============================================================");
        //displayStudent(ppdbOptions);
        ppdbView.displayOption(ppdbOptions);
        this.transferQuota.checkIfAnyRemainingQuota(ppdbOptions, jalur1, jalur2);
        //System.out.println("==============================================================");
        //ppdbView.displayOption(ppdbOptions);
        this.cutBasedOnQuota.potongBerdasarkanQuota(ppdbOptions, jalur3, jalur4);
        ppdbView.displayOption(ppdbOptions);
        this.insertFiltered.insertFilteredStudent(ppdbOptions);
    }








}
