package com.filterisasi.filterisasi.lib;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.model.PpdbHistory;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Stream;

public class StudentHistory {
    private PpdbHistory historyTarget;
    private FindData findData;
    private UpdateData updateData;
    public StudentHistory(FindData findData, UpdateData updateData) {
        this.findData = findData;
        this.updateData = updateData;
    }

    public void addOrUpdateHistoryStudentAllOptions2(List<PpdbOption> ppdbOptions, int idxTargetOption, int idxStudentTargetHistory) {
        historyTarget = new PpdbHistory();
        historyTarget.setStudentExist(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).isStudentExist());
        historyTarget.setAcceptedOptionId(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getAcceptedOptionId());
        historyTarget.setAcceptedOptionNo(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getAcceptedOptionNo());
        historyTarget.setFirstChoice(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getFirstChoice());
        historyTarget.setSecondChoice(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSecondChoice());
        historyTarget.set_id(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).get_id());
        historyTarget.setName(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getName());
        historyTarget.setSkorPeserta(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSkorPeserta());
        historyTarget.setSkorJarak1(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSkorJarak1());

        PpdbHistory updateHistoryOtherTarget2 = new PpdbHistory();
        updateHistoryOtherTarget2.setStudentExist(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).isStudentExist());
        updateHistoryOtherTarget2.setAcceptedOptionId(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getAcceptedOptionId());
        updateHistoryOtherTarget2.setAcceptedOptionNo(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getAcceptedOptionNo());
        updateHistoryOtherTarget2.setFirstChoice(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getFirstChoice());
        updateHistoryOtherTarget2.setSecondChoice(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSecondChoice());
        updateHistoryOtherTarget2.set_id(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).get_id());
        updateHistoryOtherTarget2.setName(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getName());
        updateHistoryOtherTarget2.setSkorPeserta(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSkorPeserta());
        updateHistoryOtherTarget2.setSkorJarak1(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSkorJarak1());


        addOrUpdateHistoryStudent2(ppdbOptions, idxTargetOption, idxStudentTargetHistory, historyTarget);

        int idxTargetOption1 = findData.findIndexFromOptionsByChoice(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getFirstChoice(),ppdbOptions);
        if (idxTargetOption1!=-1){
            addOrUpdateHistoryStudent2(ppdbOptions, idxTargetOption1, idxStudentTargetHistory, updateHistoryOtherTarget2);
        }

        int idxTargetOption2 = findData.findIndexFromOptionsByChoice(ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(idxStudentTargetHistory).getSecondChoice(),ppdbOptions);
        if (idxTargetOption2!=-1){
            addOrUpdateHistoryStudent2(ppdbOptions, idxTargetOption2, idxStudentTargetHistory, updateHistoryOtherTarget2);
        }


        int idxTargetOption3 = ppdbOptions.size()-1;
        addOrUpdateHistoryStudent2(ppdbOptions, idxTargetOption3, idxStudentTargetHistory, updateHistoryOtherTarget2);

    }


    public void addOrUpdateHistoryStudent2(List<PpdbOption> ppdbOptions, int idxTargetOption, int idxStudentTargetHistory, PpdbHistory historyTarget){


        boolean isExist = false;
            System.out.println("addStudentHistory:" + ppdbOptions.get(idxTargetOption).getName() +
                " histSize:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size());
        for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size(); i++) {
            System.out.println("addStudentHistory:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getName() + " "
                    + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).get_id() + "==" + historyTarget.get_id() );
            if (ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).get_id().equals(historyTarget.get_id()) ){
                System.out.println("addStudentHistory==>" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).isStudentExist());
                //historyTarget.setStudentExist(true);
                ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().set(i, historyTarget);
                isExist = true;
                break;
            }
        }

        if (isExist==false){
            System.out.println("addStudentHistory adding");
            //historyTarget.setStudentExist(true);
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().add(historyTarget);
        }

    }

    public void addOrUpdateHistoryStudentAllOptions1(List<PpdbOption> ppdbOptions, int idxTargetOption, int actOption, int actStudent){
        historyTarget = new PpdbHistory();
        historyTarget.setStudentExist(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).isStudentExist());
        historyTarget.setAcceptedOptionId(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getAcceptedOptionId());
        historyTarget.setAcceptedOptionNo(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getAcceptedOptionNo());
        historyTarget.setFirstChoice(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getFirstChoice());
        historyTarget.setSecondChoice(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSecondChoice());
        historyTarget.set_id(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).get_id());
        historyTarget.setName(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getName());
        historyTarget.setSkorPeserta(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSkorPeserta());
        historyTarget.setSkorJarak1(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSkorJarak1());
        historyTarget.setStudentExist(true);

        PpdbHistory updateHistoryOtherTarget = new PpdbHistory();
        updateHistoryOtherTarget.setStudentExist(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).isStudentExist());
        updateHistoryOtherTarget.setAcceptedOptionId(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getAcceptedOptionId());
        updateHistoryOtherTarget.setAcceptedOptionNo(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getAcceptedOptionNo());
        updateHistoryOtherTarget.setFirstChoice(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getFirstChoice());
        updateHistoryOtherTarget.setSecondChoice(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSecondChoice());
        updateHistoryOtherTarget.set_id(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).get_id());
        updateHistoryOtherTarget.setName(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getName());
        updateHistoryOtherTarget.setSkorPeserta(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSkorPeserta());
        updateHistoryOtherTarget.setSkorJarak1(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSkorJarak1());
        updateHistoryOtherTarget.setStudentExist(false);

        addOrUpdateHistoryStudent1(ppdbOptions, idxTargetOption, actOption, actStudent, historyTarget);


        int idxTargetOption1 = findData.findIndexFromOptionsByChoice(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getFirstChoice(),ppdbOptions);
        if (idxTargetOption1!=-1){
            addOrUpdateHistoryStudent1(ppdbOptions, idxTargetOption1, actOption, actStudent, updateHistoryOtherTarget);
        }

        int idxTargetOption2 = findData.findIndexFromOptionsByChoice(ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent).getSecondChoice(),ppdbOptions);
        if (idxTargetOption2!=-1){
            addOrUpdateHistoryStudent1(ppdbOptions, idxTargetOption2, actOption, actStudent, updateHistoryOtherTarget);
        }


        int idxTargetOption3 = ppdbOptions.size()-1;
        addOrUpdateHistoryStudent1(ppdbOptions, idxTargetOption3, actOption, actStudent, updateHistoryOtherTarget);


    }

    public void addOrUpdateHistoryStudent1(List<PpdbOption> ppdbOptions, int idxTargetOption, int actOption, int actStudent, PpdbHistory historyTarget){


        //historyTarget = ppdbOptions.get(actOption).getPpdbRegistrationHistories().get(actStudent);

        boolean isExist = false;
        System.out.println("addStudentHistory:" + ppdbOptions.get(idxTargetOption).getName() +
                " histSize:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size());
        for (int i = 0; i <ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().size(); i++) {
            System.out.println("addStudentHistory:" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).getName() + " "
                    + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).get_id() + "==" + historyTarget.get_id() );
            if (ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).get_id().equals(historyTarget.get_id()) ){
                System.out.println("addStudentHistory==>" + ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().get(i).isStudentExist());
                //historyTarget.setStudentExist(true);
                ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().set(i, historyTarget);
                isExist = true;
                break;
            }
        }

        if (isExist==false){
            System.out.println("addStudentHistory adding");
            //historyTarget.setStudentExist(true);
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().add(historyTarget);
        }

    }

    /*
    public int findIndexStudentHistoryById(List<PpdbRegistration> ppdbRegistrationHistories, ObjectId studentId){
        for (int iStd = 0; iStd <ppdbRegistrationHistories.size() ; iStd++) {
            if (ppdbRegistrationHistories.get(iStd).get_id().equals(studentId)){
                return iStd;
            }
        }
        return -1;
    }*/

    public int findIndexStudentHistoryById(List<PpdbHistory> ppdbRegistrationHistories, ObjectId studentId){
        for (int iStd = 0; iStd <ppdbRegistrationHistories.size() ; iStd++) {
            if (ppdbRegistrationHistories.get(iStd).get_id().equals(studentId)){
                return iStd;
            }
        }
        return -1;
    }

    public int findIndexStudentHistoryByIdAndByOption(List<PpdbOption> ppdbOptions, int idxOption, ObjectId studentId){
        for (int iStd = 0; iStd <ppdbOptions.get(idxOption).getPpdbRegistrationHistories().size(); iStd++) {
            if (ppdbOptions.get(idxOption).getPpdbRegistrationHistories().get(iStd).get_id().equals(studentId)){
                return iStd;
            }
        }
        return -1;
    }

    public void removeStudentHistory(){

    }

    public void updateMember(ObjectId stdId, List<PpdbOption> employeeFactories){

        /*
        employeeFactories.stream()
                .flatMap(factory -> factory.getPpdbRegistrationHistories().stream())
                .filter(m -> m.get_id().equals(stdId))
                .findFirst()
                .ifPresent(m -> {
                    m.setStudentExist(true);
                });*/
        employeeFactories.stream()
                .flatMap(factory -> factory.getPpdbRegistrationHistories().stream())
                .filter(m -> m.get_id().equals(stdId))
                .findFirst()
                .ifPresentOrElse(
                (value)
                        -> { System.out.println(
                        "Value is present, its: "
                                + value); },
                ()
                        -> { System.out.println(
                        "Value is empty"); });
    }

    public PpdbHistory setStudentHistory(List<PpdbOption> ppdbOptions, int idxTargetOption,
                                     ObjectId studentId, String studentName, double skorPeserta,
                                     double skorJarak1,
                                     ObjectId firstChoice, ObjectId secondChoice,
                                     ObjectId acceptedId, int acceptedNo){
        historyTarget = new PpdbHistory();
        historyTarget.setAcceptedOptionId(acceptedId);
        historyTarget.setAcceptedOptionNo(acceptedNo);
        historyTarget.setFirstChoice(firstChoice);
        historyTarget.setSecondChoice(secondChoice);
        historyTarget.set_id(studentId);
        historyTarget.setName(studentName);
        historyTarget.setSkorPeserta(skorPeserta);
        historyTarget.setSkorJarak1(skorJarak1);
        historyTarget.setStudentExist(true);
        return historyTarget;
    }

    public void addNewStudentHistory(List<PpdbOption> ppdbOptions,List<PpdbRegistration> students, int iStd,
                                     ObjectId acceptedOptionId, int acceptedOptionNo, int idxTargetOption
                                     ){

        int idxStudentHistorySecondOption = findIndexStudentHistoryByIdAndByOption(ppdbOptions, idxTargetOption, students.get(iStd).get_id());

        if (idxStudentHistorySecondOption==-1){

            PpdbHistory ppdbHistory = setStudentHistory(ppdbOptions, idxStudentHistorySecondOption,
                    students.get(iStd).get_id(),
                    students.get(iStd).getName(),
                    students.get(iStd).getSkorPeserta(),
                    students.get(iStd).getSkorJarak1(),
                    students.get(iStd).getFirstChoice(),
                    students.get(iStd).getSecondChoice(),
                    acceptedOptionId,
                    acceptedOptionNo
            );
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().add(ppdbHistory);
        }else{
            PpdbHistory  updateHistoryDariSiswaYgTerlempar2 = this.updateData.updateStudent(
                    ppdbOptions, idxTargetOption,
                    idxStudentHistorySecondOption,
                    acceptedOptionId, acceptedOptionNo);
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().set(idxStudentHistorySecondOption, updateHistoryDariSiswaYgTerlempar2);
        }
    }

    public void pullStudentHistory(List<PpdbOption> ppdbOptions,List<PpdbRegistration> students, int iStd,
                                     ObjectId acceptedOptionId, int acceptedOptionNo, int idxTargetOption
    ){

        int idxStudentHistoryTargetOption = findIndexStudentHistoryByIdAndByOption(ppdbOptions, idxTargetOption, students.get(iStd).get_id());
        if (idxStudentHistoryTargetOption==-1){
            PpdbHistory ppdbHistory = setStudentHistory(ppdbOptions, idxStudentHistoryTargetOption,
                    students.get(iStd).get_id(),
                    students.get(iStd).getName(),
                    students.get(iStd).getSkorPeserta(),
                    students.get(iStd).getSkorJarak1(),
                    students.get(iStd).getFirstChoice(),
                    students.get(iStd).getSecondChoice(),
                    acceptedOptionId,
                    acceptedOptionNo
            );
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().add(ppdbHistory);
        }else{
            PpdbHistory updateHistoryDariSiswaYgTerlempar = this.updateData.updateHistoryStudent(
                    ppdbOptions, idxTargetOption,
                    idxStudentHistoryTargetOption,
                    acceptedOptionId, acceptedOptionNo);
            ppdbOptions.get(idxTargetOption).getPpdbRegistrationHistories().set(idxStudentHistoryTargetOption, updateHistoryDariSiswaYgTerlempar);
        }
    }


}
