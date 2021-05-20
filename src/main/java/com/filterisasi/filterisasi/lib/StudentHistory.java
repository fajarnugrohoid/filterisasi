package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.bson.types.ObjectId;

import java.util.List;

public class StudentHistory {

    public StudentHistory() {
    }

    public void addStudentHistory(List<PpdbOption> ppdbOptions, Integer idxTargetOption, PpdbRegistration historyTarget){

        boolean isExist = false;
        for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size(); i++) {
            System.out.println("addStudentHistory:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getName() + " "
                    + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).get_id() + "==" + historyTarget.get_id());
            if (ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).get_id().equals(historyTarget.get_id())){
                ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).setStudentExist(true);
                isExist = true;
                break;
            }
        }

        if (isExist==false){
            System.out.println("addStudentHistory adding");
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().add(historyTarget);
        }

    }

    public int findIndexStudentHistoryById(List<PpdbRegistration> ppdbRegistrationHistories, ObjectId studentId){
        for (int iStd = 0; iStd <ppdbRegistrationHistories.size() ; iStd++) {
            if (ppdbRegistrationHistories.get(iStd).get_id().equals(studentId)){
                return iStd;
            }
        }
        return -1;
    }

    public void removeStudentHistory(){

    }

}
