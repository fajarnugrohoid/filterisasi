package com.filterisasi.filterisasi.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ppdb_options")
public class PpdbOption {

    @Id
    private String _id;
    private String name;
    private String type;

    @org.springframework.lang.Nullable
    private int rombel;

    @org.springframework.lang.Nullable
    private int quota;

    @org.springframework.lang.Nullable
    private Integer quota_foreigner;

    @org.springframework.lang.Nullable
    private int total_quota;

    private String school_id;

    @org.springframework.lang.Nullable
    private Ppdb ppdb;

    @org.springframework.lang.Nullable
    private PpdbSchool ppdb_schools;

    public PpdbOption(String _id, String name, String type, int rombel, int quota, Integer quota_foreigner, int total_quota, String school_id, Ppdb ppdb, PpdbSchool ppdb_schools) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.rombel = rombel;
        this.quota = quota;
        this.quota_foreigner = quota_foreigner;
        this.total_quota = total_quota;
        this.school_id = school_id;
        this.ppdb = ppdb;
        this.ppdb_schools = ppdb_schools;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
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

    public int getRombel() {
        return rombel;
    }

    public void setRombel(int rombel) {
        this.rombel = rombel;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getQuota_foreigner() {
        return quota_foreigner;
    }

    public void setQuota_foreigner(int quota_foreigner) {
        this.quota_foreigner = quota_foreigner;
    }

    public int getTotal_quota() {
        return total_quota;
    }

    public void setTotal_quota(int total_quota) {
        this.total_quota = total_quota;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public Ppdb getPpdb() {
        return ppdb;
    }

    public void setPpdb(Ppdb ppdb) {
        this.ppdb = ppdb;
    }

    public PpdbSchool getPpdb_schools() {
        return ppdb_schools;
    }

    public void setPpdb_schools(PpdbSchool ppdb_schools) {
        this.ppdb_schools = ppdb_schools;
    }
}
