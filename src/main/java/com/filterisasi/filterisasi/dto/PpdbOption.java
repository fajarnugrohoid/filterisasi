package com.filterisasi.filterisasi.dto;

import com.filterisasi.filterisasi.model.PpdbHistory;
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
    private int rombel = 0;

    @org.springframework.lang.Nullable
    private int quota = 0;

    @Field("quota")
    private int oldQuota = 0;

    @org.springframework.lang.Nullable
    private int quota_foreigner = 0;

    @org.springframework.lang.Nullable
    private int total_quota = 0;

    @Field("school_id")
    private ObjectId schoolId;

    @org.springframework.lang.Nullable
    private PpdbSchool ppdb_schools = new PpdbSchool();

    private List<PpdbRegistration> ppdbRegistrationList;

    private boolean needFilter = true;

    private int sisaQuota = 0;

    @Field("major_id")
    private int majorId;

    private boolean checkQuota = false;

    private int quotaBalance = 0;

    private List<PpdbHistory> ppdbRegistrationHistories;

    public PpdbOption() {
    }

    public PpdbOption(ObjectId _id, String name, String type, int rombel, int quota, int oldQuota,
                      int quota_foreigner, int total_quota, ObjectId schoolId,
                      List<PpdbRegistration> ppdbRegistrationList,
                      PpdbSchool ppdb_schools,
                      boolean needFilter, int sisaQuota, int majorId, boolean checkQuota, int quotaBalance,
                      List<PpdbHistory> ppdbRegistrationHistories
                      ) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.rombel = rombel;
        this.quota = quota;
        this.oldQuota = oldQuota;
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
        this.ppdbRegistrationHistories = ppdbRegistrationHistories;
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

    public int getOldQuota() {
        return oldQuota;
    }

    public void setOldQuota(int oldQuota) {
        this.oldQuota = oldQuota;
    }

    public int getQuota_foreigner() {
        return quota_foreigner;
    }

    public int getTotal_quota() {
        return total_quota;
    }

    public void setTotal_quota(int total_quota) {
        this.total_quota = total_quota;
    }

    public ObjectId getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(ObjectId schoolId) {
        this.schoolId = schoolId;
    }

    public void setQuota_foreigner(@Nullable int quota_foreigner) {
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

    public int getSisaQuota() {
        return sisaQuota;
    }

    public void setSisaQuota(int sisaQuota) {
        this.sisaQuota = sisaQuota;
    }

    public int getMajorId() {
        return majorId;
    }

    public void setMajorId(int majorId) {
        this.majorId = majorId;
    }

    public boolean isCheckQuota() {
        return checkQuota;
    }

    public void setCheckQuota(boolean checkQuota) {
        this.checkQuota = checkQuota;
    }

    public int getQuotaBalance() {
        return quotaBalance;
    }

    public void setQuotaBalance(int quotaBalance) {
        this.quotaBalance = quotaBalance;
    }

    public List<PpdbHistory> getPpdbRegistrationHistories() {
        return ppdbRegistrationHistories;
    }

    public void setPpdbRegistrationHistories(List<PpdbHistory> ppdbRegistrationHistories) {
        this.ppdbRegistrationHistories = ppdbRegistrationHistories;
    }
}
