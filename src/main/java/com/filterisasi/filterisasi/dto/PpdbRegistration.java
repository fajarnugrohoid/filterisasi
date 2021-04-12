package com.filterisasi.filterisasi.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ppdb_registrations_2020")
public class PpdbRegistration {

    @Id
    private ObjectId _id;

    @Field("jenjang_pendaftaran")
    private String jenjangPendaftaran;

    @Field("cara_pendaftaran")
    private String caraPendaftaran;
    private String levelPendaftaran;
    private ObjectId firstChoice;
    private String scoreJarak1;
    private ObjectId secondChoice;
    private String scoreJarak2;
    private String swastaChoice;
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
    private ObjectId swastaChoiceSchool;
    private Integer statusSeleksi;

    public PpdbRegistration() {
    }

    public PpdbRegistration(ObjectId _id, String jenjangPendaftaran, String caraPendaftaran, String levelPendaftaran, ObjectId firstChoice, String scoreJarak1, ObjectId secondChoice, String scoreJarak2, String swastaChoice, String age, String un, String name, String npsn, boolean approved, String codeSchool, String schoolLevel, String codeType, ObjectId firstChoiceSchool, ObjectId secondChoiceSchool, ObjectId swastaChoiceSchool, Integer statusSeleksi) {
        this._id = _id;
        this.jenjangPendaftaran = jenjangPendaftaran;
        this.caraPendaftaran = caraPendaftaran;
        this.levelPendaftaran = levelPendaftaran;
        this.firstChoice = firstChoice;
        this.scoreJarak1 = scoreJarak1;
        this.secondChoice = secondChoice;
        this.scoreJarak2 = scoreJarak2;
        this.swastaChoice = swastaChoice;
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
        this.swastaChoiceSchool = swastaChoiceSchool;
        this.statusSeleksi = statusSeleksi;
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

    public String getScoreJarak1() {
        return scoreJarak1;
    }

    public void setScoreJarak1(String scoreJarak1) {
        this.scoreJarak1 = scoreJarak1;
    }

    public ObjectId getSecondChoice() {
        return secondChoice;
    }

    public void setSecondChoice(ObjectId secondChoice) {
        this.secondChoice = secondChoice;
    }

    public String getScoreJarak2() {
        return scoreJarak2;
    }

    public void setScoreJarak2(String scoreJarak2) {
        this.scoreJarak2 = scoreJarak2;
    }

    public String getSwastaChoice() {
        return swastaChoice;
    }

    public void setSwastaChoice(String swastaChoice) {
        this.swastaChoice = swastaChoice;
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

    public ObjectId getSwastaChoiceSchool() {
        return swastaChoiceSchool;
    }

    public void setSwastaChoiceSchool(ObjectId swastaChoiceSchool) {
        this.swastaChoiceSchool = swastaChoiceSchool;
    }

    public Integer getStatusSeleksi() {
        return statusSeleksi;
    }

    public void setStatusSeleksi(Integer statusSeleksi) {
        this.statusSeleksi = statusSeleksi;
    }
}
