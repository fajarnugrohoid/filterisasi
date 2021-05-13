package com.filterisasi.filterisasi.module;

import com.filterisasi.filterisasi.dto.PpdbOption;
import com.filterisasi.filterisasi.dto.PpdbRegistration;
import com.filterisasi.filterisasi.dto.PpdbSchool;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class InitializationOutcast {

    public InitializationOutcast() {

    }

    public List<PpdbOption> setOptionOutcast(List<PpdbOption> ppdbOptions){
        String foo = "SekolahBuangan";
        byte[] bytes = foo.getBytes();
        //ObjectId idSekolahBuangan = new ObjectId(Hex.encodeHexString( bytes ) );
        ObjectId idSekolahBuangan = new ObjectId("5eec2ca2b2e4ce405929310f");
        String nameSekolahBuangan = "SekolahBuangan";
        String typeSekolahBuangan = "";
        Integer rombelSekolahBuangan = 0;
        Integer quotaSekolahBuangan = 0;
        Integer quotaForignerSekolahBuangan = 0;
        Integer totalQuotaSekolahBuangan = 0;
        boolean filteredSekolahBuangan = false;

        List<PpdbRegistration> siswaBuangan = new ArrayList<>();
        PpdbOption sekolahBuangan = new PpdbOption();

        sekolahBuangan.setPpdbRegistrationList(siswaBuangan);
        sekolahBuangan.set_id(idSekolahBuangan);
        sekolahBuangan.setName(nameSekolahBuangan);
        PpdbSchool tempSchool = new PpdbSchool();
        tempSchool.setId(new ObjectId("5c7fab1e4b9f621fd05374eb"));
        tempSchool.setType("open");
        tempSchool.setLevel("vocational");
        tempSchool.setCode("400232");
        tempSchool.setName("SekolahBuangan");
        tempSchool.setAddress("Jl.");
        sekolahBuangan.setPpdb_schools(tempSchool);
        sekolahBuangan.setQuota(quotaSekolahBuangan);
        sekolahBuangan.setQuota_foreigner(quotaForignerSekolahBuangan);
        sekolahBuangan.setRombel(rombelSekolahBuangan);
        sekolahBuangan.setSchoolId(new ObjectId("5c7fab1e4b9f621fd05374eb"));
        sekolahBuangan.setTotal_quota(totalQuotaSekolahBuangan);
        sekolahBuangan.setType("type");
        sekolahBuangan.setNeedFilter(filteredSekolahBuangan);
        ppdbOptions.add(sekolahBuangan);
        return ppdbOptions;
    }

}
