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

    public void transferQuotaBalance(List<PpdbOption> ppdbOptions, int iOpt, Integer optTargetIdx){


        //checkQuotaBalance.TransferQuotaBalance(ppdbOptions, iOpt, optTargetIdx);
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
                //ppdbOptions.get(optTargetIdx).setQuotaBalance(ppdbOptions.get(optTargetIdx).getQuotaBalance()-transferQuotaBasedOnNeed);

                System.out.println("actOption:" + ppdbOptions.get(iOpt).getName() +
                        " q:" + ppdbOptions.get(iOpt).getQuota() +
                        " qBalance:" + ppdbOptions.get(iOpt).getQuotaBalance()
                );
                System.out.println("targetOption:" + ppdbOptions.get(optTargetIdx).getName() +
                        " q:" + ppdbOptions.get(optTargetIdx).getQuota() +
                        " qBalance:" + ppdbOptions.get(optTargetIdx).getQuotaBalance()
                );

                for (int iStd = 0; iStd <ppdbOptions.get(iOpt).getPpdbRegistrationList().size() ; iStd++) {
                    System.out.println("==>reg-act:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(iStd).getName());
                }
                for (int iStd = 0; iStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iStd++) {
                    System.out.println("==>hist-act:" +
                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iStd).getName()+
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iStd).getAcceptedOptionId()+
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iStd).getAcceptedOptionNo() +
                            " first:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iStd).getFirstChoice() +
                            " sec:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iStd).getSecondChoice()

                    );
                }

                //ceritanya sekolah ini kekurangan kuota, kemudian lihat sekolah lawan yang merger kuota, ada kuota yang tidak terpakai atau tidak
                //jika ada, maka cek siswa history dari sekolah ini, siswa siapa aja yang terlempar ke pilihan 2 atau 3

                for (int iOriStd = 0; iOriStd <ppdbOptions.get(iOpt).getPpdbRegistrationHistories().size() ; iOriStd++) {

                    //List<ObjectId> oriStdHistories = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getOptionHistories();
                    System.out.println("==>hist:" +
                            ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                            "-isExist:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).isStudentExist() +
                            " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId() +
                            " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo() +
                            " first:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice() +
                            " second:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice() +
                            " ==iOpt:" + ppdbOptions.get(iOpt).get_id()
                    );
                    if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).isStudentExist()==false){ //cari siswa di history yang tidak ada disekolah tersebut

                        int acceptedOptionNo = 0;
                        //if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()!=0){ //kudu di cek lagi, jika siswa diterima dipilihan pertama, maka jangan ditarik lagi
                            //cek siswa yang terlempar apakah active option sekarang untuk dia, masuk ke pilihan berapa

                            if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()==0){

                            }else if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()==1){
                                acceptedOptionNo = 0;
                            }else if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()==2){
                                acceptedOptionNo = 1;
                            }else if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionNo()==3){
                                acceptedOptionNo = 2;
                            }

                            //cek pilihan pada siswa ini sama dengan pilihan pertama, artinya siswa tsb kembali ke pilihan 1
                            if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getFirstChoice().equals(ppdbOptions.get(iOpt).get_id())){
                                System.out.println("==>" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                        " siswa kembali ke pilihan pertama");
                                acceptedOptionNo=0;
                            }

                            //cek pilihan pada siswa ini sama dengan pilihan kedua, artinya siswa ini kembali ke pilihan ke dua
                            else if (ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getSecondChoice().equals(ppdbOptions.get(iOpt).get_id())){
                                System.out.println("==>" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                        " siswa kembali ke pilihan kedua");
                                acceptedOptionNo=1;
                            }
                            else{
                                //jika pilihan3 atau tempat pembuangan
                                System.out.println("==>" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                        " pilihan siswa sama dengan pilihan ketiga/terakhir");

                            }

                            ObjectId acceptedOptionId = ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getAcceptedOptionId();

                            int optionIdxLemparan = this.findData.findIndexFromOptionsByChoice(acceptedOptionId, ppdbOptions);
                            //int optionIdxFirstChoice = this.findData.findOptionIdxByChoice(oriStdfirstChoice, ppdbOptions);
                            System.out.println("SiswaYangDipilihYgAkanDiambil:" + ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() +
                                    " saatIniDiSekolah:" + acceptedOptionId +
                                    " namaSekolahYgAkanDiambilSiswanya:" + ppdbOptions.get(optionIdxLemparan).getName() +
                                    " idxSekolahDariSekolahYgDiambil:" + optionIdxLemparan +
                                    " regisListDariSekolahYgAkanDiambil:" + ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList().size()
                            );


                            List<PpdbRegistration> studentListPull = new ArrayList<>();
                            studentListPull = ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList();
                            for (int iStdLemparan = 0; iStdLemparan <studentListPull.size() ; iStdLemparan++) {
                                System.out.println("SiswaYgAdaDiSekolahYgAkanDiambil:" + studentListPull.get(iStdLemparan).getName());
                            }
                            for (int iStdLemparan = 0; iStdLemparan <studentListPull.size() ; iStdLemparan++) {

                                System.out.println("==>checkSiswaDariSekolahYgAkanDiambil:" +
                                        studentListPull.get(iStdLemparan).getName() + "==" +
                                        ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).getName() + " && " +
                                        studentListPull.get(iStdLemparan).get_id() + "==" +
                                        ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id()
                                );
                                if (studentListPull.get(iStdLemparan).get_id().equals(ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).get_id()) ){

                                    ppdbOptions.get(iOpt).getPpdbRegistrationHistories().get(iOriStd).setStudentExist(true);


                                    studentListPull.get(iStdLemparan).setAcceptedOptionId(ppdbOptions.get(iOpt).get_id());
                                    studentListPull.get(iStdLemparan).setAcceptedOptionNo(acceptedOptionNo);
                                    ppdbOptions.get(iOpt).getPpdbRegistrationList().add(studentListPull.get(iStdLemparan));
                                    ppdbOptions.get(iOpt).setNeedFilter(true);

                                    System.out.println("tarikSiswaDariSekolah:" +
                                            ppdbOptions.get(optionIdxLemparan).getName() +
                                            " - " + studentListPull.get(iStdLemparan).getName()
                                    );

                                    for (int iStd = 0; iStd < studentListPull.size() ; iStd++) {
                                        System.out.println("==>reg-lemparan-tarik-start:" + studentListPull.get(iStd).getName());
                                    }

                                    int idxStudentTargetHistory = studentHistory.findIndexStudentHistoryById(ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories(), ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationList().get(iStdLemparan).get_id());
                                    int idxStudentTargetCur = findData.findIndexFromStudentsById(studentListPull, studentListPull.get(iStdLemparan).get_id());
                                    System.out.println("idxStudentTargetCur:" + idxStudentTargetCur);
                                    System.out.println("idxStudentTargetHistory:" + idxStudentTargetHistory);

                                    for (int i = 0; i <ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().size() ; i++) {
                                        System.out.println("updateHistoryStudentExist:" +
                                                " " + i + " " +
                                                ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().get(i).getName() +
                                                " exist:" + ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().get(i).isStudentExist()

                                        );
                                    }

                                    //ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().get(idxStudentTargetHistory).setStudentExist(false);
                                    studentHistory.addOrUpdateHistoryStudentAllOptions2(ppdbOptions, optionIdxLemparan, idxStudentTargetHistory);
                                    //ppdbOptions.get(optionIdxLemparan).setNeedFilter(true);

                                    studentListPull.remove(idxStudentTargetCur);

                                    for (int iStd = 0; iStd < studentListPull.size() ; iStd++) {
                                        System.out.println("==>reg-lemparan-tarik-end:" + studentListPull.get(iStd).getName());
                                    }

                                    for (int iStd = 0; iStd < ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().size() ; iStd++) {
                                        System.out.println("==>hist-lemparan-tarik-end:" +
                                                ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().get(iStd).getName() +
                                                " isExist:" + ppdbOptions.get(optionIdxLemparan).getPpdbRegistrationHistories().get(iStd).isStudentExist()

                                        );
                                    }

                                    ppdbOptions.get(optionIdxLemparan).setPpdbRegistrationList(studentListPull);

                                    System.out.println("actSekolahSetelahSiswaDipindahkan:" + ppdbOptions.get(iOpt).get_id() + " " + ppdbOptions.get(iOpt).getName());
                                    for (int i = 0; i <ppdbOptions.get(iOpt).getPpdbRegistrationList().size() ; i++) {
                                        System.out.println(
                                                "==>" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(i).getName() +
                                                " accId:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(i).getAcceptedOptionId() +
                                                " accNo:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().get(i).getAcceptedOptionNo()
                                        );
                                    }

                                    break;
                                }
                            }
                        //}
                    } //end cari siswa yang tidak ada di sekolah tersebut untuk ditarik lagi


                } //end for loop registrationHistory



            }

        }
    }

}
