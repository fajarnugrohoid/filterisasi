package com.filterisasi.filterisasi.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "ppdb_registrations_2020")
public class PpdbHistory {

    @Id
    private ObjectId _id;

    @Field("jenjang_pendaftaran")
    private String jenjangPendaftaran;

    @Field("cara_pendaftaran")
    private String caraPendaftaran;

    @Field("level_pendaftaran")
    private String levelPendaftaran;

    @Field("first_choice")
    private ObjectId firstChoice;

    @Field("second_choice")
    private ObjectId secondChoice;

    @Field("third_choice")
    private ObjectId thirdChoice;

    @Field("priority")
    private int priority;

    private String age;
    private String un;
    private String name;
    private String npsn;
    private boolean approved;
    private String codeSchool;
    private String schoolLevel;
    private String codeType;
    private ObjectId firstChoiceSchool;
    private ObjectId secondChoiceSchool;
    private ObjectId thirdChoiceSchool;
    private int statusSeleksi;

    @Field("score_jarak_1")
    private double scoreJarak1 = 0.0;

    @Field("score_jarak_2")
    private double scoreJarak2 = 0.0;

    @Field("score_jarak_3")
    private double scoreJarak3 = 0.0;

    private double scoreDistanceFinal = 0.0;

    @Field("skor_peserta")
    private double skorPeserta = 0.0;

    //@Field("birth_date")
    //private int birthDate;
    private double scoreAge = 0.0;

    private int acceptedOptionNo = 0; //status saat ini diterima disekolah mana

    private List<ObjectId> optionHistories = new ArrayList<>();

    private ObjectId acceptedOptionId;

    private boolean studentExist;

    public PpdbHistory() {
    }

    public PpdbHistory(ObjectId _id, String jenjangPendaftaran, String caraPendaftaran,
                       String levelPendaftaran, ObjectId firstChoice,
                       ObjectId secondChoice, ObjectId thirdChoice,
                       String age, String un, String name, String npsn, boolean approved,
                       String codeSchool, String schoolLevel, String codeType,
                       ObjectId firstChoiceSchool, ObjectId secondChoiceSchool,
                       ObjectId swastaChoiceSchool, int statusSeleksi,
                       double scoreJarak1, double scoreJarak2, double scoreJarak3, double scoreDistanceFinal,
                       double scoreAge,
                       double skorPeserta,
                       int acceptedOptionNo,
                       List<ObjectId> optionHistories,
                       ObjectId acceptedOptionId,
                       boolean studentExist
    ) {
        this._id = _id;
        this.jenjangPendaftaran = jenjangPendaftaran;
        this.caraPendaftaran = caraPendaftaran;
        this.levelPendaftaran = levelPendaftaran;
        this.firstChoice = firstChoice;
        this.secondChoice = secondChoice;
        this.thirdChoice = thirdChoice;
        this.scoreJarak1 = scoreJarak1;
        this.scoreJarak2 = scoreJarak2;
        this.scoreJarak3 = scoreJarak3;
        this.scoreAge = scoreAge;
        this.scoreDistanceFinal = scoreDistanceFinal;
        this.age = age;
        this.un = un;
        this.name = name;
        this.npsn = npsn;
        this.approved = approved;
        this.codeSchool = codeSchool;
        this.schoolLevel = schoolLevel;
        this.codeType = codeType;
        this.firstChoiceSchool = firstChoiceSchool;
        this.secondChoiceSchool = secondChoiceSchool;
        this.statusSeleksi = statusSeleksi;
        this.skorPeserta = skorPeserta;
        this.acceptedOptionNo = acceptedOptionNo;
        this.optionHistories = optionHistories;
        this.acceptedOptionId = acceptedOptionId;
        this.studentExist = studentExist;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getJenjangPendaftaran() {
        return jenjangPendaftaran;
    }

    public void setJenjangPendaftaran(String jenjangPendaftaran) {
        this.jenjangPendaftaran = jenjangPendaftaran;
    }

    public String getCaraPendaftaran() {
        return caraPendaftaran;
    }

    public void setCaraPendaftaran(String caraPendaftaran) {
        this.caraPendaftaran = caraPendaftaran;
    }

    public String getLevelPendaftaran() {
        return levelPendaftaran;
    }

    public void setLevelPendaftaran(String levelPendaftaran) {
        this.levelPendaftaran = levelPendaftaran;
    }

    public ObjectId getFirstChoice() {
        return firstChoice;
    }

    public void setFirstChoice(ObjectId firstChoice) {
        this.firstChoice = firstChoice;
    }

    public ObjectId getSecondChoice() {
        return secondChoice;
    }

    public void setSecondChoice(ObjectId secondChoice) {
        this.secondChoice = secondChoice;
    }

    public ObjectId getThirdChoice() {
        return thirdChoice;
    }

    public void setThirdChoice(ObjectId thirdChoice) {
        this.thirdChoice = thirdChoice;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNpsn() {
        return npsn;
    }

    public void setNpsn(String npsn) {
        this.npsn = npsn;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getCodeSchool() {
        return codeSchool;
    }

    public void setCodeSchool(String codeSchool) {
        this.codeSchool = codeSchool;
    }

    public String getSchoolLevel() {
        return schoolLevel;
    }

    public void setSchoolLevel(String schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public ObjectId getFirstChoiceSchool() {
        return firstChoiceSchool;
    }

    public void setFirstChoiceSchool(ObjectId firstChoiceSchool) {
        this.firstChoiceSchool = firstChoiceSchool;
    }

    public ObjectId getSecondChoiceSchool() {
        return secondChoiceSchool;
    }

    public void setSecondChoiceSchool(ObjectId secondChoiceSchool) {
        this.secondChoiceSchool = secondChoiceSchool;
    }

    public ObjectId getThirdChoiceSchool() {
        return thirdChoiceSchool;
    }

    public void setThirdChoiceSchool(ObjectId thirdChoiceSchool) {
        this.thirdChoiceSchool = thirdChoiceSchool;
    }

    public int getStatusSeleksi() {
        return statusSeleksi;
    }

    public void setStatusSeleksi(int statusSeleksi) {
        this.statusSeleksi = statusSeleksi;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getScoreJarak1() {
        return scoreJarak1;
    }

    public void setScoreJarak1(double scoreJarak1) {
        this.scoreJarak1 = scoreJarak1;
    }

    public double getScoreJarak2() {
        return scoreJarak2;
    }

    public void setScoreJarak2(double scoreJarak2) {
        this.scoreJarak2 = scoreJarak2;
    }

    public double getScoreJarak3() {
        return scoreJarak3;
    }

    public void setScoreJarak3(double scoreJarak3) {
        this.scoreJarak3 = scoreJarak3;
    }

    public double getSkorPeserta() {
        return skorPeserta;
    }

    public void setSkorPeserta(double skorPeserta) {
        this.skorPeserta = skorPeserta;
    }

    public double getScoreDistanceFinal() {
        return scoreDistanceFinal;
    }

    public void setScoreDistanceFinal(double scoreDistanceFinal) {
        this.scoreDistanceFinal = scoreDistanceFinal;
    }

    public double getScoreAge() {
        return scoreAge;
    }

    public void setScoreAge(double scoreAge) {
        this.scoreAge = scoreAge;
    }

    public List<ObjectId> getOptionHistories() {
        return optionHistories;
    }

    public void setOptionHistories(List<ObjectId> optionHistories) {
        this.optionHistories = optionHistories;
    }

    public int getAcceptedOptionNo() {
        return acceptedOptionNo;
    }

    public void setAcceptedOptionNo(int acceptedOptionNo) {
        this.acceptedOptionNo = acceptedOptionNo;
    }

    public ObjectId getAcceptedOptionId() {
        return acceptedOptionId;
    }

    public void setAcceptedOptionId(ObjectId acceptedOptionId) {
        this.acceptedOptionId = acceptedOptionId;
    }

    public boolean isStudentExist() {
        return studentExist;
    }

    public void setStudentExist(boolean studentExist) {
        this.studentExist = studentExist;
    }
}
