package com.filterisasi.filterisasi.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import java.util.List;

@Document(collection = "ppdb_options")
public class PpdbOption {

    @Id
    private ObjectId _id;
    private String name;
    private String type;

    @org.springframework.lang.Nullable
    private Integer rombel = 0;

    @org.springframework.lang.Nullable
    private Integer quota = 0;

    @org.springframework.lang.Nullable
    private Integer quota_foreigner = 0;

    @org.springframework.lang.Nullable
    private Integer total_quota = 0;

    private String school_id;

    @org.springframework.lang.Nullable
    private PpdbSchool ppdb_schools = new PpdbSchool();

    private List<PpdbRegistration> ppdbRegistrationList;

    private boolean filtered = true;

    public PpdbOption() {
    }

    public PpdbOption(ObjectId _id, String name, String type, Integer rombel, Integer quota,
                      Integer quota_foreigner, Integer total_quota, String school_id,
                      List<PpdbRegistration> ppdbRegistrationList,
                      PpdbSchool ppdb_schools,
                      boolean filtered
                      ) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.rombel = rombel;
        this.quota = quota;
        this.quota_foreigner = quota_foreigner;
        this.total_quota = total_quota;
        this.school_id = school_id;
        this.ppdbRegistrationList = ppdbRegistrationList;
        this.ppdb_schools = ppdb_schools;
        this.filtered = filtered;
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRombel() {
        return rombel;
    }

    public void setRombel(Integer rombel) {
        this.rombel = rombel;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public Integer getQuota_foreigner() {
        return quota_foreigner;
    }

    public Integer getTotal_quota() {
        return total_quota;
    }

    public void setTotal_quota(Integer total_quota) {
        this.total_quota = total_quota;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public void setQuota_foreigner(@Nullable Integer quota_foreigner) {
        this.quota_foreigner = quota_foreigner;
    }

    public List<PpdbRegistration> getPpdbRegistrationList() {
        return ppdbRegistrationList;
    }

    public void setPpdbRegistrationList(List<PpdbRegistration> ppdbRegistrationList) {
        this.ppdbRegistrationList = ppdbRegistrationList;
    }

    public PpdbSchool getPpdb_schools() {
        return ppdb_schools;
    }

    public void setPpdb_schools(PpdbSchool ppdb_schools) {
        this.ppdb_schools = ppdb_schools;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }
}
