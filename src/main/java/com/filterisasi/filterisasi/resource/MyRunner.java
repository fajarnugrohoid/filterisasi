package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.dto.PpdbSchool;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentComparator;
import com.filterisasi.filterisasi.module.InitializationOutcast;
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
    private InitializationOutcast initializationOutcast;
    private FindData findData;
    public MyRunner(PpdbOptionLookupSchoolRepository ppdbOptionLookupSchoolRepository,
                    PpdbRegistrationRepository ppdbRegistrationRepository,
                    PpdbFilteredRepository ppdbFilteredRepository
                    ) {

        this.ppdbOptionLookupSchoolRepository = ppdbOptionLookupSchoolRepository;
        this.ppdbRegistrationRepository = ppdbRegistrationRepository;
        this.ppdbFilteredRepository = ppdbFilteredRepository;
        this.ppdbView = new PpdbView();
        this.initializationOutcast = new InitializationOutcast();
        this.findData = new FindData();
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
        String jalurs[] = new String[] { "perpindahan", "anak-guru", "nhun","nhun-unggulan",};
        ppdbOptions.addAll(ppdbOptionLookupSchoolRepository.lookupPpdbOptionPpdbSchoolByJalur(jalurs));

        System.out.println("ppdbOptions:" + ppdbOptions.size());
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("ppdbSchools:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                    " Add: " + ppdbOptions.get(iOpt).getPpdb_schools().getAddress());
            List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice(ppdbOptions.get(iOpt).get_id());


            List<PpdbRegistration> ppdbRegistrationList = new ArrayList<>();
            List<PpdbRegistration> oriRegistrationList = new ArrayList<>();
            PpdbRegistration ppdbRegistration = new PpdbRegistration();
            for (int std = 0; std <ppdbRegistrations.size() ; std++) {

                /*
                System.out.println("ppdbRegistrations1:" + std + "-" + ppdbRegistrations.get(std).get_id()  + " - " +
                        ppdbRegistrations.get(std).getName() +  " - " +
                        ppdbRegistrations.get(std).getSkorPeserta() + " - " +
                        ppdbRegistrations.get(std).getSkorJarak1()); */

                ppdbRegistration = new PpdbRegistration();
                ppdbRegistration.set_id(ppdbRegistrations.get(std).get_id());
                ppdbRegistration.setName(ppdbRegistrations.get(std).getName());
                ppdbRegistration.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
                ppdbRegistration.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
                ppdbRegistration.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
                ppdbRegistration.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
                ppdbRegistration.setChoiceIteration(0); //diterima dipilihan 1
                ppdbRegistrationList.add(ppdbRegistration);
                oriRegistrationList.add(ppdbRegistration);
            }
            ppdbOptions.get(iOpt).setPpdbRegistrationList(ppdbRegistrationList);
            ppdbOptions.get(iOpt).setOriRegistrationList(oriRegistrationList);

            System.out.println("===============================================================================");
        }

        initializationOutcast.setOptionOutcast(ppdbOptions);

        //List<PpdbRegistration> ppdbRegistrations = ppdbRegistrationRepository.getByFirstChoice();
        ppdbView.displayOption(ppdbOptions);
        System.out.println("==============================================================");
        potongBerdasarkanQuota(ppdbOptions);

        //pelimpahanQuota(ppdbOptions);
        System.out.println("==============================================================");
        //displayStudent(ppdbOptions);
        ppdbView.displayOption(ppdbOptions);
        //checkIfAnyRemainingQuota(ppdbOptions);
        //System.out.println("==============================================================");
        //ppdbView.displayOption(ppdbOptions);
    }

    private void potongBerdasarkanQuota(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ( (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan") ) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru"))){



                System.out.println( iOpt + "-" + ppdbOptions.get(iOpt).getName() +
                        " jumlahPendaftar:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota() +
                        " sts:" + ppdbOptions.get(iOpt).isNeedFilter() + "-qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance());



                //cari dulu option index lawannya, kali aja, di jalur lawannya, ada siswa quota, sehingga bisa meminta
                Integer optTargetIdx = null;
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), "anak-guru", ppdbOptions);

                } else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru")) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), "perpindahan", ppdbOptions);
                }

                System.out.println( "checkQuotaBalance " +  ppdbOptions.get(iOpt).getName() + ":" + ppdbOptions.get(iOpt).getQuotaBalance() + " && " + ppdbOptions.get(optTargetIdx).getQuotaBalance());
                if ((ppdbOptions.get(iOpt).getQuotaBalance() < 0) && (ppdbOptions.get(optTargetIdx).getQuotaBalance() > 0 ) ){

                    //search siswa yang sudah terlempar, dengan tracking dari ori registration

                    if (ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx).getQuota()) {
                        System.out.println("set quota balance:" + ppdbOptions.get(iOpt).getName() +
                                        " oriStudent:" + ppdbOptions.get(iOpt).getOriRegistrationList().size()
                                );

                        //quotaBalance, jika kekurangan jadi negatif

                        Integer avalaibleQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                        System.out.println("sisaQuotaTarget:" + avalaibleQuotaTarget + "=" + ppdbOptions.get(optTargetIdx).getQuota() + "-" + ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size());
                        Integer transferQuotaBasedOnNeed = 0;
                        Integer sisaQuotaTarget = 0;
                        //cek jika sisa quota target lebih besar dari (kebutuhan quota) dijadikan positif Math.abs

                        if ( avalaibleQuotaTarget > Math.abs(ppdbOptions.get(iOpt).getQuotaBalance())){
                            sisaQuotaTarget = (avalaibleQuotaTarget + ppdbOptions.get(iOpt).getQuotaBalance());
                            transferQuotaBasedOnNeed = avalaibleQuotaTarget - sisaQuotaTarget;
                        }else{
                            transferQuotaBasedOnNeed = avalaibleQuotaTarget;
                        }
                        System.out.println("ppdbOptions.get(iOpt).getQuota():" + ppdbOptions.get(iOpt).getQuota() + " + " + transferQuotaBasedOnNeed);
                        Integer totalQuotaOptionCur = ppdbOptions.get(iOpt).getQuota() + transferQuotaBasedOnNeed;
                        Integer totalQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - transferQuotaBasedOnNeed;

                        ppdbOptions.get(optTargetIdx).setQuota(totalQuotaTarget);
                        ppdbOptions.get(iOpt).setQuota(totalQuotaOptionCur);

                        ppdbOptions.get(iOpt).setQuotaBalance(ppdbOptions.get(iOpt).getQuotaBalance()+transferQuotaBasedOnNeed);
                        ppdbOptions.get(optTargetIdx).setQuotaBalance(ppdbOptions.get(optTargetIdx).getQuotaBalance()-transferQuotaBasedOnNeed);

                        for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getOriRegistrationList().size() ; iOriStd++) {
                            List<ObjectId> oriStdHistories = ppdbOptions.get(iOpt).getOriRegistrationList().get(iOriStd).getOptionHistories();
                            ObjectId oriStdfirstChoice = ppdbOptions.get(iOpt).getOriRegistrationList().get(iOriStd).getFirstChoice();


                                 if (oriStdHistories.size()>0){
                                   Integer optionIdxLemparan = this.findData.findOptionIdxByChoice(oriStdHistories.get(oriStdHistories.size()-1), ppdbOptions);
                                   Integer optionIdxFirstChoice = this.findData.findOptionIdxByChoice(oriStdfirstChoice, ppdbOptions);

                                   List<PpdbRegistration> ppdbRegistrationList = new ArrayList<>();
                                   ppdbRegistrationList.clear();
                                   ppdbRegistrationList.addAll(ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList());
                                     for (int iStdLemparan = 0; iStdLemparan <ppdbRegistrationList.size() ; iStdLemparan++) {
                                         //cek id siswa sama atau tidak dengan yang di ori siswa
                                         if (ppdbRegistrationList.get(iStdLemparan).get_id().equals(ppdbOptions.get(iOpt).getOriRegistrationList().get(iOriStd).get_id())){

                                             List<PpdbRegistration> firstChoiceRegistrationList = new ArrayList<>();
                                             firstChoiceRegistrationList.clear();
                                             System.out.println("optFirstChoice" + ppdbOptions.get(optionIdxFirstChoice).getName());
                                             for (int i = 0; i <ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList().size() ; i++) {
                                                 System.out.println("bef==>" + ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList().get(i).getName());
                                             }

                                             firstChoiceRegistrationList.addAll(ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList());
                                             firstChoiceRegistrationList.add(ppdbRegistrationList.get(iStdLemparan));

                                             System.out.println("tarik:" + ppdbRegistrationList.get(iStdLemparan).getName() + " firstChoiceRegistrationList:" + firstChoiceRegistrationList.size());



                                             ppdbRegistrationList.remove(iStdLemparan);


                                             //set yg kekurangin siswanya di filter ulang
                                             ppdbOptions.get(optionIdxFirstChoice).setNeedFilter(true);
                                             ppdbOptions.get(optionIdxFirstChoice).setPpdbRegistrationList(firstChoiceRegistrationList);



                                             System.out.println("iOpt:" + iOpt + " optionIdxFirstChoice: " + optionIdxFirstChoice);
                                             //set yg ditambahin siswanya di filter ulang
                                             ppdbOptions.get(optionIdxLemparan).setNeedFilter(true);
                                             ppdbOptions.get(optionIdxLemparan).setPpdbRegistrationList(ppdbRegistrationList);

                                             System.out.println("optFirstChoice" + ppdbOptions.get(optionIdxFirstChoice).getName());
                                             for (int i = 0; i <ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList().size() ; i++) {
                                                 System.out.println("aft==>" + ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList().get(i).getName());
                                             }


                                             break;
                                         }
                                     }

                                 }

                        }



                    }

                }

                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList1:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
                List<PpdbRegistration> oriStudents = ppdbOptions.get(iOpt).getOriRegistrationList();
                Collections.sort(students, new StudentComparator());
                System.out.println("potong siswa " + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota());
                if (ppdbOptions.get(iOpt).getPpdbRegistrationList().size() > ppdbOptions.get(iOpt).getQuota()) { //jika pendaftar lebih banyak dari quota, maka potong

                    /*
                    if (ppdbOptions.get(optTargetIdx).isNeedFilter() == false){ //sudah tidak membutuhkan di filter ulang, karena sudah difilter
                        //jika lawannya atau targetnya ada sisa quota, maka

                        if (ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx).getQuota()) {
                            System.out.println("set quota transfer " + ppdbOptions.get(iOpt).getName());

                            Integer sisaQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                            Integer totalQuotaOptionCur = ppdbOptions.get(iOpt).getQuota() + sisaQuotaTarget;
                            Integer totalQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - sisaQuotaTarget;
                            ppdbOptions.get(optTargetIdx).setQuota(totalQuotaTarget);
                            ppdbOptions.get(iOpt).setQuota(totalQuotaOptionCur);
                        }
                    } */


                    Integer kekuranganQuota = 0;

                    for (int iStd = students.size()-1; iStd>ppdbOptions.get(iOpt).getQuota()-1 ; iStd--) {

                        System.out.println("option:" + iOpt + ppdbOptions.get(iOpt).getName() + " - " + students.get(iStd).get_id() +
                                " sec:" + students.get(iStd).getSecondChoice());

                        Integer idxTargetOption = null;
                        if (students.get(iStd).getChoiceIteration()==0){ //saat ini posisi siswa tidak diterima pilihan 1, maka ambil informasi skeolah pilihan 2
                            idxTargetOption = findData.findOptionIdxByChoice(students.get(iStd).getSecondChoice(),ppdbOptions);
                            students.get(iStd).setChoiceIteration(1);
                        }else if (students.get(iStd).getChoiceIteration()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                            //harusnya ke option pilihan swasta
                            idxTargetOption = ppdbOptions.size()-1;
                        }else{
                            idxTargetOption = ppdbOptions.size()-1;
                        }
                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().add(students.get(iStd)); //tambahkan siswa ke sekolah pilihan selanjutnya
                        ppdbOptions.get(idxTargetOption).setNeedFilter(true); //rubah status needFilter=true,karena sekolah tsb dapat limpahan siswa, jadi perlu di filter ulang


                        List<ObjectId> actObjectIds = new ArrayList<>();
                        actObjectIds.addAll(students.get(iStd).getOptionHistories());
                        actObjectIds.add(students.get(iStd).getSecondChoice());
                        System.out.println("actObjectIds");
                        System.out.println(students.get(iStd).get_id() + "-" + students.get(iStd).getName() + "-students.get(iStd).getSecondChoice():" + students.get(iStd).getSecondChoice());
                        for (ObjectId oid: actObjectIds
                        ) {
                            System.out.println("->actObjectId:" + oid);
                        }

                        List<ObjectId> oriObjectIds = new ArrayList<>();
                        oriObjectIds.addAll(oriStudents.get(iStd).getOptionHistories());
                        oriObjectIds.add(oriStudents.get(iStd).getSecondChoice());
                        System.out.println("oriObjectIds");
                        for (ObjectId oid: oriObjectIds
                             ) {
                            System.out.println("->oriObjectId:" + oid);
                        }

                        students.get(iStd).setOptionHistories(actObjectIds);
                        oriStudents.get(iStd).setOptionHistories(oriObjectIds);

                        //Integer idxFirstChoiceOption = findData.findOptionIdxByChoice(students.get(iStd).getFirstChoice(), ppdbOptions);
                        students.remove(iStd); //hapus siswa
                        kekuranganQuota--;
                    }
                    ppdbOptions.get(iOpt).setQuotaBalance(kekuranganQuota);
                }
                ppdbOptions.get(iOpt).setNeedFilter(false);
                ppdbOptions.get(iOpt).setPpdbRegistrationList(students); ///?
                ppdbOptions.get(iOpt).setOriRegistrationList(oriStudents); ///?

                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList2:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                if (ppdbOptions.get(iOpt).getPpdbRegistrationList().size() < ppdbOptions.get(iOpt).getQuota()){
                    Integer overQuota = ppdbOptions.get(iOpt).getQuota() - ppdbOptions.get(iOpt).getPpdbRegistrationList().size();
                    ppdbOptions.get(iOpt).setQuotaBalance(ppdbOptions.get(iOpt).getQuotaBalance()+overQuota);
                }





                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList3:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                ppdbOptions.set(iOpt, ppdbOptions.get(iOpt)); ///?
            }
        }

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ((ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru"))) {

                Integer optTargetIdx = null;
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), "anak-guru", ppdbOptions);

                } else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru")) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), "perpindahan", ppdbOptions);
                }

                if (
                        ( ppdbOptions.get(iOpt).isNeedFilter() == true )
                                ||
                        ( (ppdbOptions.get(iOpt).getQuotaBalance() < 0 ) && (ppdbOptions.get(optTargetIdx).getQuotaBalance()>0) )
                    ) {
                    potongBerdasarkanQuota(ppdbOptions);
                    break;
                }
            }
        }
    }

    public static<T> List<T> addToList(List<T> target, Stream<T> source) {
        target.addAll(source.collect(Collectors.toList()));
        return target;
    }

    public void checkIfAnyRemainingQuota(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ((ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru"))) {

                transferQuota(ppdbOptions, iOpt, "nhun");

            }
        }

    }

    public void transferQuota(List<PpdbOption> ppdbOptions, int iOpt, String targetJalur){

            int jumlahPendaftarDiterima = ppdbOptions.get(iOpt).getPpdbRegistrationList().size();
            int quotaOption = ppdbOptions.get(iOpt).getQuota();
            System.out.println(iOpt + ppdbOptions.get(iOpt).getName() + " jumlahPendaftar:" + jumlahPendaftarDiterima + " > " + quotaOption);

            if (jumlahPendaftarDiterima < quotaOption) { //butuh quota, cek ke nhun
                Integer optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), targetJalur , ppdbOptions);
                Integer kelebihanQuota = quotaOption - jumlahPendaftarDiterima;

                    Integer targetQuota = ppdbOptions.get(optTargetIdx).getQuota();
                    Integer totalQuotaTarget = targetQuota + kelebihanQuota;
                    Integer sisaQuotaCurOption = quotaOption - kelebihanQuota;
                    ppdbOptions.get(iOpt).setQuota(sisaQuotaCurOption);
                    ppdbOptions.get(optTargetIdx).setQuota(totalQuotaTarget);
            }

    }


}
