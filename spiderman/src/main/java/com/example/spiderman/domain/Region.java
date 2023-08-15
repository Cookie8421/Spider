package com.example.spiderman.domain;

public class Region {

    public Region(String regionCode, String regionName, String grade, int hasChild) {
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.shortName = regionName;
        if("province".equals(grade)){
            this.parentCode = "0";
        } else if("city".equals(grade)) {
            this.parentCode = regionCode.substring(0, 2) + "0000000000";
        } else if("county".equals(grade)) {
            this.parentCode = regionCode.substring(0, 4) + "00000000";
        } else if("town".equals(grade)) {
            this.parentCode = regionCode.substring(0, 6) + "000000";
        } else if("village".equals(grade)) {
            this.parentCode = regionCode.substring(0, 9) + "000";
            this.provinceCode = regionCode.substring(0, 2) + "0000000000";
            this.cityCode = regionCode.substring(0, 4) + "00000000";
            this.countyCode = regionCode.substring(0, 6) + "000000";
            this.streetCode = regionCode.substring(0, 9) + "000";
            this.regionCodes = regionCode.substring(0, 2) + "0000000000"
                    + "/" + regionCode.substring(0, 4) + "00000000"
                    + "/" + regionCode.substring(0, 6) + "000000"
                    + "/" + regionCode.substring(0, 9) + "000";
        }
        this.grade = grade;
        this.hasChild = hasChild;
    }

    private String regionCode;

    private String regionName;

    private String shortName;

    private String parentCode;

    private String provinceCode;

    private String cityCode;

    private String countyCode;

    private String streetCode;

    private String regionNames;

    private String regionCodes;

    private String grade;

    private int hasChild;

    private String status = "1";

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getRegionNames() {
        return regionNames;
    }

    public void setRegionNames(String regionNames) {
        this.regionNames = regionNames;
    }

    public String getRegionCodes() {
        return regionCodes;
    }

    public void setRegionCodes(String regionCodes) {
        this.regionCodes = regionCodes;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getHasChild() {
        return hasChild;
    }

    public void setHasChild(int hasChild) {
        this.hasChild = hasChild;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
