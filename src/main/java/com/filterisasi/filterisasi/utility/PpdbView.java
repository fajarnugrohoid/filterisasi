package com.filterisasi.filterisasi.utility;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;

import java.util.List;

public class PpdbView {
    public PpdbView() {
    }

    public void displayOption(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            System.out.println("view-ppdbOption:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                    " # P:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() +
                    " # Q:" + ppdbOptions.get(iOpt).getQuota()
            );
            this.displayStudent(ppdbOptions, iOpt);
        }
    }

    public void displayStudent(List<PpdbOption> ppdbOptions, Integer iOpt){


            List<PpdbRegistration> ppdbRegistrations = ppdbOptions.get(iOpt).getPpdbRegistrationList();
            for (int std = 0; std <ppdbRegistrations.size() ; std++) {
                System.out.println("ppdbRegistrations:" + std + "-" + ppdbRegistrations.get(std).get_id() + " - " +
                        ppdbRegistrations.get(std).getName() + " - " +
                        ppdbRegistrations.get(std).getSkorPeserta() + " - " +
                        ppdbRegistrations.get(std).getSkorJarak1());
            }

            /*
            **ori registration students
            **
            List<PpdbRegistration> oriRegistrations = ppdbOptions.get(iOpt).getOriRegistrationList();
            for (int std = 0; std <oriRegistrations.size() ; std++) {
                if (iOpt==ppdbOptions.size()-1) continue;

                System.out.println("ori ppdbRegistrations:" + std + "-" + oriRegistrations.get(std).get_id() + " - " +
                        oriRegistrations.get(std).getName() + " - " +
                        oriRegistrations.get(std).getOptionHistories()

                );
                for (int iHst = 0; iHst <oriRegistrations.get(std).getOptionHistories().size() ; iHst++) {
                    System.out.println("==>" + oriRegistrations.get(std).getOptionHistories().get(iHst).toString());
                }
            }*/

    }

}
