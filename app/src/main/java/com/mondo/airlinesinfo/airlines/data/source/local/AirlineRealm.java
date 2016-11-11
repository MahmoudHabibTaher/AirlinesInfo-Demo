package com.mondo.airlinesinfo.airlines.data.source.local;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mahmoud on 11/9/16.
 */

public class AirlineRealm extends RealmObject {
    @PrimaryKey
    private String code;
    private String name;
    private String logo;
    private String website;
    private String phone;
    private boolean isFavorite;

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
