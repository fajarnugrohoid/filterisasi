package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.lib.FindData;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class CheckQuotaBalance {
    private FindData findData;
    public CheckQuotaBalance(FindData findData) {
        this.findData = findData;
    }

    public void TransferQuotaBalance(List<PpdbOption> ppdbOptions, int iOpt, Integer optTargetIdx){
        System.out.println( "checkQuotaBalance " +  ppdbOptions.get(iOpt).getName() + ":" + ppdbOptions.get(iOpt).getQuotaBalance() + " && " + ppdbOptions.get(optTargetIdx).getQuotaBalance());
        if ((ppdbOptions.get(iOpt).getQuotaBalance() < 0) && (ppdbOptions.get(optTargetIdx).getQuotaBalance() > 0 ) ){

            //search siswa yang sudah terlempar, dengan tracking dari ori registration

            if (ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx).getQuota()) {
                System.out.println("set quota balance:" + ppdbOptions.get(iOpt).getName() +
                        " oriStudent:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size()
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


                //cek siswa yang pil1 ke active option(sekolah yang sedang di looping)
                //cek siswa yang pil2 ke active option(sekolah yang sedang di looping)
                //atau cek siswa yang pernah terlempar ke sekolah ini active option(sekolah yang sedang di looping)
                //caranya setiap siswa yang terlempar, siswa tsb simpan saja di history
                for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iOriStd++) {
                    List<ObjectId> oriStdHistories = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getOptionHistories();
                    ObjectId oriStdfirstChoice = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice();


                    if (oriStdHistories.size()>0){
                        Integer optionIdxLemparan = this.findData.findIndexFromOptionsByChoice(oriStdHistories.get(oriStdHistories.size()-1), ppdbOptions);
                        Integer optionIdxFirstChoice = this.findData.findIndexFromOptionsByChoice(oriStdfirstChoice, ppdbOptions);

                        List<PpdbRegistration> ppdbRegistrationList = new ArrayList<>();
                        ppdbRegistrationList.clear();
                        ppdbRegistrationList.addAll(ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList());
                        for (int iStdLemparan = 0; iStdLemparan <ppdbRegistrationList.size() ; iStdLemparan++) {
                            //cek id siswa sama atau tidak dengan yang di ori siswa
                            if (ppdbRegistrationList.get(iStdLemparan).get_id().equals(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id())){

                                List<PpdbRegistration> firstChoiceRegistrationList = new ArrayList<>();
                                firstChoiceRegistrationList.clear();
                                System.out.println("optFirstChoice" + ppdbOptions.get(optionIdxFirstChoice).getName());
                                for (int i = 0; i <ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList().size() ; i++) {
                                    System.out.println("bef==>" + ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList().get(i).getName());
                                }

                                firstChoiceRegistrationList.addAll(ppdbOptions.get(optionIdxFirstChoice).getPpdbRegistrationList());
                                firstChoiceRegistrationList.add(ppdbRegistrationList.get(iStdLemparan));

                                System.out.println("tarik:" + ppdbRegistrationList.get(iStdLemparan).getName() + " firstChoiceRegistrationList:" + firstChoiceRegistrationList.size());
                                ppdbRegistrationList.get(iStdLemparan).setAcceptedOptionNo(0);


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
    }

}
