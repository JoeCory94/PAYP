package com.joe.payp;

import java.io.Serializable;

public class ListObject implements Serializable{

    private String cityName;
    private Integer cityPicture;

    public String getCityName() {
        return cityName;
    }

    public Integer getCityPicture() { return cityPicture; }

    public ListObject(String cityName, Integer cityPicture) {
        this.cityName = cityName;
        this.cityPicture = cityPicture;
    }
}

