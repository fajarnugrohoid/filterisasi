package com.filterisasi.filterisasi.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ppdb_schools")
public class Ppdb {

    @Id
    private String _id;
    private String type;
    private String level;
    private String code;
    private String name;
    private String address;
    private PpdbSchool schools;

    public Ppdb() {
    }

    public Ppdb(PpdbSchool schools) {
        this.schools = schools;
    }

    public Ppdb(String _id, String type, String level, String code, String name, String address) {
        this._id = _id;
        this.type = type;
        this.level = level;
        this.code = code;
        this.name = name;
        this.address = address;
    }

    public String get_Id() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PpdbSchool getSchools() {
        return schools;
    }

    public void setSchools(PpdbSchool schools) {
        this.schools = schools;
    }
}
