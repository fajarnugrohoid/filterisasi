package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import org.bson.types.ObjectId;

import java.util.List;

public class FindData {

    public FindData() {
    }


    public Integer findIndexFromOptionsByChoice(ObjectId nextChoice, List<PpdbOption> ppdbOptions) {

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (ppdbOptions.get(iOpt).get_id().equals(nextChoice)) {
                return iOpt;
            }
        }
        return ppdbOptions.size()-1; //lempar ke sekolah buangan
    }

    public Integer findOptionIdxByMajorIdandSchoolId(Integer majorId, ObjectId schoolId, String jalur, List<PpdbOption> ppdbOptions) {

        for (int iOpt = 0; iOpt <ppdbOptions.size() ; iOpt++) {
            if (
                    (ppdbOptions.get(iOpt).getSchoolId().equals(schoolId))  &&
                            (ppdbOptions.get(iOpt).getMajorId()==majorId) &&
                            (ppdbOptions.get(iOpt).getType().equals(jalur))
            ) {
                return iOpt;
            }
        }
        return null;
    }

    public Integer findIndexFromStudentsById(List<PpdbRegistration> ppdbRegistrations, ObjectId studentId){
        for (int iStd = 0; iStd <ppdbRegistrations.size() ; iStd++) {
            //System.out.println("findStudentIdxById:" + ppdbRegistrations.get(iStd).get_id() + "==" + studentId);
            if (ppdbRegistrations.get(iStd).get_id()==studentId){
                return iStd;
            }
        }
        return null;
    }

    public Integer findIndexStudentHistoryById(List<PpdbRegistration> ppdbRegistrationHistories, ObjectId studentId){
        for (int iStd = 0; iStd <ppdbRegistrationHistories.size() ; iStd++) {
            if (ppdbRegistrationHistories.get(iStd).get_id()==studentId){
                return iStd;
            }
        }
        return null;
    }


}
