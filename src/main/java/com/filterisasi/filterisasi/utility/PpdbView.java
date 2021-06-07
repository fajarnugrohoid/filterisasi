package com.filterisasi.filterisasi.utility;

import com.filterisasi.filterisasi.dto.PpdbFiltered;
import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.model.PpdbHistory;
import com.filterisasi.filterisasi.repository.PpdbFilteredRepository;
import com.filterisasi.filterisasi.repository.PpdbOptionLookupSchoolRepository;
import com.filterisasi.filterisasi.resource.RunnerSenior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PpdbView {

    private static final Logger logger = LoggerFactory.getLogger(PpdbView.class);

    public PpdbView() {

    }

    public void displayOption(List<PpdbOption> ppdbOptions){
        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if ( (ppdbOptions.get(iOpt).getType().equals("kondisi-tertentu") ) ||
                  (ppdbOptions.get(iOpt).getType().equals("ketm") ) ||
                  (iOpt == ppdbOptions.size()-1) ) {
                logger.debug("view-ppdbOption: {} - {} - {} - {} #P:{} - #Q:{} - NeedFilter:{}",
                        iOpt,
                        ppdbOptions.get(iOpt).get_id(),
                        ppdbOptions.get(iOpt).getName(),
                        ppdbOptions.get(iOpt).getPpdb_schools().get_Id(),
                        ppdbOptions.get(iOpt).getPpdbRegistrationList().size(),
                        ppdbOptions.get(iOpt).getQuota(),
                        ppdbOptions.get(iOpt).isNeedFilter()
                );
                System.out.println("view-ppdbOption:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id() + " - " +
                        ppdbOptions.get(iOpt).getName() + " - " +
                        ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                        " # P:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() +
                        " # Q:" + ppdbOptions.get(iOpt).getQuota() +
                        " # NeedFilter:" + ppdbOptions.get(iOpt).isNeedFilter()
                );
                this.displayStudent(ppdbOptions, iOpt);
            }
        }
    }

    public void displayStudent(List<PpdbOption> ppdbOptions, int iOpt){


            List<PpdbRegistration> ppdbRegistrations = ppdbOptions.get(iOpt).getPpdbRegistrationList();
            for (int std = 0; std <ppdbRegistrations.size() ; std++) {
                System.out.println("ppdbRegistrations:" + std + "-" + ppdbRegistrations.get(std).get_id() + " - " +
                        ppdbRegistrations.get(std).getName() + " - " +
                        ppdbRegistrations.get(std).getSkorPeserta() + " - " +
                        " fdis:" + ppdbRegistrations.get(std).getScoreDistanceFinal() + " - " +
                        ppdbRegistrations.get(std).getBirthDate() + " - " +
                        " accNo:" + ppdbRegistrations.get(std).getAcceptedOptionNo() + " " +
                        ppdbRegistrations.get(std).getJenjangPendaftaran() + " " +
                        ppdbRegistrations.get(std).getLevelPendaftaran() + " "
                );
            }

            /*
            **ori registration students
            ** */
            List<PpdbHistory> oriRegistrations = ppdbOptions.get(iOpt).getPpdbRegistrationHistories();
            for (int std = 0; std <oriRegistrations.size() ; std++) {
                //if (iOpt==ppdbOptions.size()-1) continue;

                System.out.println("Hist ppdbRegistrations:" + std + "-" + oriRegistrations.get(std).get_id() + " - " +
                        oriRegistrations.get(std).getName() + " - " +
                        oriRegistrations.get(std).isStudentExist() +
                        " accNo:" + oriRegistrations.get(std).getAcceptedOptionNo() +
                        " accId:" + oriRegistrations.get(std).getAcceptedOptionId()

                );
                for (int iHst = 0; iHst <oriRegistrations.get(std).getOptionHistories().size() ; iHst++) {
                    System.out.println("==>" + oriRegistrations.get(std).getOptionHistories().get(iHst).toString());
                }
            }


    }

    public void displayOptionByIdx(List<PpdbOption> ppdbOptions, int iOpt){

            System.out.println("view-ppdbOption:" + iOpt + "-" + ppdbOptions.get(iOpt).get_id()  + " - " +
                    ppdbOptions.get(iOpt).getName() +  " - " +
                    ppdbOptions.get(iOpt).getPpdb_schools().get_Id() +
                    " # P:" + ppdbOptions.get(iOpt).getPpdbRegistrationList().size() +
                    " # Q:" + ppdbOptions.get(iOpt).getQuota()
            );
            this.displayStudent(ppdbOptions, iOpt);

    }



}
