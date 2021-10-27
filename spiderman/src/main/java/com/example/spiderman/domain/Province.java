package com.example.spiderman.domain;

import java.util.List;

public class Province {
    public Province(String provinceCode, String provinceName, String parentCode) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        if("黑龙江省".equals(provinceName)){
            this.shortName = "黑龙江";
        } else {
            this.shortName = provinceName.substring(0, 2);
        }
        this.parentCode = parentCode;
        this.status = 1;
    }

    private String provinceCode;

    private String provinceName;

    private String shortName;

    private String parentCode;

    private List<City> cityList;

    private int hasChild = 0;

    private final String grade = "province";

    private int status;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
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
