package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransferQuotaFromOneToOne {

    private FindData findData;
    public TransferQuotaFromOneToOne(FindData findData) {
        this.findData = findData;
    }


    public void checkIfAnyRemainingQuota(List<PpdbOption> ppdbOptions, String jalur1, String targetJalur){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (iOpt==ppdbOptions.size()-1) continue; //jika sekolah buangan lewat

            if ( ppdbOptions.get(iOpt).getType().equalsIgnoreCase(jalur1) ) {

                transferQuota(ppdbOptions, iOpt, targetJalur);

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
