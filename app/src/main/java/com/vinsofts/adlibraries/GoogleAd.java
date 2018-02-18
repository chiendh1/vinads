package com.vinsofts.adlibraries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macOS on 2/12/18.
 */

public class GoogleAd {
    @SerializedName("app_name")
    private String appName;

    @SerializedName("app_package")
    private String appPackage;

    @SerializedName("ad_type")
    private String adType;

    @SerializedName("ad_name")
    private String adName;

    @SerializedName("ad_code")
    private String adCode;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }
}
