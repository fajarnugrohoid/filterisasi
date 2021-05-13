package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import org.bson.types.ObjectId;

import java.util.List;

public class FindData {

    public FindData() {
    }


    public Integer findOptionIdxByChoice(ObjectId nextChoice, List<PpdbOption> ppdbOptions) {

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


}
