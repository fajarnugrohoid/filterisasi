package com.filterisasi.filterisasi.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "ppdb_registrations_2020")
public class PpdbRegistration {

    @Id
    private String _id;

    @Field("jenjang_pendaftaran")
    private String jenjangPendaftaran;

    private String caraPendaftaran;
    private String levelPendaftaran;
    private String firstChoice;
    private String scoreJarak1;
    private String secondChoice;
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


}
