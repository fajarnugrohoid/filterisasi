package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.*;
import com.filterisasi.filterisasi.model.PpdbHistory;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CutBasedOnQuotaTargetMoreThanOne {

    private FindData findData;
    private StudentHistory studentHistory;
    private CheckQuotaBalanceTargetMoreThanOne checkQuotaBalanceTargetMoreThanOne;
    private UpdateData updateData;

    public CutBasedOnQuotaTargetMoreThanOne(FindData findData, StudentHistory studentHistory, CheckQuotaBalanceTargetMoreThanOne checkQuotaBalanceTargetMoreThanOne, UpdateData updateData) {
        this.findData = findData;
        this.studentHistory = studentHistory;
        this.checkQuotaBalanceTargetMoreThanOne = checkQuotaBalanceTargetMoreThanOne;
        this.updateData = updateData;
    }


    //berbagi kuuota satu lawan 2
    public void potongBerdasarkanQuotaBerbagiLebihDariSatu(List<PpdbOption> ppdbOptions, String jalur0, String jalur1, String jalur2){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if (
                    (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur0) ) ||
                    (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1)) ||
                    (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur2))
            ){

                System.out.println( iOpt + "-" + ppdbOptions.get(iOpt).getName() +
                        " jumlahPendaftarFirst:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota() +
                        " sts:" + ppdbOptions.get(iOpt).isNeedFilter() + "-qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance());


                int optTargetIdx = -1;
                int optTargetIdx2 = -1;
                System.out.println("ppdbOptions.get(iOpt):" + iOpt + " " + ppdbOptions.get(iOpt).getName());
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur0)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur1, ppdbOptions);

                }
                //misal perpindahan tanya ke ketm dan kondisi tertentu
                else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur0, ppdbOptions);
                    optTargetIdx2 = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur2, ppdbOptions);
                }

                this.checkQuotaBalanceTargetMoreThanOne.transferQuotaBalance(ppdbOptions, iOpt, optTargetIdx, optTargetIdx2);

                List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
                List<PpdbHistory> studentActOptionHistories = new ArrayList<>();
                studentActOptionHistories.addAll(ppdbOptions.get(iOpt).getPpdbRegistrationHistories());

                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru")){
                    Collections.sort(students, new StudentAnakGuruSortingComparator());
                }else {
                    Collections.sort(students, new StudentGeneralSortingComparator());
                }
                System.out.println(ppdbOptions.get(iOpt).getName()  + " potong siswa " + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota());

                if (ppdbOptions.get(iOpt).getPpdbRegistrationList().size() > ppdbOptions.get(iOpt).getQuota()) { //jika pendaftar lebih banyak dari quota, maka potong

                    int kekuranganQuota = 0;

                    for (int iStd = students.size()-1; iStd>ppdbOptions.get(iOpt).getQuota()-1 ; iStd--) {
                        int idxTargetOption = -1;

                        System.out.println("siswa lempar:" + students.get(iStd).getName() + " accNo:" + students.get(iStd).getAcceptedOptionNo());
                        ObjectId acceptedSchoolId;
                        ObjectId acceptedOptionId;
                        int acceptedOptionNo = -1;

                        if (students.get(iStd).getAcceptedOptionNo()==0){ //saat ini posisi siswa tidak diterima pilihan 1, maka ambil informasi skeolah pilihan 2
                            idxTargetOption = findData.findIdxFromOptionsByChoiceId(students.get(iStd).getSecondChoice(),ppdbOptions);

                            if (students.get(iStd).getSecondChoice()!=null){
                                students.get(iStd).setAcceptedOptionNo(1);
                                students.get(iStd).setAcceptedOptionId(students.get(iStd).getSecondChoice());
                                students.get(iStd).setScoreDistanceFinal(students.get(iStd).getScoreJarak2());

                                acceptedOptionId = students.get(iStd).getSecondChoice();
                                acceptedOptionNo = 1;
                            }else{
                                //ke option buangan
                                idxTargetOption = ppdbOptions.size()-1;
                                students.get(iStd).setAcceptedOptionNo(1);
                                students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                                students.get(iStd).setScoreDistanceFinal(students.get(iStd).getScoreJarak2());

                                acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                                acceptedOptionNo = 1;
                            }



                        }else if (students.get(iStd).getAcceptedOptionNo()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                            //ke option pilihan 3

                            if (students.get(iStd).getThirdChoice()!=null){
                                students.get(iStd).setAcceptedOptionNo(2);
                                students.get(iStd).setAcceptedOptionId(students.get(iStd).getThirdChoice());
                                students.get(iStd).setScoreDistanceFinal(students.get(iStd).getScoreJarak3());

                                acceptedOptionId = students.get(iStd).getThirdChoice();
                                acceptedOptionNo = 2;
                            }else{
                                //ke option buangan
                                idxTargetOption = ppdbOptions.size()-1;
                                students.get(iStd).setAcceptedOptionNo(2);
                                students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                                students.get(iStd).setScoreDistanceFinal(students.get(iStd).getScoreJarak3());

                                acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                                acceptedOptionNo = 2;
                            }

                        }
                        else{
                            //ke option buangan
                            idxTargetOption = ppdbOptions.size()-1;
                            students.get(iStd).setAcceptedOptionNo(3);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());
                            students.get(iStd).setScoreDistanceFinal(students.get(iStd).getScoreJarak3());

                            acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                            acceptedOptionNo = 3;
                        }


                        //update history pilihan selanjutnya dari siswa yang akan dilempar
                        List<PpdbHistory> studentTargetOptionHistories = new ArrayList<>();
                        idxTargetOption = findData.findIdxFromOptionsByChoiceId(acceptedOptionId, ppdbOptions);
                        studentTargetOptionHistories.addAll(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories());
                        int idxHistoryTargetOption = studentHistory.findIndexStudentHistoryById(studentTargetOptionHistories, students.get(iStd).get_id());

                        if (idxHistoryTargetOption==-1){
                            //add new

                            PpdbHistory ppdbHistory = this.studentHistory.setStudentHistory(
                                    students.get(iStd).get_id(),
                                    students.get(iStd).getName(),
                                    students.get(iStd).getSkorPeserta(),
                                    students.get(iStd).getScoreJarak1(),
                                    students.get(iStd).getScoreJarak2(),
                                    students.get(iStd).getScoreJarak3(),
                                    students.get(iStd).getScoreDistanceFinal(),
                                    students.get(iStd).getScoreAge(),
                                    students.get(iStd).getFirstChoice(),
                                    students.get(iStd).getSecondChoice(),
                                    students.get(iStd).getThirdChoice(),
                                    acceptedOptionId,
                                    acceptedOptionNo
                            );
                            studentTargetOptionHistories.add(ppdbHistory);

                            int idxFirstOption = findData.findIdxFromOptionsByChoiceId(students.get(iStd).getFirstChoice(),ppdbOptions);
                            this.studentHistory.addNewOrUpdateStudentHistory(ppdbOptions, students, iStd, acceptedOptionId, acceptedOptionNo, idxFirstOption);

                            int idxSecondOption = findData.findIdxFromOptionsByChoiceId(students.get(iStd).getSecondChoice(),ppdbOptions);
                            this.studentHistory.addNewOrUpdateStudentHistory(ppdbOptions, students, iStd, acceptedOptionId, acceptedOptionNo, idxSecondOption);

                            int idxLastOption = ppdbOptions.size()-1;
                            this.studentHistory.addNewOrUpdateStudentHistory(ppdbOptions, students, iStd, acceptedOptionId, acceptedOptionNo, idxLastOption);


                        }else{

                            int idxFirstOption = findData.findIdxFromOptionsByChoiceId(students.get(iStd).getFirstChoice(),ppdbOptions);
                            this.studentHistory.addNewOrUpdateStudentHistory(ppdbOptions, students, iStd, acceptedOptionId, acceptedOptionNo, idxFirstOption);


                            int idxSecondOption = findData.findIdxFromOptionsByChoiceId(students.get(iStd).getSecondChoice(),ppdbOptions);
                            this.studentHistory.addNewOrUpdateStudentHistory(ppdbOptions, students, iStd, acceptedOptionId, acceptedOptionNo, idxSecondOption);

                            int idxLastOption = ppdbOptions.size()-1;
                            this.studentHistory.addNewOrUpdateStudentHistory(ppdbOptions, students, iStd, acceptedOptionId, acceptedOptionNo, idxLastOption);


                            PpdbHistory  updateHistoryDariSiswaYgTerlempar = this.updateData.updateStudent(
                                    ppdbOptions, idxTargetOption,
                                    idxHistoryTargetOption,
                                    acceptedOptionId, acceptedOptionNo);
                            studentTargetOptionHistories.set(idxHistoryTargetOption, updateHistoryDariSiswaYgTerlempar);

                        }

                        ppdbOptions.get(idxTargetOption).setPpdbRegistrationHistories(studentTargetOptionHistories);

                        ppdbOptions.get(idxTargetOption).getPpdbRegistrationList().add(students.get(iStd)); //tambahkan siswa ke sekolah pilihan selanjutnya

                        PpdbOption updateNeedFilter = updateData.updateOption(ppdbOptions, idxTargetOption);
                        ppdbOptions.set(idxTargetOption, updateNeedFilter); //rubah status needFilter=true,karena sekolah tsb dapat limpahan siswa, jadi perlu di filter ulang

                        students.remove(iStd); //hapus siswa
                        kekuranganQuota--;

                    }//end of for loop lempar siswa
                    ppdbOptions.get(iOpt).setQuotaBalance( ppdbOptions.get(iOpt).getQuotaBalance() + kekuranganQuota);
                }

                ppdbOptions.get(iOpt).setNeedFilter(false);
                ppdbOptions.get(iOpt).setPpdbRegistrationList(students);


                ppdbOptions.set(iOpt, ppdbOptions.get(iOpt));
            }
        }

        checkOptionNeedFilterOrRemainingQuota(ppdbOptions, jalur0, jalur1, jalur2);

    }

    public void checkOptionNeedFilterOrRemainingQuota(List<PpdbOption> ppdbOptions, String jalur0, String jalur1, String jalur2){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ((ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur0)) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1))) {

                int optTargetIdx = -1;
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur0)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur1, ppdbOptions);

                } else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur0, ppdbOptions);
                }


                int sisaQuota = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                if (
                        ( ppdbOptions.get(iOpt).isNeedFilter() == true )
                                ||
                                (
                                        (ppdbOptions.get(iOpt).getQuotaBalance() < 0 ) && ( sisaQuota > 0 )
                                )
                ) {
                    potongBerdasarkanQuotaBerbagiLebihDariSatu(ppdbOptions, jalur0, jalur1, jalur2);
                    break;
                }

            }
        }
    }


}
