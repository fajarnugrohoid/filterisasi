package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.model.PpdbHistory;
import org.bson.types.ObjectId;

import java.util.List;

public class UpdateData {

    public UpdateData() {
    }

    public PpdbOption updateOption(List<PpdbOption> ppdbOptions, int idxTargetOption){
        PpdbOption ppdbOption = new PpdbOption();

        ppdbOption.set_id(ppdbOptions.get(idxTargetOption).get_id());
        ppdbOption.setSchoolId(ppdbOptions.get(idxTargetOption).getSchoolId());
        ppdbOption.setMajorId(ppdbOptions.get(idxTargetOption).getMajorId());
        ppdbOption.setPpdb_schools(ppdbOptions.get(idxTargetOption).getPpdb_schools());
        ppdbOption.setName(ppdbOptions.get(idxTargetOption).getName());
        ppdbOption.setType(ppdbOptions.get(idxTargetOption).getType());
        ppdbOption.setQuota(ppdbOptions.get(idxTargetOption).getQuota());
        ppdbOption.setQuotaBalance(ppdbOptions.get(idxTargetOption).getQuotaBalance());
        ppdbOption.setPpdbRegistrationList(ppdbOptions.get(idxTargetOption).getPpdbRegistrationList());
        ppdbOption.setPpdbRegistrationHistories(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories());
        ppdbOption.setNeedFilter(true);
        return ppdbOption;
    }
    
    public PpdbHistory updateStudent(List<PpdbOption> ppdbOptions, int idxOptionFirstChoice, int idxCurStudent,
                                     ObjectId acceptedOptionId, int acceptedOptionNo

    ){

        PpdbHistory historyTarget = new PpdbHistory();
        //historyTarget.setStudentExist(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).isStudentExist());
        historyTarget.setAcceptedOptionId(acceptedOptionId);
        historyTarget.setAcceptedOptionNo(acceptedOptionNo);
        historyTarget.setFirstChoice(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getFirstChoice());
        historyTarget.setSecondChoice(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getSecondChoice());
        historyTarget.setThirdChoice(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getThirdChoice());
        historyTarget.set_id(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).get_id());
        historyTarget.setName(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getName());
        historyTarget.setSkorPeserta(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getSkorPeserta());
        historyTarget.setScoreJarak1(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreJarak1());
        historyTarget.setScoreJarak2(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreJarak2());
        historyTarget.setScoreJarak3(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreJarak3());
        historyTarget.setScoreDistanceFinal(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreDistanceFinal());
        historyTarget.setScoreAge(ppdbOptions.get(idxOptionFirstChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreAge());

        return historyTarget;
    }

    public PpdbHistory updateHistoryStudent(List<PpdbOption> ppdbOptions, int idxOptionTargetChoice, int idxCurStudent,
                                     ObjectId acceptedOptionId, int acceptedOptionNo

    ){

        PpdbHistory historyTarget = new PpdbHistory();
        historyTarget.setAcceptedOptionId(acceptedOptionId);
        historyTarget.setAcceptedOptionNo(acceptedOptionNo);
        historyTarget.setFirstChoice(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getFirstChoice());
        historyTarget.setSecondChoice(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getSecondChoice());
        historyTarget.setThirdChoice(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getThirdChoice());
        historyTarget.set_id(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).get_id());
        historyTarget.setName(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getName());
        historyTarget.setSkorPeserta(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getSkorPeserta());
        historyTarget.setScoreJarak1(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreJarak1());
        historyTarget.setScoreJarak2(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreJarak2());
        historyTarget.setScoreJarak3(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreJarak3());
        historyTarget.setScoreDistanceFinal(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreDistanceFinal());
        historyTarget.setScoreAge(ppdbOptions.get(idxOptionTargetChoice).getPpdbRegistrationHistories().get(idxCurStudent).getScoreAge());

        return historyTarget;
    }

}
