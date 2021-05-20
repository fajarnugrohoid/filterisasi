package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentComparator;
import com.filterisasi.filterisasi.lib.StudentHistory;
import com.filterisasi.filterisasi.lib.TransformData;
import com.filterisasi.filterisasi.module.CheckQuotaBalance;
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
    private TransformData transformData;
    private CheckQuotaBalance checkQuotaBalance;
    private StudentHistory studentHistory;
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
        this.transformData = new TransformData();
        this.checkQuotaBalance = new CheckQuotaBalance(this.findData);
        this.studentHistory = new StudentHistory();
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("run...");
        List<PpdbFiltered> ppdbFiltereds = new ArrayList<>();
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
            PpdbRegistration ppdbRegistration = new PpdbRegistration();

            List<PpdbRegistration> ppdbRegistrationHistories = new ArrayList<>();
            PpdbRegistration ppdbRegistrationHistory = new PpdbRegistration();

            for (int std = 0; std <ppdbRegistrations.size() ; std++) {

                ppdbRegistration = new PpdbRegistration();
                ppdbRegistration.set_id(ppdbRegistrations.get(std).get_id());
                ppdbRegistration.setName(ppdbRegistrations.get(std).getName());
                ppdbRegistration.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
                ppdbRegistration.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
                ppdbRegistration.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
                ppdbRegistration.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
                ppdbRegistration.setAcceptedOptionNo(0); //diterima dipilihan 1
                ppdbRegistrationList.add(ppdbRegistration);

                ppdbRegistrationHistory = new PpdbRegistration();
                ppdbRegistrationHistory.set_id(ppdbRegistrations.get(std).get_id());
                ppdbRegistrationHistory.setName(ppdbRegistrations.get(std).getName());
                ppdbRegistrationHistory.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
                ppdbRegistrationHistory.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
                ppdbRegistrationHistory.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
                ppdbRegistrationHistory.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
                ppdbRegistrationHistory.setAcceptedOptionNo(0); //diterima dipilihan 1
                ppdbRegistrationHistory.setAcceptedOptionId(ppdbOptions.get(iOpt).get_id());
                ppdbRegistrationHistory.setStudentExist(true);

                ppdbRegistrationHistories.add(ppdbRegistrationHistory);
            }
            ppdbOptions.get(iOpt).setPpdbRegistrationList(ppdbRegistrationList);
            ppdbOptions.get(iOpt).setPpdbRegistrationHistories(ppdbRegistrationHistories);

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
                        " jumlahPendaftarFirst:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota() +
                        " sts:" + ppdbOptions.get(iOpt).isNeedFilter() + "-qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance());



                //cari dulu option index lawannya, kali aja, di jalur lawannya, ada siswa quota, sehingga bisa meminta
                int optTargetIdx = -1;
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), "anak-guru", ppdbOptions);

                } else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru")) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), "perpindahan", ppdbOptions);
                }

                //checkQuotaBalance.TransferQuotaBalance(ppdbOptions, iOpt, optTargetIdx);
                int sisaQuota = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                System.out.println( "checkQuotaBalance " +  ppdbOptions.get(iOpt).getName() + ":" + ppdbOptions.get(iOpt).getQuotaBalance() + " && " + sisaQuota );
                if ((ppdbOptions.get(iOpt).getQuotaBalance() < 0) && (sisaQuota > 0 ) ){

                    //search siswa yang sudah terlempar, dengan tracking dari ori registration
                    System.out.println("optTarget studentList:" + ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() + " < q:" + ppdbOptions.get(optTargetIdx).getQuota());
                    if (ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx).getQuota()) {
                        System.out.println("set quota balance:" + ppdbOptions.get(iOpt).getName() +
                                " oriStudent:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size()
                        );

                        //quotaBalance, jika kekurangan jadi negatif

                        int avalaibleQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                        System.out.println("sisaQuotaTarget:" + avalaibleQuotaTarget + "=" + ppdbOptions.get(optTargetIdx).getQuota() + "-" + ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size());
                        int transferQuotaBasedOnNeed = 0;
                        int sisaQuotaTarget = 0;
                        //cek jika sisa quota target lebih besar dari (kebutuhan quota) dijadikan positif Math.abs

                        if ( avalaibleQuotaTarget > Math.abs(ppdbOptions.get(iOpt).getQuotaBalance())){
                            sisaQuotaTarget = (avalaibleQuotaTarget + ppdbOptions.get(iOpt).getQuotaBalance());
                            transferQuotaBasedOnNeed = avalaibleQuotaTarget - sisaQuotaTarget;
                        }else{
                            transferQuotaBasedOnNeed = avalaibleQuotaTarget;
                        }
                        System.out.println("ppdbOptions.get(iOpt).getQuota():" + ppdbOptions.get(iOpt).getQuota() + " + " + transferQuotaBasedOnNeed);
                        int totalQuotaOptionCur = ppdbOptions.get(iOpt).getQuota() + transferQuotaBasedOnNeed;
                        int totalQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - transferQuotaBasedOnNeed;

                        ppdbOptions.get(optTargetIdx).setQuota(totalQuotaTarget);
                        ppdbOptions.get(iOpt).setQuota(totalQuotaOptionCur);

                        ppdbOptions.get(iOpt).setQuotaBalance(ppdbOptions.get(iOpt).getQuotaBalance()+transferQuotaBasedOnNeed);
                        ppdbOptions.get(optTargetIdx).setQuotaBalance(ppdbOptions.get(optTargetIdx).getQuotaBalance()-transferQuotaBasedOnNeed);


                        //cek siswa yang pil1 ke active option(sekolah yang sedang di looping)
                        //cek siswa yang pil2 ke active option(sekolah yang sedang di looping)
                        //atau cek siswa yang pernah terlempar ke sekolah ini active option(sekolah yang sedang di looping)
                        //caranya setiap siswa yang terlempar, siswa tsb simpan saja di history
                        for (int iStd = 0; iStd <ppdbOptions.get(iOpt).getPpdbRegistrationList().size() ; iStd++) {
                            System.out.println("==>reg:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(iStd).getName());
                        }

                        for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iOriStd++) {

                            //List<ObjectId> oriStdHistories = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getOptionHistories();
                            System.out.println("==>hist:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() + "-isExist:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).isStudentExist());
                            if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).isStudentExist()==false){ //cari siswa di history yang tidak ada disekolah tersebut
                                if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()!=0){ //kudu di cek lagi, jika siswa diterima dipilihan pertama, maka jangan ditarik lagi
                                //cek siswa yang terlempar apakah active option sekarang untuk dia, masuk ke pilihan berapa

                                    //cek pilihan pada siswa ini sama dengan pilihan pertama
                                    if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice()==ppdbOptions.get(iOpt).get_id()){
                                        System.out.println("==>" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                                " pilihan siswa sama dengan pilihan pertama");
                                    }

                                    //cek pilihan pada siswa ini sama dengan pilihan kedua
                                    else if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice()==ppdbOptions.get(iOpt).get_id()){
                                        System.out.println("==>" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                                " pilihan siswa sama dengan pilihan kedua");
                                    }
                                    else{
                                        //jika pilihan3 atau tempat pembuangan
                                        System.out.println("==>" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                                " pilihan siswa sama dengan pilihan ketiga/terakhir");
                                    }

                                ObjectId acceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId();

                                int optionIdxLemparan = this.findData.findIndexFromOptionsByChoice(acceptedOptionId, ppdbOptions);
                                //int optionIdxFirstChoice = this.findData.findOptionIdxByChoice(oriStdfirstChoice, ppdbOptions);
                                    System.out.println("lemparan:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                            " acceptedOptionId:" + acceptedOptionId +
                                            " optionName:" + ppdbOptions.get(optionIdxLemparan).getName() +
                                            " optionIdxLemparan:" + optionIdxLemparan +
                                            " ppdbOptions regisList:" + ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList().size()
                                            );


                                     List<PpdbRegistration> studentListPull = new ArrayList<>();
                                             studentListPull = ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList();
                                    for (int iStdLemparan = 0; iStdLemparan <studentListPull.size() ; iStdLemparan++) {

                                        System.out.println("==>checkStdLemparan:" +
                                                studentListPull.get(iStdLemparan).getName() + "==" +
                                                ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() + " && " +
                                                studentListPull.get(iStdLemparan).get_id() + "==" +
                                                ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id()
                                        );
                                        if (studentListPull.get(iStdLemparan).get_id().equals(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id()) ){

                                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).setStudentExist(true);
                                            ppdbOptions.get(iOpt).getPpdbRegistrationList().add(studentListPull.get(iStdLemparan));
                                            ppdbOptions.get(iOpt).setNeedFilter(true);

                                            System.out.println("tarik:" +
                                                    ppdbOptions.get(optionIdxLemparan).getName() +
                                                    " - " + studentListPull.get(iStdLemparan).getName()
                                            );

                                            for (int iStd = 0; iStd < studentListPull.size() ; iStd++) {
                                                System.out.println("==>reg-lemparan-tarik-start:" + studentListPull.get(iStd).getName());
                                            }

                                            int idxStudentTargetHistory = studentHistory.findIndexStudentHistoryById(ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories(), ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList().get(iStdLemparan).get_id());
                                            int idxStudentTargetCur = findData.findIndexFromStudentsById(studentListPull, studentListPull.get(iStdLemparan).get_id());
                                            System.out.println("idxStudentTargetCur:" + idxStudentTargetCur);

                                            ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().get(idxStudentTargetHistory).setStudentExist(false);
                                            ppdbOptions.get(optionIdxLemparan).setNeedFilter(true);

                                            studentListPull.remove(idxStudentTargetCur);

                                            for (int iStd = 0; iStd < studentListPull.size() ; iStd++) {
                                                System.out.println("==>reg-lemparan-tarik-end:" + studentListPull.get(iStd).getName());
                                            }

                                            ppdbOptions.get(optionIdxLemparan).setPpdbRegistrationList(studentListPull);

                                            break;
                                        }
                                    }
                                }
                            } //end cari siswa yang tidak ada di sekolah tersebut untuk ditarik lagi


                        } //end for loop registrationHistory



                    }

                }

                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList1:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
                List<PpdbRegistration> studentActOptionHistories = ppdbOptions.get(iOpt).getPpdbRegistrationHistories();

                Collections.sort(students, new StudentComparator());
                System.out.println(ppdbOptions.get(iOpt).getName()  + " potong siswa " + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota());
                if (ppdbOptions.get(iOpt).getPpdbRegistrationList().size() > ppdbOptions.get(iOpt).getQuota()) { //jika pendaftar lebih banyak dari quota, maka potong


                    int kekuranganQuota = 0;

                    for (int iStd = students.size()-1; iStd>ppdbOptions.get(iOpt).getQuota()-1 ; iStd--) {

                        System.out.println("option:" + iOpt + ppdbOptions.get(iOpt).getName() + " - " + students.get(iStd).get_id() + " - " + students.get(iStd).getName() +
                                " sec:" + students.get(iStd).getSecondChoice());

                        int idxTargetOption = -1;
                        int studentHistoryIdxById = studentHistory.findIndexStudentHistoryById(studentActOptionHistories, students.get(iStd).get_id());
                        PpdbRegistration addHistoryStudentTarget = new PpdbRegistration();
                        if (students.get(iStd).getAcceptedOptionNo()==0){ //saat ini posisi siswa tidak diterima pilihan 1, maka ambil informasi skeolah pilihan 2
                            idxTargetOption = findData.findIndexFromOptionsByChoice(students.get(iStd).getSecondChoice(),ppdbOptions);

                            students.get(iStd).setAcceptedOptionNo(1);
                            students.get(iStd).setAcceptedOptionId(students.get(iStd).getSecondChoice());
                            addHistoryStudentTarget = students.get(iStd);

                            studentActOptionHistories.get(studentHistoryIdxById).setStudentExist(false);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionNo(1);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionId(students.get(iStd).getSecondChoice());

                        }else if (students.get(iStd).getAcceptedOptionNo()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                            //harusnya ke option pilihan swasta
                            idxTargetOption = ppdbOptions.size()-1;
                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                            addHistoryStudentTarget = students.get(iStd);

                            studentActOptionHistories.get(studentHistoryIdxById).setStudentExist(false);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionNo(2);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                        }else{
                            idxTargetOption = ppdbOptions.size()-1;
                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                            addHistoryStudentTarget = students.get(iStd);

                            studentActOptionHistories.get(studentHistoryIdxById).setStudentExist(false);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionNo(2);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                        }
                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().add(students.get(iStd)); //tambahkan siswa ke sekolah pilihan selanjutnya
                        ppdbOptions.get(idxTargetOption).setNeedFilter(true); //rubah status needFilter=true,karena sekolah tsb dapat limpahan siswa, jadi perlu di filter ulang

                        addHistoryStudentTarget.setStudentExist(true);
                        System.out.println("idxTargetOption:" + idxTargetOption);
                        System.out.println("addHistoryStudentTarget:" + addHistoryStudentTarget.get_id());
                        System.out.println("getPpdbRegistrationHistories:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size());

                        students.remove(iStd); //hapus siswa
                        kekuranganQuota--;

                        this.studentHistory.addStudentHistory(ppdbOptions, idxTargetOption, addHistoryStudentTarget);

                        System.out.println("sekolah pilihan2:" + ppdbOptions.get(idxTargetOption).getName() +
                                " p:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() +
                                " q:" + ppdbOptions.get(idxTargetOption).getQuota() +
                                " qBalance:" + ppdbOptions.get(idxTargetOption).getQuotaBalance()
                        );
                        for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() ; i++) {
                            System.out.println("==>" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getName());
                        }

                        /*
                        List<ObjectId> actObjectIds = new ArrayList<>();
                        actObjectIds.addAll(students.get(iStd).getOptionHistories());
                        actObjectIds.add(students.get(iStd).getSecondChoice());
                        System.out.println("actObjectIds");
                        System.out.println(students.get(iStd).get_id() + "-" + students.get(iStd).getName() + "-students.get(iStd).getSecondChoice():" + students.get(iStd).getSecondChoice());
                        for (ObjectId oid: actObjectIds
                        ) {
                            System.out.println("->actObjectId:" + oid);
                        }

                        int iFirstOption = findData.findOptionIdxByChoice(students.get(iStd).getFirstChoice(), ppdbOptions);
                        List<PpdbRegistration> ppdbRegistrationHistories = ppdbOptions.get(iFirstOption).getPpdbRegistrationHistories();

                        int iOriStd = findData.findStudentHistoryIdxById(ppdbRegistrationHistories, students.get(iStd).get_id());
                        List<ObjectId> oriObjectIds = new ArrayList<>();
                        System.out.println("oriStudents.get(iStd).getOptionHistories:" + ppdbRegistrationHistories.get(iOriStd).getName());
                        for (int i = 0; i <ppdbRegistrationHistories.get(iOriStd).getOptionHistories().size() ; i++) {
                            System.out.println("==>" + ppdbRegistrationHistories.get(iOriStd).getOptionHistories().get(i));
                        }
                        oriObjectIds.addAll(ppdbRegistrationHistories.get(iOriStd).getOptionHistories());
                        oriObjectIds.add(ppdbRegistrationHistories.get(iOriStd).getSecondChoice());
                        System.out.println("oriObjectIds");
                        for (ObjectId oid: oriObjectIds
                             ) {
                            System.out.println("->oriObjectId:" + oid);
                        }

                        students.get(iStd).setOptionHistories(actObjectIds);
                        ppdbRegistrationHistories.get(iOriStd).setOptionHistories(oriObjectIds);
                        ppdbRegistrationHistories.get(iOriStd).setStudentExist(false);*/

                        //int idxFirstChoiceOption = findData.findOptionIdxByChoice(students.get(iStd).getFirstChoice(), ppdbOptions);


                        //ppdbOptions.get(iFirstOption).setPpdbRegistrationHistories(ppdbRegistrationHistories); ///?
                    }
                    ppdbOptions.get(iOpt).setQuotaBalance( ppdbOptions.get(iOpt).getQuotaBalance() + kekuranganQuota);
                }
                ppdbOptions.get(iOpt).setNeedFilter(false);
                ppdbOptions.get(iOpt).setPpdbRegistrationList(students);
                ppdbOptions.get(iOpt).setPpdbRegistrationHistories(studentActOptionHistories);

                /*
                //jika kelebihan quota
                int kelebihanQuota = 0;
                if (ppdbOptions.get(iOpt).getPpdbRegistrationList().size() < ppdbOptions.get(iOpt).getQuota()) {
                    kelebihanQuota = ppdbOptions.get(iOpt).getQuota() - ppdbOptions.get(iOpt).getPpdbRegistrationList().size();
                    ppdbOptions.get(iOpt).setQuotaBalance(kelebihanQuota);
                } */

                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList2:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size() +
                        " q:" +  ppdbOptions.get(iOpt).getQuota() +
                        " qBalance:" +  ppdbOptions.get(iOpt).getQuotaBalance()
                );


                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList3:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                ppdbOptions.set(iOpt, ppdbOptions.get(iOpt)); ///?
            }
        }

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ((ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru"))) {

                int optTargetIdx = -1;
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
                int optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), targetJalur , ppdbOptions);
                int kelebihanQuota = quotaOption - jumlahPendaftarDiterima;

                int targetQuota = ppdbOptions.get(optTargetIdx).getQuota();
                int totalQuotaTarget = targetQuota + kelebihanQuota;
                int sisaQuotaCurOption = quotaOption - kelebihanQuota;
                    ppdbOptions.get(iOpt).setQuota(sisaQuotaCurOption);
                    ppdbOptions.get(optTargetIdx).setQuota(totalQuotaTarget);
            }

    }


}
