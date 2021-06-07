package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.lib.*;
import com.filterisasi.filterisasi.module.*;
import com.filterisasi.filterisasi.repository.PpdbFilteredRepository;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import com.filterisasi.filterisasi.repository.PpdbRegistrationRepository;
import com.filterisasi.filterisasi.utility.PpdbView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class RunnerSenior implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RunnerSenior.class);

    private PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository;
    private PpdbRegistrationRepository ppdbRegistrationRepository;
    private PpdbFilteredRepository ppdbFilteredRepository;
    private PpdbView ppdbView;
    private InitializationStudent initializationStudent;
    private FindData findData;
    private TransformData transformData;

    private CheckQuotaBalance checkQuotaBalance;
    private CheckQuotaBalanceTargetMoreThanOne checkQuotaBalanceTargetMoreThanOne;

    private StudentHistory studentHistory;
    private UpdateData updateData;
    private InsertFiltered insertFiltered;

    private CutBasedOnQuotaShareToOneOption cutBasedOnQuotaShareToOneOption;
    private CutBasedOnQuotaTargetMoreThanOne cutBasedOnQuotaTargetMoreThanOne;
    private CutBasedOnQuotaWithOutShareQuota cutBasedOnQuotaWithOutShareQuota;

    private TransferQuotaFromTwoToOne transferQuotaFromTwoToOne;
    private TransferQuotaFromOneToOne transferQuotaFromOneToOne;
    public RunnerSenior(PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository,
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
        this.checkQuotaBalanceTargetMoreThanOne = new CheckQuotaBalanceTargetMoreThanOne(this.findData, this.studentHistory, this.ppdbView);
        this.insertFiltered = new InsertFiltered(ppdbOptionLookupSchoolRepository, ppdbRegistrationRepository, ppdbFilteredRepository);
        this.cutBasedOnQuotaShareToOneOption = new CutBasedOnQuotaShareToOneOption(this.findData, this.studentHistory, this.checkQuotaBalance, this.updateData);
        this.cutBasedOnQuotaTargetMoreThanOne = new CutBasedOnQuotaTargetMoreThanOne(this.findData, this.studentHistory, this.checkQuotaBalanceTargetMoreThanOne, this.updateData);
        this.cutBasedOnQuotaWithOutShareQuota = new CutBasedOnQuotaWithOutShareQuota(this.findData, this.studentHistory, this.checkQuotaBalance, this.updateData, this.ppdbView);
        this.transferQuotaFromTwoToOne = new TransferQuotaFromTwoToOne(this.findData);
        this.transferQuotaFromOneToOne = new TransferQuotaFromOneToOne(this.findData);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("run filter ppdb SMA...");
        List<PpdbFiltered> ppdbFiltereds = new ArrayList<>();
        List<PpdbOption> ppdbOptions = new ArrayList<>();

        String jalur0 = "abk";
        String jalur1 = "kondisi-tertentu";
        String jalur2 = "ketm";

        String jalur3 = "perpindahan";

        String jalur4 = "prestasi-rapor";
        String jalur5 = "prestasi";

        String jalurs[] = new String[] {jalur0, jalur1, jalur2, jalur3, jalur4, jalur5};
        ppdbOptions.addAll(ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchoolByJalur(jalurs));

        ppdbOptions = initializationStudent.initializationRegistrationOptionsAndStudents(ppdbOptions);

        ppdbOptions = initializationStudent.initializationOutcastOptionsAndStudents(ppdbOptions);

        //List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice();

        ppdbView.displayOption(ppdbOptions);
        System.out.println("==============================================================");

        //filter jalur abk
        this.cutBasedOnQuotaWithOutShareQuota.potongBerdasarkanQuotaTanpaBerbagiQuota(ppdbOptions, jalur0);

        //sisa quota abk dikirim ke ketm
        this.transferQuotaFromOneToOne.checkIfAnyRemainingQuota(ppdbOptions, jalur0, jalur2);


        System.out.println("==============================================================");
        //filter ketm dan kondisi-tertentu sekaligus berbagi quota
        this.cutBasedOnQuotaShareToOneOption.potongBerdasarkanQuota(ppdbOptions, jalur1, jalur2);
        //ppdbView.displayOption(ppdbOptions);

        System.out.println("==============================================================");
        //ppdbView.displayOption(ppdbOptions);
        //jika ada sisa quota dari ketm dan kondisi-tertentu, maka limpahkan ke jalur perpindahan
        this.transferQuotaFromTwoToOne.checkIfAnyRemainingQuota(ppdbOptions, jalur1, jalur2, jalur3);
        System.out.println("==============================================================");
        //ppdbView.displayOption(ppdbOptions);

        //transfer quota dari jalur perpindahan ke jalur prestasi-rapor
        this.transferQuotaFromOneToOne.checkIfAnyRemainingQuota(ppdbOptions, jalur3, jalur4);

        //filter prestasi dan prestasi rapor sekaligus berbagi quota
        this.cutBasedOnQuotaShareToOneOption.potongBerdasarkanQuota(ppdbOptions, jalur4, jalur5);
        ppdbView.displayOption(ppdbOptions);
        this.insertFiltered.insertFilteredStudent(ppdbOptions);
    }








}
