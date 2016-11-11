package com.mondo.airlinesinfo.airlines.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 11/8/16.
 */

public class Airline {
    @SerializedName("code")
    private String code;
    @SerializedName("defaultName")
    private String name;
    @SerializedName("logoURL")
    private String logo;
    @SerializedName("site")
    private String website;
    @SerializedName("phone")
    private String phone;
    private boolean isFavorite;

    public Airline(){

    }

    public Airline(String code, String name, String logo, String website, String phone,
                   boolean isFavorite) {
        this.code = code;
        this.name = name;
        this.logo = logo;
        this.website = website;
        this.phone = phone;
        this.isFavorite = isFavorite;
    }

    public Airline(Airline airline){
        this.code = airline.getCode();
        this.name = airline.getName();
        this.logo = airline.getLogo();
        this.website = airline.getWebsite();
        this.phone = airline.getPhone();
        this.isFavorite = airline.isFavorite();
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
