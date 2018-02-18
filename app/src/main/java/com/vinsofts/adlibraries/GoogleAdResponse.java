package com.vinsofts.adlibraries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macOS on 2/12/18.
 */

public class GoogleAdResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("detail")
    private GoogleAd googleAd;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GoogleAd getGoogleAd() {
        return googleAd;
    }

    public void setGoogleAd(GoogleAd googleAd) {
        this.googleAd = googleAd;
    }
}
