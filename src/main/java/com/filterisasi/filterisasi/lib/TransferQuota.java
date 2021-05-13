package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransferQuota {

    private FindData findData;
    public TransferQuota(FindData findData) {
        this.findData = findData;
    }

    private void pelimpahanQuota(List<PpdbOption> ppdbOptions) {
        List<String> targetJalurs = new ArrayList<>();
        for (int iOpt = 0; iOpt < ppdbOptions.size(); iOpt++) {
            if (iOpt == ppdbOptions.size() - 1) continue; //jika sekolah buangan lewat


            if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("perpindahan")){
                targetJalurs.clear();
                targetJalurs.add("anak-guru");
                limpahkanQuota(ppdbOptions, iOpt, targetJalurs);
            } //end if perpindahan

            if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("anak-guru")){
                targetJalurs.clear();
                targetJalurs.add("perpindahan");
                limpahkanQuota(ppdbOptions, iOpt, targetJalurs);
            } //end if anak_guru

            if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("nhun")){
                targetJalurs.clear();
                targetJalurs.add("nhun-unggulan");
                targetJalurs.add("perpindahan");
                targetJalurs.add("anak-guru");
                limpahkanQuota(ppdbOptions, iOpt, targetJalurs);
            } //end if nhun

            if (ppdbOptions.get(iOpt).getType().equalsIgnoreCase("nhun-unggulan")){
                targetJalurs.clear();
                targetJalurs.add("nhun");
                targetJalurs.add("perpindahan");
                targetJalurs.add("anak-guru");
                limpahkanQuota(ppdbOptions, iOpt, targetJalurs);
            } //end if nhun-unggulan
        }
    }

    public void limpahkanQuota(List<PpdbOption> ppdbOptions, int iOpt, List<String> targetJalurs){

        for (int iJalur =0; iJalur <targetJalurs.size() ; iJalur++) {

            int jumlahPendaftar = ppdbOptions.get(iOpt).getPpdbRegistrationList().size();
            int quotaOption = ppdbOptions.get(iOpt).getQuota();
            System.out.println(iOpt + ppdbOptions.get(iOpt).getName() + " jumlahPendaftar:" + jumlahPendaftar + " > " + quotaOption);
            List<PpdbRegistration> students = ppdbOptions.get(iOpt).getPpdbRegistrationList();
            Collections.sort(students, new StudentComparator());

            if (jumlahPendaftar > quotaOption) { //butuh quota, cek ke nhun
                Integer optTargetIdx = this.findData.findOptionIdxByMajorIdandSchoolId(ppdbOptions.get(iOpt).getMajorId(), ppdbOptions.get(iOpt).getSchoolId(), targetJalurs.get(iJalur) , ppdbOptions);
                Integer kekuranganQuota = jumlahPendaftar - quotaOption;
                if (ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size() < ppdbOptions.get(optTargetIdx).getQuota()){
                    Integer targetQuota = ppdbOptions.get(optTargetIdx).getQuota();
                    Integer targetJumlahSiswa = ppdbOptions.get(optTargetIdx).getPpdbRegistrationList().size();
                    Integer sisaQuota = targetQuota - targetJumlahSiswa;
                    if (sisaQuota > kekuranganQuota){
                        ppdbOptions.get(optTargetIdx).setQuota(targetQuota-kekuranganQuota);
                        ppdbOptions.get(iOpt).setQuota(quotaOption + kekuranganQuota); //harusnya kasih seperlunya


                    }else{
                        ppdbOptions.get(optTargetIdx).setQuota(targetQuota-sisaQuota);
                        ppdbOptions.get(iOpt).setQuota(quotaOption + sisaQuota);

                    }
                }
            }
            ppdbOptions.get(iOpt).setCheckQuota(true);
        }
    }
}
