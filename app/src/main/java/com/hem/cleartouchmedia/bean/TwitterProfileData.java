package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.Screen;

import java.util.List;

/**
 * Created by LENOVO on 08/10/2022.
 */

public class TwitterProfileData
{
    @SerializedName("data")
    List<TwitterProfileDataBean> twitterProfileDataBean;

    public TwitterProfileData(List<TwitterProfileDataBean> twitterProfileDataBean) {
        this.twitterProfileDataBean = twitterProfileDataBean;
    }

    public List<TwitterProfileDataBean> getTwitterProfileDataBean() {
        return twitterProfileDataBean;
    }

    public void setTwitterProfileDataBean(List<TwitterProfileDataBean> twitterProfileDataBean) {
        this.twitterProfileDataBean = twitterProfileDataBean;
    }
}
