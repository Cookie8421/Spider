package com.example.spiderman.domain;

import java.util.List;

public class County {
    public County(String countyCode, String countyName, String shortName, String parentCode, List<Town> townList) {
        this.countyCode = countyCode;
        this.countyName = countyName;
        this.shortName = shortName;
        this.parentCode = parentCode;
        this.townList = townList;
        this.status = 1;
    }

    private String countyCode;

    private String countyName;

    private String shortName;

    private String parentCode;

    private List<Town> townList;

    private final String grade = "county";

    private int hasChild;

    private int status;

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
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

    public List<Town> getTownList() {
        return townList;
    }

    public void setTownList(List<Town> townList) {
        this.townList = townList;
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
