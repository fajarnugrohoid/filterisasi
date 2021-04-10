package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyRunner implements CommandLineRunner {

    private PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository;

    public MyRunner(PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository) {

        this.ppdbOptionLookupSchoolRepository = ppdbOptionLookupSchoolRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("helloo xxx");
        /*List<Users> usersList =  userRepository.findAll();
        System.out.println("userListCount:" + usersList.size());
        for (int i = 0; i <usersList.size() ; i++) {
            System.out.println("userList:" + usersList.get(i).getName());
        }*/
        List<PpdbOption> ppdbOptions = ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchool();
        System.out.println("ppdbOptions:" + ppdbOptions.size());
        for (int i = 0; i <ppdbOptions.size() ; i++) {
            System.out.println("ppdbSchools:" + i + "-" + ppdbOptions.get(i).getName());
        }
    }
}
