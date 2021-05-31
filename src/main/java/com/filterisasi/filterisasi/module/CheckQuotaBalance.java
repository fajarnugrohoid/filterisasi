package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentHistory;
import com.filterisasi.filterisasi.utility.PpdbView;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CheckQuotaBalance {
    private FindData findData;
    private StudentHistory studentHistory;
    private PpdbView ppdbView;
    public CheckQuotaBalance(FindData findData, StudentHistory studentHistory, PpdbView ppdbView) {
        this.findData = findData;
        this.studentHistory = studentHistory;
        this.ppdbView = ppdbView;
    }

    public void transferQuotaBalance(List<PpdbOption> ppdbOptions, int iOpt, int optTargetIdx){


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
                }else{
                    transferQuotaBasedOnNeed = avalaibleQuotaTarget;
                }
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

                for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iOriStd++) {
                    System.out.println("hist:"+
                            " optionId:" + ppdbOptions.get(iOpt).get_id() +
                            " studentId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id() +
                            " name:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId() +
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo() +
                            " first:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice() +
                            " sec:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice()
                    );



                    ObjectId objectIdOptionTarik = null;
                    int idxOptionTarik = -1;
                    int idxStudentTarik = -1;

                    //cari siswa yg ada di history student, tapi tidak ada di registration
                    //ppdbView.displayOption(ppdbOptions);
                    if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId()!=ppdbOptions.get(iOpt).get_id()){
                        System.out.println("YgAKanDitarik==>" +
                                ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId()
                        );

                        if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()!=0){

                        ObjectId histAcceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId();
                        idxOptionTarik = findData.findIndexFromOptionsByChoice(histAcceptedOptionId, ppdbOptions);
                        idxStudentTarik = findData.findIndexFromStudentsByIdandOption(ppdbOptions, idxOptionTarik, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());

                        System.out.println(" ==>StdId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id() +
                                " name:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                " idxOptionTarik:" + idxOptionTarik + " " + histAcceptedOptionId + " idxStudentTarik:" + idxStudentTarik);

                        //ppdbView.displayOptionByIdx(ppdbOptions, idxOptionTarik);
                        //ppdbView.displayOptionByIdx(ppdbOptions, iOpt);

                        PpdbRegistration ppdbRegistration = ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik);


                        ObjectId acceptedOptionId = ppdbRegistration.getAcceptedOptionId();
                        int acceptedOptionNo = ppdbRegistration.getAcceptedOptionNo();

                        //perbedaan
                        //ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo() 0
                        //dengan ini
                        //ppdbRegistration.getAcceptedOptionNo() 0

                        //int idxStudentYgTelahDitarik = findData.findIndexFromStudentsByIdandOption(ppdbOptions, iOpt, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());



                            //cek dia saat ini ketarik ke pilihan yang mana?
                            if (ppdbOptions.get(iOpt).get_id()==ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik).getFirstChoice()){
                                acceptedOptionId = ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik).getFirstChoice();
                                acceptedOptionNo = 0;
                                System.out.println("acceptedOption 1");
                            }else if (ppdbOptions.get(iOpt).get_id()==ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik).getSecondChoice()){
                                System.out.println("acceptedOption 2");
                                acceptedOptionId = ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik).getSecondChoice();
                                acceptedOptionNo = 1;
                            }else{
                                System.out.println("acceptedOption 3");
                                acceptedOptionId = ppdbOptions.get(ppdbOptions.size()-1).get_id();
                                acceptedOptionNo = 3;
                            }

                            if (acceptedOptionNo < ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()) {

                                ppdbOptions.get(iOpt).getPpdbRegistrationList().add(ppdbRegistration);
                                ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().remove(idxStudentTarik);

                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), idxStudentTarik, acceptedOptionId, acceptedOptionNo, idxOptionTarik); //history siswa yang ketarik siswanya dirubah

                                //int idxActiveStudent = findData.findIndexFromStudentsByIdandOption(ppdbOptions, iOpt, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), idxStudentTarik, acceptedOptionId, acceptedOptionNo, iOpt); //history siswa disekolah yang menarik siswa dirubah

                                int idxFirstChoice = findData.findIndexFromOptionsByChoice(ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik).getFirstChoice(), ppdbOptions);
                                //int idxStudentFirstChoice = findData.findIndexFromStudentsByIdandOption(ppdbOptions, idxFirstChoice, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), idxStudentTarik, acceptedOptionId, acceptedOptionNo, idxFirstChoice); //ubah history siswa pilihan1

                                int idxSecondChoice = findData.findIndexFromOptionsByChoice(ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik).getSecondChoice(), ppdbOptions);

                                int idxStudentSecondChoice = findData.findIndexFromStudentsByIdandOption(ppdbOptions, idxSecondChoice, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());
                                this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), idxStudentTarik, acceptedOptionId, acceptedOptionNo, idxSecondChoice); //ubah history siswa pilihan2

                                int idxLastChoice = ppdbOptions.size() - 1;
                                int idxStudentLastChoice = findData.findIndexFromStudentsByIdandOption(ppdbOptions, idxLastChoice, ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());
                                if (idxStudentLastChoice != -1)
                                    this.studentHistory.pullStudentHistory(ppdbOptions, ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), idxStudentTarik, acceptedOptionId, acceptedOptionNo, idxLastChoice); //ubah history siswa pilihan terakhir


                                ppdbOptions.get(idxOptionTarik).setNeedFilter(true);
                                System.out.println("=============================================================================");
                                //ppdbView.displayOptionByIdx(ppdbOptions, idxOptionTarik);
                                //ppdbView.displayOptionByIdx(ppdbOptions, iOpt);
                            }
                        }

                    }

                } //end for loop registrationHistory

                ppdbOptions.get(iOpt).setNeedFilter(true);



            }

        }
    }

}
