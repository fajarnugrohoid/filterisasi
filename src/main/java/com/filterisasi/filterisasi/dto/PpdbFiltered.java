package com.filterisasi.filterisasi.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ppdb_filtereds_test")
public class PpdbFiltered {

    @Id
    private ObjectId _id;

    @Field("registration_id")
    private ObjectId registrationId;

    @Field("option_id")
    private ObjectId optionId;

    @Field("school_id")
    private ObjectId schoolId;

    @Field("level")
    private String level; //senior or vocational

    @Field("type")
    private String type; //jalur

    private ObjectId penetepanOptionId;

    public PpdbFiltered() {
    }

    public PpdbFiltered(ObjectId _id, ObjectId registrationId, ObjectId optionId, ObjectId schoolId, String level, String type, ObjectId penetepanOptionId) {
        this._id = _id;
        this.registrationId = registrationId;
        this.optionId = optionId;
        this.schoolId = schoolId;
        this.level = level;
        this.type = type;
        this.penetepanOptionId = penetepanOptionId;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(ObjectId registrationId) {
        this.registrationId = registrationId;
    }

    public ObjectId getOptionId() {
        return optionId;
    }

    public void setOptionId(ObjectId optionId) {
        this.optionId = optionId;
    }

    public ObjectId getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(ObjectId schoolId) {
        this.schoolId = schoolId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ObjectId getPenetepanOptionId() {
        return penetepanOptionId;
    }

    public void setPenetepanOptionId(ObjectId penetepanOptionId) {
        this.penetepanOptionId = penetepanOptionId;
    }
}
