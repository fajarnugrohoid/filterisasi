package com.filterisasi.filterisasi.resource;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentComparator;
import com.filterisasi.filterisasi.lib.StudentHistory;
import com.filterisasi.filterisasi.lib.TransformData;
import com.filterisasi.filterisasi.model.PpdbHistory;
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
        this.studentHistory = new StudentHistory(findData);
        this.checkQuotaBalance = new CheckQuotaBalance(this.findData, this.studentHistory);

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

            List<PpdbHistory> ppdbRegistrationHistories = new ArrayList<>();
            PpdbHistory ppdbRegistrationHistory = new PpdbHistory();

            for (int std = 0; std <ppdbRegistrations.size() ; std++) {

                ppdbRegistration = new PpdbRegistration();
                ppdbRegistration.set_id(ppdbRegistrations.get(std).get_id());
                ppdbRegistration.setName(ppdbRegistrations.get(std).getName());
                ppdbRegistration.setSkorPeserta(ppdbRegistrations.get(std).getSkorPeserta());
                ppdbRegistration.setSkorJarak1(ppdbRegistrations.get(std).getSkorJarak1());
                ppdbRegistration.setFirstChoice(ppdbRegistrations.get(std).getFirstChoice());
                ppdbRegistration.setSecondChoice(ppdbRegistrations.get(std).getSecondChoice());
                ppdbRegistration.setAcceptedOptionNo(0); //diterima dipilihan 1
                ppdbRegistration.setAcceptedOptionId(ppdbOptions.get(iOpt).get_id());
                ppdbRegistration.setStudentExist(true);
                ppdbRegistrationList.add(ppdbRegistration);

                ppdbRegistrationHistory = new PpdbHistory();
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

                this.checkQuotaBalance.transferQuotaBalance(ppdbOptions, iOpt, optTargetIdx);

                System.out.println(ppdbOptions.get(iOpt).getName() +
                        " getPpdbRegistrationList1:" +  ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
                List<PpdbHistory> studentActOptionHistories = new ArrayList<>();
                studentActOptionHistories.addAll(ppdbOptions.get(iOpt).getPpdbRegistrationHistories());

                Collections.sort(students, new StudentComparator());
                System.out.println(ppdbOptions.get(iOpt).getName()  + " potong siswa " + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota());
                if (ppdbOptions.get(iOpt).getPpdbRegistrationList().size() > ppdbOptions.get(iOpt).getQuota()) { //jika pendaftar lebih banyak dari quota, maka potong


                    int kekuranganQuota = 0;

                    for (int iStd = students.size()-1; iStd>ppdbOptions.get(iOpt).getQuota()-1 ; iStd--) {

                        System.out.println("option:" + iOpt + " " + ppdbOptions.get(iOpt).getName() + " - " + students.get(iStd).get_id() + " - " + students.get(iStd).getName() +
                                " sec:" + students.get(iStd).getSecondChoice());

                        int idxTargetOption = -1;
                        int studentHistoryIdxById = studentHistory.findIndexStudentHistoryById(studentActOptionHistories, students.get(iStd).get_id());

                        //PpdbRegistration addHistoryStudentTarget = new PpdbRegistration();
                        if (students.get(iStd).getAcceptedOptionNo()==0){ //saat ini posisi siswa tidak diterima pilihan 1, maka ambil informasi skeolah pilihan 2
                            idxTargetOption = findData.findIndexFromOptionsByChoice(students.get(iStd).getSecondChoice(),ppdbOptions);

                            System.out.println("bef-targetOptionName:" + ppdbOptions.get(idxTargetOption).getName() +
                                    " regSize:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() +
                                    " histSize:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size()
                            );
                            for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() ; i++) {
                                System.out.println("reg-targetStudent==>" +
                                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getName() +
                                        " accId:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getAcceptedOptionId() +
                                        " accNo:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getAcceptedOptionNo()
                                );
                            }
                            for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size() ; i++) {
                                System.out.println("hist-targetStudent==>" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getName() + " isExist:" +
                                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).isStudentExist() +
                                        " accId:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getAcceptedOptionId() +
                                        " accNo:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getAcceptedOptionNo()
                                );
                            }

                            students.get(iStd).setAcceptedOptionNo(1);
                            students.get(iStd).setAcceptedOptionId(students.get(iStd).getSecondChoice());
                            //addHistoryStudentTarget = students.get(iStd);

                            studentActOptionHistories.get(studentHistoryIdxById).setStudentExist(false);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionNo(1);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionId(students.get(iStd).getSecondChoice());

                            System.out.println("if getAcceptedOptionNo()==0 " +
                                    studentActOptionHistories.get(studentHistoryIdxById).getName() +
                                    " isExist:" + studentActOptionHistories.get(studentHistoryIdxById).isStudentExist());

                        }else if (students.get(iStd).getAcceptedOptionNo()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                            //harusnya ke option pilihan swasta
                            idxTargetOption = ppdbOptions.size()-1;

                            System.out.println("bef-targetOptionName:" + ppdbOptions.get(idxTargetOption).getName() +
                                    " regSize:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() +
                                    " histSize:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size()
                                    );
                            for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() ; i++) {
                                System.out.println("reg-targetStudent==>" +
                                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getName() +
                                        " accId:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getAcceptedOptionId() +
                                        " accNo:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getAcceptedOptionNo()
                                );
                            }
                            for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size() ; i++) {
                                System.out.println("hist-targetStudent==>" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getName() + " isExist:" +
                                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).isStudentExist() +
                                        " accId:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getAcceptedOptionId() +
                                        " accNo:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getAcceptedOptionNo()
                                );
                            }

                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                            //addHistoryStudentTarget = students.get(iStd);


                            studentActOptionHistories.get(studentHistoryIdxById).setStudentExist(false);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionNo(2);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());

                            //kalau untuk target isExist set true
                            //tpi untuk active Student jdi set false, karena siswa telah dipindahkan
                            System.out.println("else if getAcceptedOptionNo()==1 " +
                                    studentActOptionHistories.get(studentHistoryIdxById).getName() +
                                    " studentActExist:" + studentActOptionHistories.get(studentHistoryIdxById).isStudentExist());

                        }else{
                            idxTargetOption = ppdbOptions.size()-1;
                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                            //addHistoryStudentTarget = students.get(iStd);

                            studentActOptionHistories.get(studentHistoryIdxById).setStudentExist(false);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionNo(2);
                            studentActOptionHistories.get(studentHistoryIdxById).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());


                            System.out.println("else " +
                                    studentActOptionHistories.get(studentHistoryIdxById).getName() + " " +
                                    studentActOptionHistories.get(studentHistoryIdxById).isStudentExist());
                        }

                        //this.studentHistory.updateMember(students.get(iStd).get_id(),ppdbOptions);

                        this.studentHistory.addOrUpdateHistoryStudentAllOptions1(ppdbOptions, idxTargetOption, iOpt, iStd);


                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().add(students.get(iStd)); //tambahkan siswa ke sekolah pilihan selanjutnya
                        ppdbOptions.get(idxTargetOption).setNeedFilter(true); //rubah status needFilter=true,karena sekolah tsb dapat limpahan siswa, jadi perlu di filter ulang

                        //addHistoryStudentTarget.setStudentExist(true);
                        System.out.println("idxTargetOption:" + idxTargetOption);
                        //System.out.println("addHistoryStudentTarget:" + addHistoryStudentTarget.get_id());
                        System.out.println("getPpdbRegistrationHistories:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size());

                        students.remove(iStd); //hapus siswa
                        kekuranganQuota--;

                        System.out.println("sekolah pilihan2:" + ppdbOptions.get(idxTargetOption).getName() +
                                " p:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() +
                                " q:" + ppdbOptions.get(idxTargetOption).getQuota() +
                                " qBalance:" + ppdbOptions.get(idxTargetOption).getQuotaBalance()
                        );

                        System.out.println("aft-targetOptionName:" + ppdbOptions.get(idxTargetOption).getName());
                        for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().size() ; i++) {
                            System.out.println("reg-targetStudent==>" +
                                    ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getName() +
                                    " accId:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getAcceptedOptionId() +
                                    " accNo:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().get(i).getAcceptedOptionNo()
                            );
                        }
                        for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size() ; i++) {
                            System.out.println("hist-targetStudent==>" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getName() + " isExist:" +
                                    ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).isStudentExist() +
                                    " accId:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getAcceptedOptionId() +
                                    " accNo:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getAcceptedOptionNo()
                            );
                        }



                    }
                    ppdbOptions.get(iOpt).setQuotaBalance( ppdbOptions.get(iOpt).getQuotaBalance() + kekuranganQuota);
                }
                ppdbOptions.get(iOpt).setNeedFilter(false);
                ppdbOptions.get(iOpt).setPpdbRegistrationList(students);
                ppdbOptions.get(iOpt).setPpdbRegistrationHistories(studentActOptionHistories);

                System.out.println("curOptionName:" + ppdbOptions.get(iOpt).getName());
                for (int i = 0; i <ppdbOptions.get(iOpt).getPpdbRegistrationList().size() ; i++) {
                    System.out.println("reg-curStudent==>" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(i).getName());
                }
                for (int i = 0; i <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; i++) {
                    System.out.println("hist-curStudent==>:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(i).getName() + " isExist:" +
                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(i).isStudentExist() +
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(i).getAcceptedOptionId() +
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(i).getAcceptedOptionNo()
                    );
                }

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

                /*
                if (
                        ( ppdbOptions.get(iOpt).isNeedFilter() == true )
                                ||
                        ( (ppdbOptions.get(iOpt).getQuotaBalance() < 0 ) && (ppdbOptions.get(optTargetIdx).getQuotaBalance()>0) )
                    ) {
                    potongBerdasarkanQuota(ppdbOptions);
                    break;
                } */

                int sisaQuota = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();

                if (
                        ( ppdbOptions.get(iOpt).isNeedFilter() == true )
                                ||
                                (
                                        (ppdbOptions.get(iOpt).getQuotaBalance() < 0 ) &&
                                        ( sisaQuota > 0 )
                                )
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
