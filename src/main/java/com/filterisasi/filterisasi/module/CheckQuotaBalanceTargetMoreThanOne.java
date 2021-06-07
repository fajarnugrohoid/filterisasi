package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentGeneralSortingComparator;
import com.filterisasi.filterisasi.lib.StudentHistory;
import com.filterisasi.filterisasi.utility.PpdbView;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

public class CheckQuotaBalanceTargetMoreThanOne {
    private FindData findData;
    private StudentHistory studentHistory;
    private PpdbView ppdbView;
    public CheckQuotaBalanceTargetMoreThanOne(FindData findData, StudentHistory studentHistory, PpdbView ppdbView) {
        this.findData = findData;
        this.studentHistory = studentHistory;
        this.ppdbView = ppdbView;
    }

    public void transferQuotaBalance(List<PpdbOption> ppdbOptions, int iOpt, int optTargetIdx, int optTargetIdx2){

        System.out.println("optTargetIdx:" + optTargetIdx);
        int sisaQuota = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
        System.out.println( "checkQuotaBalance " +  ppdbOptions.get(iOpt).getName() + ":" +
                ppdbOptions.get(iOpt).getQuotaBalance() +
                " actQuota:" + ppdbOptions.get(iOpt).getQuota() +
                " && " + sisaQuota
        );
        if ((ppdbOptions.get(iOpt).getQuotaBalance() < 0) && (sisaQuota > 0 ) ){

            //search siswa yang sudah terlempar, dengan tracking dari ori registration
            System.out.println("optTarget studentList:" + ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() + " < q:" + ppdbOptions.get(optTargetIdx).getQuota());
            if (ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx).getQuota()) {
                System.out.println("set quota balance:" + ppdbOptions.get(iOpt).getName() +
                        " oriStudent:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                );

                //quotaBalance, jika kekurangan jadi negatif

                int avalaibleQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                System.out.println("sisaQuotaTarget:" + avalaibleQuotaTarget + "=" + ppdbOptions.get(optTargetIdx).getQuota() + "-" + ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size());
                int transferQuotaBasedOnNeed = 0;
                int sisaQuotaTarget = 0;
                //cek jika sisa quota target lebih besar dari (kebutuhan quota) dijadikan positif Math.abs

                if ( avalaibleQuotaTarget > Math.abs(ppdbOptions.get(iOpt).getQuotaBalance())){
                    sisaQuotaTarget = avalaibleQuotaTarget + ppdbOptions.get(iOpt).getQuotaBalance();
                    transferQuotaBasedOnNeed = avalaibleQuotaTarget - sisaQuotaTarget;
                    processAddingQuota(ppdbOptions,iOpt, optTargetIdx, transferQuotaBasedOnNeed);
                }else{
                    transferQuotaBasedOnNeed = avalaibleQuotaTarget;

                    processAddingQuota(ppdbOptions,iOpt, optTargetIdx, transferQuotaBasedOnNeed);

                    int sisaQuota2 = ppdbOptions.get(optTargetIdx2).getQuota() - ppdbOptions.get(optTargetIdx2).getPpdbRegistrationList().size();
                    System.out.println( "checkQuotaBalance " +  ppdbOptions.get(iOpt).getName() + ":" +
                            ppdbOptions.get(iOpt).getQuotaBalance() +
                            " actQuota:" + ppdbOptions.get(iOpt).getQuota() +
                            " && " + sisaQuota2
                    );

                    if ((ppdbOptions.get(iOpt).getQuotaBalance() < 0) && (sisaQuota2 > 0 ) ){
                        System.out.println("optTarget studentList:" + ppdbOptions.get(optTargetIdx2).getPpdbRegistrationList().size() + " < q:" + ppdbOptions.get(optTargetIdx2).getQuota());
                        if (ppdbOptions.get(optTargetIdx2).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx2).getQuota()) {
                            System.out.println("set quota balance:" + ppdbOptions.get(iOpt).getName() +
                                    " oriStudent:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size()
                            );

                            int avalaibleQuotaTarget2 = ppdbOptions.get(optTargetIdx2).getQuota() - ppdbOptions.get(optTargetIdx2).getPpdbRegistrationList().size();
                            System.out.println("sisaQuotaTarget:" + avalaibleQuotaTarget2 + "=" + ppdbOptions.get(optTargetIdx2).getQuota() + "-" + ppdbOptions.get(optTargetIdx2).getPpdbRegistrationList().size());
                            int transferQuotaBasedOnNeed2 = 0;
                            int sisaQuotaTarget2 = 0;

                            if ( avalaibleQuotaTarget2 > Math.abs(ppdbOptions.get(iOpt).getQuotaBalance())){
                                sisaQuotaTarget2 = avalaibleQuotaTarget2 + ppdbOptions.get(iOpt).getQuotaBalance();
                                transferQuotaBasedOnNeed2 = avalaibleQuotaTarget2 - sisaQuotaTarget2;
                                processAddingQuota(ppdbOptions,iOpt, optTargetIdx2, transferQuotaBasedOnNeed2);
                            }else {
                                transferQuotaBasedOnNeed2 = avalaibleQuotaTarget2;
                                processAddingQuota(ppdbOptions,iOpt, optTargetIdx2, transferQuotaBasedOnNeed2);
                            }

                        }
                    }

                }



                Collections.sort(ppdbOptions.get(iOpt).getPpdbRegistrationList(), new StudentGeneralSortingComparator());

                for (int iHistStd = 0; iHistStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iHistStd++) {
                    System.out.println("hist:"+
                            " optionId:" + ppdbOptions.get(iOpt).get_id() +
                            " studentId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).get_id() +
                            " name:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getName() +
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionId() +
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionNo() +
                            " first:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getFirstChoice() +
                            " sec:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getSecondChoice()
                    );

                    int pullOptionIdx = -1;
                    int pullStudentIdx = -1;

                    //cari siswa yg ada di history student, tapi tidak ada di registration
                    if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionId()!=ppdbOptions.get(iOpt).get_id()){
                        System.out.println(
                                " sekolahYgAkanTarikSiswa:" + ppdbOptions.get(iOpt).get_id() + " "  + ppdbOptions.get(iOpt).getName()
                        );
                        System.out.println(" YgAKanDitarik==>" +
                                ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getName() +
                                " optionIdFromSource:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionId() +
                                " optionNoFromSource:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionNo());

                        ObjectId histAcceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionId();
                        pullOptionIdx = findData.findIdxFromOptionsByChoiceId(histAcceptedOptionId, ppdbOptions);
                        pullStudentIdx = findData.findIdxFromRegistStudentsByStdIdandOptIdx(ppdbOptions, pullOptionIdx, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).get_id());

                        System.out.println(" ==>StdId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).get_id() +
                                " name:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getName() +
                                " idxOptionFromSource:" + pullOptionIdx + " " + histAcceptedOptionId + " idxStudentFromSource:" + pullStudentIdx);

                        //ppdbView.displayOptionByIdx(ppdbOptions, pullOptionIdx);
                        //ppdbView.displayOptionByIdx(ppdbOptions, iOpt);

                        PpdbRegistration ppdbRegistration = ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList().get(pullStudentIdx);


                        ObjectId acceptedOptionId = ppdbRegistration.getAcceptedOptionId();
                        int acceptedOptionNo = ppdbRegistration.getAcceptedOptionNo();
                        double scoreDistanceFinal = ppdbRegistration.getScoreDistanceFinal();
                        //perbedaan
                        //ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo() 0
                        //dengan ini
                        //ppdbRegistration.getAcceptedOptionNo() 0

                            //cek dia saat ini ketarik ke pilihan yang mana?
                            if (ppdbOptions.get(iOpt).get_id().equals(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getFirstChoice())) {

                                scoreDistanceFinal = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getScoreJarak1();
                                acceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getFirstChoice();
                                acceptedOptionNo = 0;
                                System.out.println("acceptedOption 0");
                            }else if (ppdbOptions.get(iOpt).get_id().equals(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getSecondChoice())){
                                System.out.println("acceptedOption 1");
                                scoreDistanceFinal = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getScoreJarak2();
                                acceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getSecondChoice();
                                acceptedOptionNo = 1;
                            }
                            else if (ppdbOptions.get(iOpt).get_id().equals(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getSecondChoice())){
                                System.out.println("acceptedOption 2");
                                scoreDistanceFinal = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getScoreJarak3();
                                acceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getThirdChoice();
                                acceptedOptionNo = 1;
                            }
                            else{
                                System.out.println("acceptedOption 3");
                                scoreDistanceFinal = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getScoreJarak3();

                                acceptedOptionId = ppdbOptions.get(ppdbOptions.size()-1).get_id();
                                acceptedOptionNo = 2;
                            }
                            System.out.println(" idxOptionDestinationOption:" + iOpt + " destinationOptionId:" + ppdbOptions.get(iOpt).get_id());

                            System.out.println(" akanDiTarikKemana:" + acceptedOptionNo + " < FromSourceNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionNo());
                            if (acceptedOptionNo < ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).getAcceptedOptionNo()) {

                                //update student history pada sekolah sebelummya (Source Option)
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList(), pullStudentIdx, acceptedOptionId, acceptedOptionNo, scoreDistanceFinal, pullOptionIdx, pullOptionIdx); //history siswa yang ketarik siswanya dirubah

                                //update student history pada active sekolah
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList(), pullStudentIdx, acceptedOptionId, acceptedOptionNo, scoreDistanceFinal, iOpt, pullOptionIdx); //history siswa disekolah yang menarik siswa dirubah

                                int idxFirstChoice = findData.findIdxFromOptionsByChoiceId(ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList().get(pullStudentIdx).getFirstChoice(), ppdbOptions);
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList(), pullStudentIdx, acceptedOptionId, acceptedOptionNo, scoreDistanceFinal, idxFirstChoice, pullOptionIdx); //ubah history siswa pilihan1

                                int idxSecondChoice = findData.findIdxFromOptionsByChoiceId(ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList().get(pullStudentIdx).getSecondChoice(), ppdbOptions);
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList(), pullStudentIdx, acceptedOptionId, acceptedOptionNo, scoreDistanceFinal, idxSecondChoice, pullOptionIdx); //ubah history siswa pilihan2

                                int idxLastChoice = ppdbOptions.size() - 1;
                                int idxStudentLastChoice = findData.findIdxFromRegistStudentsByStdIdandOptIdx(ppdbOptions, idxLastChoice, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iHistStd).get_id());
                                if (idxStudentLastChoice != -1){
                                    this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList(), pullStudentIdx, acceptedOptionId, acceptedOptionNo, scoreDistanceFinal, idxLastChoice, pullOptionIdx); //ubah history siswa pilihan terakhir
                                }

                                ppdbRegistration.setScoreDistanceFinal(scoreDistanceFinal);
                                ppdbRegistration.setAcceptedOptionNo(acceptedOptionNo);
                                ppdbRegistration.setAcceptedOptionId(acceptedOptionId);

                                ppdbOptions.get(iOpt).getPpdbRegistrationList().add(ppdbRegistration);
                                ppdbOptions.get(pullOptionIdx).getPpdbRegistrationList().remove(pullStudentIdx);

                                ppdbOptions.get(pullOptionIdx).setNeedFilter(true);
                                System.out.println("=============================================================================");
                                //ppdbView.displayOptionByIdx(ppdbOptions, pullOptionIdx);
                                //ppdbView.displayOptionByIdx(ppdbOptions, iOpt);
                            }
                    }

                } //end for loop registrationHistory

                ppdbOptions.get(iOpt).setNeedFilter(true);



            }

        }
    }

    private void processAddingQuota(List<PpdbOption> ppdbOptions, int iOpt, int optTargetIdx, int transferQuotaBasedOnNeed){
        System.out.println("ppdbOptions.get(iOpt).getQuota():" + ppdbOptions.get(iOpt).getQuota() + " + " + transferQuotaBasedOnNeed);
        int totalQuotaOptionCur = ppdbOptions.get(iOpt).getQuota() + transferQuotaBasedOnNeed;
        int totalQuotaTarget = ppdbOptions.get(optTargetIdx).getQuota() - transferQuotaBasedOnNeed;

        ppdbOptions.get(iOpt).setQuota(totalQuotaOptionCur);
        ppdbOptions.get(optTargetIdx).setQuota(totalQuotaTarget);

        ppdbOptions.get(iOpt).setQuotaBalance(ppdbOptions.get(iOpt).getQuotaBalance()+transferQuotaBasedOnNeed);

        System.out.println("actOption:" + ppdbOptions.get(iOpt).getName() +
                " q:" + ppdbOptions.get(iOpt).getQuota() +
                " qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance()
        );
        System.out.println("targetOption:" + ppdbOptions.get(optTargetIdx).getName() +
                " q:" + ppdbOptions.get(optTargetIdx).getQuota() +
                " qBalance:" + ppdbOptions.get(optTargetIdx).getQuotaBalance()
        );
    }

}
