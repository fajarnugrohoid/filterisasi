package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import com.filterisasi.filterisasi.lib.StudentHistory;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CheckQuotaBalance {
    private FindData findData;
    private StudentHistory studentHistory;
    public CheckQuotaBalance(FindData findData, StudentHistory studentHistory) {
        this.findData = findData;
        this.studentHistory = studentHistory;
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

                System.out.println("SekolahYgAkanMenarikSiswa:" + ppdbOptions.get(iOpt).getName());
                for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getPpdbRegistrationList().size() ; iOriStd++) {
                    System.out.println("reg:"+
                            ppdbOptions.get(iOpt).getPpdbRegistrationList().get(iOriStd).getName() +
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(iOriStd).getAcceptedOptionId() +
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(iOriStd).getAcceptedOptionNo()

                    );
                }

                for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iOriStd++) {
                    System.out.println("hist:"+
                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id() +
                            " name:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId() +
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo() +
                            " first:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice() +
                            " sec:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice()
                    );

                    ObjectId objectIdOptionTarik = null;
                    int idxOptionTarik = -1;
                    int idxStudentTarik = -1;

                    //jika accNo=1, siswa tsb ada di secondChoice
                    if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()==1){
                        System.out.println("tarik kembali ke pilihan");

                        objectIdOptionTarik = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice();
                        idxOptionTarik = findData.findIndexFromOptionsByChoice(objectIdOptionTarik, ppdbOptions);
                        idxStudentTarik = findData.findIndexFromStudentsById(ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());
                        /*
                        System.out.println("idxOptionTarik:" + idxOptionTarik);
                        System.out.println("idxStudentTarik:" + idxStudentTarik);
                        for (int i = 0; i <ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().size() ; i++) {
                            System.out.println("SiswaYgAkanDiTarik:" +
                                    ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(i).get_id() + " " +
                                    ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(i).getName()
                            );
                        }*/
                        PpdbRegistration siswaYgDitarik = ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik);
                        siswaYgDitarik.setAcceptedOptionNo(0);
                        siswaYgDitarik.setAcceptedOptionId(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice());

                        ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).setAcceptedOptionNo(0);
                        ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).setAcceptedOptionId(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice());

                        ppdbOptions.get(iOpt).getPpdbRegistrationList().add(siswaYgDitarik);
                        ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().remove(idxStudentTarik);


                        ppdbOptions.get(idxOptionTarik).setNeedFilter(true);
                    }

                    //jika accNo=2, siswa tsb ada di thirdChoice/buangan
                    if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()==2){
                        System.out.println("tarik kembali ke pilihan");

                        //objectIdOptionTarik = ppdbOptions.size()-1;
                        idxOptionTarik = ppdbOptions.size()-1;
                        idxStudentTarik = findData.findIndexFromStudentsById(ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList(), ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id());

                        PpdbRegistration siswaYgDitarik = ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().get(idxStudentTarik);
                        siswaYgDitarik.setAcceptedOptionNo(1);
                        siswaYgDitarik.setAcceptedOptionId(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice());

                        ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).setAcceptedOptionNo(1);
                        ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).setAcceptedOptionId(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice());

                        ppdbOptions.get(iOpt).getPpdbRegistrationList().add(siswaYgDitarik);
                        ppdbOptions.get(idxOptionTarik).getPpdbRegistrationList().remove(idxStudentTarik);
                    }
                    System.out.println("==>" +
                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() + " " +
                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()
                    );

                } //end for loop registrationHistory

                ppdbOptions.get(iOpt).setNeedFilter(true);



            }

        }
    }

}
