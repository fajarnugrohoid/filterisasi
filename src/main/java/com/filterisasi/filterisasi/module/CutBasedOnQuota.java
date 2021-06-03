package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentComparator;
import com.filterisasi.filterisasi.lib.StudentHistory;
import com.filterisasi.filterisasi.lib.UpdateData;
import com.filterisasi.filterisasi.model.PpdbHistory;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CutBasedOnQuota {

    private FindData findData;
    private StudentHistory studentHistory;
    private CheckQuotaBalance checkQuotaBalance;
    private UpdateData updateData;

    public CutBasedOnQuota(FindData findData, StudentHistory studentHistory, CheckQuotaBalance checkQuotaBalance, UpdateData updateData) {
        this.findData = findData;
        this.studentHistory = studentHistory;
        this.checkQuotaBalance = checkQuotaBalance;
        this.updateData = updateData;
    }

    public void potongBerdasarkanQuota(List<PpdbOption> ppdbOptions, String jalur1, String jalur2){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ( (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1) ) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur2))){

                System.out.println( iOpt + "-" + ppdbOptions.get(iOpt).getName() +
                        " jumlahPendaftarFirst:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota() +
                        " sts:" + ppdbOptions.get(iOpt).isNeedFilter() + "-qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance());


                int optTargetIdx = -1;
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur2, ppdbOptions);

                } else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur2)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur1, ppdbOptions);
                }

                this.checkQuotaBalance.transferQuotaBalance(ppdbOptions, iOpt, optTargetIdx);

                List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
                List<PpdbHistory> studentActOptionHistories = new ArrayList<>();
                studentActOptionHistories.addAll(ppdbOptions.get(iOpt).getPpdbRegistrationHistories());

                Collections.sort(students, new StudentComparator());
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

                            students.get(iStd).setAcceptedOptionNo(1);
                            students.get(iStd).setAcceptedOptionId(students.get(iStd).getSecondChoice());


                            acceptedOptionId = students.get(iStd).getSecondChoice();
                            acceptedOptionNo = 1;

                        }else if (students.get(iStd).getAcceptedOptionNo()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                            //harusnya ke option pilihan swasta
                            idxTargetOption = ppdbOptions.size()-1;

                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());

                            acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                            acceptedOptionNo = 2;

                        }else{
                            idxTargetOption = ppdbOptions.size()-1;
                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());

                            acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                            acceptedOptionNo = 2;
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
                                    students.get(iStd).getSkorJarak1(),
                                    students.get(iStd).getFirstChoice(),
                                    students.get(iStd).getSecondChoice(),
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

        checkOptionNeedFilterOrRemainingQuota(ppdbOptions, jalur1, jalur2);

    }

    public void potongBerdasarkanQuotaTanpaBerbagiQuota(List<PpdbOption> ppdbOptions, String jalur1){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat


                System.out.println( iOpt + "-" + ppdbOptions.get(iOpt).getName() +
                        " jumlahPendaftarFirst:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() + " > " + ppdbOptions.get(iOpt).getQuota() +
                        " sts:" + ppdbOptions.get(iOpt).isNeedFilter() + "-qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance());


                //int optTargetIdx = -1;


                List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
                List<PpdbHistory> studentActOptionHistories = new ArrayList<>();
                studentActOptionHistories.addAll(ppdbOptions.get(iOpt).getPpdbRegistrationHistories());

                Collections.sort(students, new StudentComparator());
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

                            students.get(iStd).setAcceptedOptionNo(1);
                            students.get(iStd).setAcceptedOptionId(students.get(iStd).getSecondChoice());


                            acceptedOptionId = students.get(iStd).getSecondChoice();
                            acceptedOptionNo = 1;

                        }else if (students.get(iStd).getAcceptedOptionNo()==1) { //saat ini posisi siswa tidak diterima pilihan 2, maka ambil informasi skeolah pilihan 3
                            //harusnya ke option pilihan swasta
                            idxTargetOption = ppdbOptions.size()-1;

                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());

                            acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                            acceptedOptionNo = 2;

                        }else{
                            idxTargetOption = ppdbOptions.size()-1;
                            students.get(iStd).setAcceptedOptionNo(2);
                            students.get(iStd).setAcceptedOptionId(ppdbOptions.get(idxTargetOption).get_id());

                            acceptedOptionId = ppdbOptions.get(idxTargetOption).get_id();
                            acceptedOptionNo = 2;
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
                                    students.get(iStd).getSkorJarak1(),
                                    students.get(iStd).getFirstChoice(),
                                    students.get(iStd).getSecondChoice(),
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

        checkOptionNeedFilterTanpaBerbagiQuota(ppdbOptions, jalur1);

    }

    public void checkOptionNeedFilterOrRemainingQuota(List<PpdbOption> ppdbOptions, String jalur1, String jalur2){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ((ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1)) || (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur2))) {

                int optTargetIdx = -1;
                if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur2, ppdbOptions);

                } else if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur2)) {
                    optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), jalur1, ppdbOptions);
                }


                int sisaQuota = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                if (
                        ( ppdbOptions.get(iOpt).isNeedFilter() == true )
                                ||
                                (
                                        (ppdbOptions.get(iOpt).getQuotaBalance() < 0 ) && ( sisaQuota > 0 )
                                )
                ) {
                    potongBerdasarkanQuota(ppdbOptions, jalur1, jalur2);
                    break;
                }

                /*
                if (
                        ppdbOptions.get(iOpt).isNeedFilter() == true
                ) {
                    potongBerdasarkanQuota(ppdbOptions);
                    break;
                } */

            }
        }
    }

    public void checkOptionNeedFilterTanpaBerbagiQuota(List<PpdbOption> ppdbOptions, String jalur1){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

                if (
                        ppdbOptions.get(iOpt).isNeedFilter() == true
                ) {
                    potongBerdasarkanQuotaTanpaBerbagiQuota(ppdbOptions, jalur1);
                    break;
                }

        }
    }

}
