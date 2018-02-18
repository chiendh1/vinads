package com.vinsofts.adlibraries;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by macOS on 2/12/18.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("ad")
    Call<GoogleAdResponse> requestAdId(@Field("token") String token,
                                   @Field("app_package") String packageName,
                                   @Field("ad_type") String adType,
                                   @Field("ad_name") String adName);
}
