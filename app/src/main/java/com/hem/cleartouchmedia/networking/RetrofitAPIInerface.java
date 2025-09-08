package com.hem.cleartouchmedia.networking;

import com.hem.cleartouchmedia.response.CompositionCustomLayoutResponse;
import com.hem.cleartouchmedia.response.CompositionLayoutResponse;
import com.hem.cleartouchmedia.response.CompositionResponse;
import com.hem.cleartouchmedia.response.CompositionStatusCheckResponse;
import com.hem.cleartouchmedia.response.DevicePushStatusReponse;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;
import com.hem.cleartouchmedia.response.ScreenRegResponse;
import com.hem.cleartouchmedia.response.ScreenResponse;
import com.hem.cleartouchmedia.response.TwitterResponse;
import com.hem.cleartouchmedia.response.UpdateScreenRegiResponse;
import com.hem.cleartouchmedia.response.WeatherResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPIInerface {

//    @GET("search")
//    Call<List<Screen>> getImgs(@Query("limit") int limit);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     **/
    @FormUrlEncoded
    @POST("screenDetailsBydeviceId") Call<ScreenResponse> getScreenResponse
        (@Field("device_id") String device_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     **/
    @FormUrlEncoded
    @POST("screenRegiToken") Call<ScreenRegResponse> getScreenRegResponse
    (@Field("device_id") String device_id);


    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     **/
    @FormUrlEncoded
    @POST("updateScreenRegiToken") Call<UpdateScreenRegiResponse> getUpdateScreenRegiToken
        (@Field("versionRelease") String versionRelease,
         @Field("screen_id") String screen_id,
         @Field("device_id") String device_id,
         @Field("device_token") String device_token,
         @Field("device_model") String device_model,
         @Field("device_os") String device_os,
         @Field("device_manufacturer") String device_manufacturer,
         @Field("apk_version") String apk_version,
         @Field("sdk_version") String sdk_version,
         @Field("private_ip") String private_ip,
         @Field("public_ip") String public_ip,
         @Field("mac_address") String mac_address
     );

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("compositionStatusCheck") Call<CompositionStatusCheckResponse> getCompositionStatusCheckResponse
    (@Field("device_id") String device_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("compositionDetailsById") Call<CompositionResponse> getCompositionResponse
        (@Field("device_id") String device_id,
         @Field("composition_id") String compostion_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("compositionDetailsByCompositionId") Call<CompositionLayoutResponse>
    getCompositionLayoutResponse
    (@Field("device_id") String device_id,
     @Field("composition_id") String compostion_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("getTwitterApiData") Call<TwitterResponse> getTwitterApiDataResponse
    (@Field("media_id") String media_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("getTwitterDetailsById") Call<TwitterResponse> getTwitterResponse
    (@Field("tweet_id") String tweet_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("devicePushStatus") Call<DevicePushStatusReponse>
    getDevicePushStatusResponse
    (@Field("device_id") String device_id);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("devicestatuschk") Call<DeviceStatusCheckReponse>
    getDeviceStatusCheckResponse
    (@Field("device_token") String device_token,
     @Field("device_id") String device_id,
     @Field("app_log_method") String app_log_method,
     @Field("composition_id") String composition_id,
     @Field("status") String status);


    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("composition-log") Call<DeviceStatusCheckReponse>
    getCompositionLogResponse
    (@Field("device_id") String device_id,
     @Field("app_log_method") String app_log_method,
     @Field("composition_id") String composition_id,
     @Field("status") String status);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @GET("?q=toronto&appid=764b9d8a331ca3ed10072996af51153a") Call<CompositionLayoutResponse>
    getCompositionWeatherResponse ();


    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     *
     * @param params*/
    @GET("forecast")
    Call<WeatherResponse> getWeatherApiDataResponse(Map<String, String> params);

    /**
     * FORM ENCODED AND MULTIPART
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value
     * */
    @FormUrlEncoded
    @POST("dynamic_layout") Call<CompositionCustomLayoutResponse> getCustomCompositionResponse (
     @Field("composition_id") String compostion_id);
}
