package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.Screen;

/**
 * Created by LENOVO on 08/10/2022.
 */

public class TwitterDetailByID
{
    @SerializedName("includes")
    TwitterIncludes includes;

    public TwitterDetailByID(TwitterIncludes includes) {
        this.includes = includes;
    }

    public TwitterIncludes getIncludes() {
        return includes;
    }

    public void setIncludes(TwitterIncludes includes) {
        this.includes = includes;
    }
}
