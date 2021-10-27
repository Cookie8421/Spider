package com.example.spiderman.domain;

import java.util.List;

public class Town {
    public Town(String townCode, String townName, String shortName, String parentCode, List<Village> villageList) {
        this.townCode = townCode;
        this.townName = townName;
        this.shortName = shortName;
        this.parentCode = parentCode;
        VillageList = villageList;
        this.status = 1;
    }

    private String townCode;

    private String townName;

    private String shortName;

    private String parentCode;

    private List<Village> VillageList;

    private final String grade = "town";

    private int hasChild;

    private int status;

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
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

    public List<Village> getVillageList() {
        return VillageList;
    }

    public void setVillageList(List<Village> villageList) {
        VillageList = villageList;
    }

    public int getHasChild() {
        return hasChild;
    }

    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
