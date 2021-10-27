package com.example.spiderman.domain;

import java.util.List;

public class City {
    public City(String cityCode, String cityName) {
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.shortName = cityName;
        this.parentCode = cityCode.substring(0, 2);
        this.status = 1;
    }

    private String cityCode;

    private String cityName;

    private String shortName;

    private String parentCode;

    private List<County> countyList;

    private int hasChild;

    private final String grade = "city";

    private int status;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public List<County> getCountyList() {
        return countyList;
    }

    public void setCountyList(List<County> countyList) {
        this.countyList = countyList;
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
