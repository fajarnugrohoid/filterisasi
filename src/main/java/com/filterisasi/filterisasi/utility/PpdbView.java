package com.filterisasi.filterisasi.utility;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;

import java.util.List;

public class PpdbView {
    public PpdbView() {
    }

    public void displayOption(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("ppdbOption:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                    " # P:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() +
                    " # Q:" + ppdbOptions.get(iOpt).getQuota()
            );
        }
    }

    public void displayStudent(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("ppdbOption:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id());

            List<PpdbRegistration> ppdbRegistrations = ppdbOptions.get(iOpt).getPpdbRegistrationList();
            for (int std = 0; std <ppdbRegistrations.size() ; std++) {

                System.out.println("ppdbRegistrations1:" + std + "-" + ppdbRegistrations.get(std).get_id() + " - " +
                        ppdbRegistrations.get(std).getName() + " - " +
                        ppdbRegistrations.get(std).getSkorPeserta() + " - " +
                        ppdbRegistrations.get(std).getSkorJarak1());
            }
        }
    }

}
