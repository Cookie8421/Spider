package com.example.spiderman.domain;

public class Village {

    public Village(String villageCode, String villageName, String shortName, String parentCode) {
        this.villageCode = villageCode;
        this.villageName = villageName;
        this.shortName = shortName;
        this.parentCode = parentCode;
        this.status = 1;
    }

    private String villageCode;

    private String villageName;

    private String shortName;

    private String parentCode;

    private final String grade = "village";

    private int status;

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
