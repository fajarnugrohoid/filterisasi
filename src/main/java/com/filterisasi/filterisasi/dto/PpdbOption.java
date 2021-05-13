package com.filterisasi.filterisasi.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.Nullable;

import java.util.List;

@Document(collection = "ppdb_options")
public class PpdbOption {

    @Id
    private ObjectId _id;
    private String name;

    @Field("type")
    private String type;

    @org.springframework.lang.Nullable
    private Integer rombel = 0;

    @org.springframework.lang.Nullable
    private Integer quota = 0;

    @org.springframework.lang.Nullable
    private Integer quota_foreigner = 0;

    @org.springframework.lang.Nullable
    private Integer total_quota = 0;

    @Field("school_id")
    private ObjectId schoolId;

    @org.springframework.lang.Nullable
    private PpdbSchool ppdb_schools = new PpdbSchool();

    private List<PpdbRegistration> ppdbRegistrationList;

    private boolean needFilter = true;

    private Integer sisaQuota = 0;

    @Field("major_id")
    private Integer majorId;

    private boolean checkQuota = false;

    private Integer quotaBalance = 0;

    public PpdbOption() {
    }

    public PpdbOption(ObjectId _id, String name, String type, Integer rombel, Integer quota,
                      Integer quota_foreigner, Integer total_quota, ObjectId schoolId,
                      List<PpdbRegistration> ppdbRegistrationList,
                      PpdbSchool ppdb_schools,
                      boolean needFilter, Integer sisaQuota, Integer majorId, boolean checkQuota, Integer quotaBalance
                      ) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.rombel = rombel;
        this.quota = quota;
        this.quota_foreigner = quota_foreigner;
        this.total_quota = total_quota;
        this.schoolId = schoolId;
        this.ppdbRegistrationList = ppdbRegistrationList;
        this.ppdb_schools = ppdb_schools;
        this.needFilter = needFilter;
        this.sisaQuota = sisaQuota;
        this.majorId = majorId;
        this.checkQuota = checkQuota;
        this.quotaBalance = quotaBalance;
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

    public ObjectId getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(ObjectId schoolId) {
        this.schoolId = schoolId;
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

    public boolean isNeedFilter() {
        return needFilter;
    }

    public void setNeedFilter(boolean needFilter) {
        this.needFilter = needFilter;
    }

    public Integer getSisaQuota() {
        return sisaQuota;
    }

    public void setSisaQuota(Integer sisaQuota) {
        this.sisaQuota = sisaQuota;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public boolean isCheckQuota() {
        return checkQuota;
    }

    public void setCheckQuota(boolean checkQuota) {
        this.checkQuota = checkQuota;
    }

    public Integer getQuotaBalance() {
        return quotaBalance;
    }

    public void setQuotaBalance(Integer quotaBalance) {
        this.quotaBalance = quotaBalance;
    }
}
