package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.dto.PpdbSchool;
import com.filterisasi.filterisasi.lib.StudentComparator;
import com.filterisasi.filterisasi.repository.PpdbFilteredRepository;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import com.filterisasi.filterisasi.repository.PpdbRegistrationRepository;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.binary.Hex;


import java.util.ArrayList;
import java.util.Arrays;
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
        List<PpdbOption> ppdbOptions = new ArrayList<>();
        ppdbOptions.addAll(ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchool());

        System.out.println("ppdbOptions:" + ppdbOptions.size());
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("ppdbSchools:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                    " Add: " + ppdbOptions.get(iOpt).getPpdb_schools().getAddress());
            //System.out.println("ppdbSchools-address:" + i + "-" + ppdbOptions.get(i).getPpdb_schools().getAddress());
            List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice(ppdbOptions.get(iOpt).get_id());


            List<PpdbRegistration> testPpdbRegistrations = new ArrayList<>();
            PpdbRegistration ppdbRegistration = new PpdbRegistration();
            for (int std = 0; std <ppdbRegistrations.size() ; std++) {

                System.out.println("ppdbRegistrations1:" + std + "-" + ppdbRegistrations.get(std).get_id()  + " - " +
                        ppdbRegistrations.get(std).getName() +  " - " +
                        ppdbRegistrations.get(std).getSkorPeserta() + " - " +
                        ppdbRegistrations.get(std).getSkorJarak1());
                ppdbRegistration = new PpdbRegistration();
                ppdbRegistration.set_id(ppdbRegistrations.get(std).get_id());
                ppdbRegistration.setName(ppdbRegistrations.get(std).getName());
                ppdbRegistration.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
                ppdbRegistration.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
                ppdbRegistration.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
                ppdbRegistration.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
                ppdbRegistration.setChoiceIteration(0); //diterima dipilihan 1
                testPpdbRegistrations.add(ppdbRegistration);
            }
            ppdbOptions.get(iOpt).setPpdbRegistrationList(testPpdbRegistrations);

            System.out.println("===============================================================================");
        }


        String foo = "SekolahBuangan";
        byte[] bytes = foo.getBytes();
        //ObjectId idSekolahBuangan = new ObjectId(Hex.encodeHexString( bytes ) );
        ObjectId idSekolahBuangan = new ObjectId("5eec2ca2b2e4ce405929310f");
        String nameSekolahBuangan = "SekolahBuangan";
        String typeSekolahBuangan = "";
        Integer rombelSekolahBuangan = 0;
        Integer quotaSekolahBuangan = 0;
        Integer quotaForignerSekolahBuangan = 0;
        Integer totalQuotaSekolahBuangan = 0;
        boolean filteredSekolahBuangan = false;

        List<PpdbRegistration> siswaBuangan = new ArrayList<>();
        PpdbOption sekolahBuangan = new PpdbOption();
        sekolahBuangan.setPpdbRegistrationList(siswaBuangan);
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
        sekolahBuangan.setSchool_id("5c7fab1e4b9f621fd05374eb");
        sekolahBuangan.setTotal_quota(totalQuotaSekolahBuangan);
        sekolahBuangan.setType("type");
        sekolahBuangan.setFiltered(filteredSekolahBuangan);
        ppdbOptions.add(sekolahBuangan);


        //List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice();

        System.out.println("==============================================================");
        potongBerdasarkanQuota(ppdbOptions);
        System.out.println("==============================================================");
        displayStudent(ppdbOptions);
        /*
        Collections.sort(testPpdbRegistrations, new StudentComparator());
        for (int i = 0; i <100 ; i++) {
            System.out.println("ppdbRegistrations2:" + i + "-" + testPpdbRegistrations.get(i).get_id()  + " - " +
                    testPpdbRegistrations.get(i).getName() +  " - " +
                    testPpdbRegistrations.get(i).getSkorPeserta() + " - " +
                    testPpdbRegistrations.get(i).getSkorJarak1());
        }
        */

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

    private void potongBerdasarkanQuota(List<PpdbOption> ppdbOptions){

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat


            int jumlahPendaftar = ppdbOptions.get(iOpt).getPpdbRegistrationList().size();
            int quotaOption = ppdbOptions.get(iOpt).getQuota();

            System.out.println( iOpt + "jumlahPendaftar:" + jumlahPendaftar + " > " + quotaOption);

            List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
            Collections.sort(students, new StudentComparator());
            if (jumlahPendaftar > quotaOption){
                    //potong

                for (int iStd = students.size()-1; iStd>quotaOption ; iStd--) {
                    System.out.println("option:" + iOpt + " - " + students.get(iStd).get_id() +
                            " sec:" + students.get(iStd).getSecondChoice());
                    Integer idxTargetOption = null;
                    if (students.get(iStd).getChoiceIteration()==0){ //saat ini posisi siswa tidak diterima pilihan 1, maka ambil informasi skeolah pilihan 2
                        idxTargetOption = findUsingEnhancedForLoop(students.get(iStd).getSecondChoice(),ppdbOptions);
                        students.get(iStd).setChoiceIteration(1);
                    }else if (students.get(iStd).getChoiceIteration()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                        //harusnya ke option pilihan swasta
                        idxTargetOption = ppdbOptions.size()-1;
                    }else{
                        idxTargetOption = ppdbOptions.size()-1;
                    }
                    ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().add(students.get(iStd)); //tambahkan siswa ke sekolah pilihan selanjutnya
                    ppdbOptions.get(idxTargetOption).setFiltered(true); //rubah status filtered=true sekolah yang dapat limpahan siswa
                    students.remove(iStd); //hapus siswa
                }
                ppdbOptions.get(iOpt).setFiltered(false);
            }

            ppdbOptions.get(iOpt).setPpdbRegistrationList(students);
            ppdbOptions.set(iOpt, ppdbOptions.get(iOpt));
        }

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat
            if (ppdbOptions.get(iOpt).isFiltered()==true){
                potongBerdasarkanQuota(ppdbOptions);
                break;
            }
        }
    }

    public Integer findUsingEnhancedForLoop(ObjectId nextChoice, List<PpdbOption> ppdbOptions) {

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (ppdbOptions.get(iOpt).get_id().equals(nextChoice)) {
                return iOpt;
            }
        }
        return ppdbOptions.size()-1; //lempar ke sekolah buangan
    }

    private void displayStudent(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("ppdbOption:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id());

            List<PpdbRegistration> ppdbRegistrations = ppdbOptions.get(iOpt).getPpdbRegistrationList();
            for (int std = 0; std <ppdbRegistrations.size() ; std++) {

                System.out.println("ppdbRegistrations1:" + std + "-" + ppdbRegistrations.get(std).get_id() + " - " +
                        ppdbRegistrations.get(std).getName() + " - " +
                        ppdbRegistrations.get(std).getSkorPeserta() + " - " +
                        ppdbRegistrations.get(std).getSkorJarak1());
            }
        }
    }


}
