package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.StudentComparator;
import com.filterisasi.filterisasi.repository.PpdbFilteredRepository;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import com.filterisasi.filterisasi.repository.PpdbRegistrationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MyRunner implements CommandLineRunner {

    private PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository;
    private PpdbRegistrationRepository ppdbRegistrationRepository;
    private PpdbFilteredRepository ppdbFilteredRepository;

    public MyRunner(PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository,
                    PpdbRegistrationRepository ppdbRegistrationRepository,
                    PpdbFilteredRepository ppdbFilteredRepository) {

        this.ppdbOptionLookupSchoolRepository = ppdbOptionLookupSchoolRepository;
        this.ppdbRegistrationRepository = ppdbRegistrationRepository;
        this.ppdbFilteredRepository = ppdbFilteredRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("helloo xxx");
        List<PpdbFiltered> ppdbFiltereds = new ArrayList<>();
        /*List<Users> usersList =  userRepository.findAll();
        System.out.println("userListCount:" + usersList.size());
        for (int i = 0; i <usersList.size() ; i++) {
            System.out.println("userList:" + usersList.get(i).getName());
        }*/
        List<PpdbOption> ppdbOptions = ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchool();
        System.out.println("ppdbOptions:" + ppdbOptions.size());
        /*for (int i = 0; i <ppdbOptions.size() ; i++) {
            System.out.println("ppdbSchools:" + i + "-" + ppdbOptions.get(i).get_id()  + " - " + ppdbOptions.get(i).getName() +  " - " + ppdbOptions.get(i).getPpdb_schools().get_Id() + " Add: " + ppdbOptions.get(i).getPpdb_schools().getAddress());
            //System.out.println("ppdbSchools-address:" + i + "-" + ppdbOptions.get(i).getPpdb_schools().getAddress());
        }*/


        List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice();
        List<PpdbRegistration> testPpdbRegistrations = new ArrayList<>();
        PpdbRegistration ppdbRegistration = new PpdbRegistration();
        for (int i = 0; i <100 ; i++) {
            System.out.println("ppdbRegistrations1:" + i + "-" + ppdbRegistrations.get(i).get_id()  + " - " +
                    ppdbRegistrations.get(i).getName() +  " - " +
                    ppdbRegistrations.get(i).getSkorPeserta() + " - " +
                    ppdbRegistrations.get(i).getSkorJarak1());
            ppdbRegistration = new PpdbRegistration();
            ppdbRegistration.set_id(ppdbRegistrations.get(i).get_id());
            ppdbRegistration.setName(ppdbRegistrations.get(i).getName());
            ppdbRegistration.setSkorPeserta(ppdbRegistrations.get(i).getSkorPeserta());
            ppdbRegistration.setSkorJarak1(ppdbRegistrations.get(i).getSkorJarak1());
            testPpdbRegistrations.add(ppdbRegistration);
        }
        System.out.println("==============================================================");
        /*Comparator<PpdbRegistration> comparator = Comparator.comparingDouble(PpdbRegistration::getSkorPeserta)
                .thenComparingDouble(PpdbRegistration::getSkorJarak1);
        //ppdbRegistrations.sort(comparator);

        PersonComparator comp = new PersonComparator(PersonComparator.SortOrder.DESCENDING);
        testPpdbRegistrations.sort(comp); */
        Collections.sort(testPpdbRegistrations, new StudentComparator());
        for (int i = 0; i <100 ; i++) {
            System.out.println("ppdbRegistrations2:" + i + "-" + testPpdbRegistrations.get(i).get_id()  + " - " +
                    testPpdbRegistrations.get(i).getName() +  " - " +
                    testPpdbRegistrations.get(i).getSkorPeserta() + " - " +
                    testPpdbRegistrations.get(i).getSkorJarak1());
        }

        /*
        PpdbFiltered ppdbFiltered = new PpdbFiltered();
        System.out.println("ppdbRegistrations:" + ppdbRegistrations.size());
        for (int i = 0; i <ppdbRegistrations.size() ; i++) {
            System.out.println("ppdbRegistrations2:" + i + "-" + ppdbRegistrations.get(i).get_id()  + " - " + ppdbRegistrations.get(i).getFirstChoice() + "-" + ppdbRegistrations.get(i).getCaraPendaftaran());
            ppdbFiltered = new PpdbFiltered();
            ppdbFiltered.setRegistrationId(ppdbRegistrations.get(i).get_id());
            ppdbFiltered.setOptionId(ppdbRegistrations.get(i).getFirstChoice());

            ppdbFiltereds.add(ppdbFiltered);

        }
        this.ppdbFilteredRepository.insertStudents(ppdbFiltereds);
        */


    }
}
