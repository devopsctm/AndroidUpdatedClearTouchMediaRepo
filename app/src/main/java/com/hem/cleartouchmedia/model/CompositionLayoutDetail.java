package com.hem.cleartouchmedia.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "compositionLayoutDetail",indices = @Index(value = {"id"},unique = true))
public class CompositionLayoutDetail {

    @PrimaryKey@NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("title")
    @Nullable
    @ColumnInfo(name = "title")
    String title;

    @SerializedName("description")
    @Nullable
    @ColumnInfo(name = "description")
    String description;

    @SerializedName("url")
    @Nullable
    @ColumnInfo(name = "url")
    String url;

    @SerializedName("tag")
    @Nullable
    @ColumnInfo(name = "tag")
    String tag;

    @SerializedName("duration")
    @Nullable
    @ColumnInfo(name = "duration")
    String duration;

    @SerializedName("associated_schedule")
    @Nullable
    @ColumnInfo(name = "associated_schedule")
    String associated_schedule;

    @SerializedName("orientation")
    @Nullable
    @ColumnInfo(name = "orientation")
    String orientation;

    @SerializedName("createdBy")
    @Nullable
    @ColumnInfo(name = "createdBy")
    String createdBy;

    @SerializedName("media_id")
    @Nullable
    @ColumnInfo(name = "media_id")
    String media_id;

    @SerializedName("layout_type")
    @Nullable
    @ColumnInfo(name = "layout_type")
    String layout_type;

    @SerializedName("composition_id")
    @Nullable
    @ColumnInfo(name = "composition_id")
    String composition_id;

    @SerializedName("zone_type")
    @Nullable
    @ColumnInfo(name = "zone_type")
    String zone_type;

    @SerializedName("app_type")
    @Nullable
    @ColumnInfo(name = "app_type")
    String app_type;

    @SerializedName("status")
    @Nullable
    @ColumnInfo(name = "status")
    String status;

    @SerializedName("type")
    @Nullable
    @ColumnInfo(name = "type")
    String type;

    @SerializedName("created_at")
    @Nullable
    @ColumnInfo(name = "created_at")
    String created_at;

    @SerializedName("playtime_seconds")
    @Nullable
    @ColumnInfo(name = "playtime_seconds")
    String playtime_seconds;

    @SerializedName("properties")
    @Nullable
    @ColumnInfo(name = "properties")
    String properties;

    @SerializedName("app_name")
    @Nullable
    @ColumnInfo(name = "app_name")
    String app_name;

    @SerializedName("app_url")
    @Nullable
    @ColumnInfo(name = "app_url")
    String app_url;

    @SerializedName("app_cache")
    @Nullable
    @ColumnInfo(name = "app_cache")
    String app_cache;

    @SerializedName("text")
    @Nullable
    @ColumnInfo(name = "text")
    String text;

    @SerializedName("text_color")
    @Nullable
    @ColumnInfo(name = "text_color")
    String text_color;

    @SerializedName("text_style")
    @Nullable
    @ColumnInfo(name = "text_style")
    String text_style;

    @SerializedName("background_color")
    @Nullable
    @ColumnInfo(name = "background_color")
    String background_color;

    @SerializedName("scroll_speed")
    @Nullable
    @ColumnInfo(name = "scroll_speed")
    String scroll_speed;


    @SerializedName("scroll_font_size")
    @Nullable
    @ColumnInfo(name = "scroll_font_size")
    String scroll_font_size;

    @SerializedName("scroll_direction")
    @Nullable
    @ColumnInfo(name = "scroll_direction")
    String scroll_direction;

    @SerializedName("timezone")
    @Nullable
    @ColumnInfo(name = "timezone")
    String timezone;

    @SerializedName("location")
    @Nullable
    @ColumnInfo(name = "location")
    String location;

    @SerializedName("weather_location")
    @Nullable
    @ColumnInfo(name = "weather_location")
    String weather_location;

    @SerializedName("weather_data")
    @Nullable
    @ColumnInfo(name = "weather_data")
    String weatherData;

    @SerializedName("weather_lat")
    @Nullable
    @ColumnInfo(name = "weather_lat")
    String weather_lat;

    @SerializedName("weather_lng")
    @Nullable
    @ColumnInfo(name = "weather_lng")
    String weather_lng;

    @SerializedName("temp_unit")
    @Nullable
    @ColumnInfo(name = "temp_unit")
    String temp_unit;

    @SerializedName("forcast")
    @Nullable
    @ColumnInfo(name = "forcast")
    String forcast;

    @SerializedName("animation")
    @Nullable
    @ColumnInfo(name = "animation")
    String animation;

    @SerializedName("language")
    @Nullable
    @ColumnInfo(name = "language")
    String language;

    @SerializedName("search_type")
    @Nullable
    @ColumnInfo(name = "search_type")
    String search_type;

    @SerializedName("term")
    @Nullable
    @ColumnInfo(name = "term")
    String term;

    @SerializedName("tweet_count_display")
    @Nullable
    @ColumnInfo(name = "tweet_count_display")
    String tweet_count_display;

    @SerializedName("slide_duration")
    @Nullable
    @ColumnInfo(name = "slide_duration")
    String slide_duration;

    @SerializedName("twitter_feeds")
    @Nullable
    @ColumnInfo(name = "twitter_feeds")
    String twitterProfileData;

    @SerializedName("tweet_feeds")
    @Nullable
    @ColumnInfo(name = "tweet_feeds")
    String twitterFeedsList;

    @SerializedName("theme")
    @Nullable
    @ColumnInfo(name = "theme")
    String theme;

    @SerializedName("moderation")
    @Nullable
    @ColumnInfo(name = "moderation")
    String moderation;

    @SerializedName("authenticated_user")
    @Nullable
    @ColumnInfo(name = "authenticated_user")
    String authenticated_user;

    @SerializedName("twitter_id")
    @Nullable
    @ColumnInfo(name = "twitter_id")
    String twitter_id;

    @SerializedName("youtube_channel_data")
    @Nullable
    @ColumnInfo(name = "youtube_channel_data")
    String youtube_channel_data;

    @SerializedName("youtube_video_ids")
    @Nullable
    @ColumnInfo(name = "youtube_video_ids")
    String youtube_video_ids;

    @SerializedName("channel_id")
    @Nullable
    @ColumnInfo(name = "channel_id")
    String channel_id;

    /*@SerializedName("facebook_data")
    @Nullable
    @ColumnInfo(name = "facebook_data")
    String facebook_data;

    @SerializedName("facebook_id")
    @Nullable
    @ColumnInfo(name = "facebook_id")
    String facebook_id;

    @SerializedName("instagram_data")
    @Nullable
    @ColumnInfo(name = "instagram_data")
    String instagram_data;

    @SerializedName("instagram_id")
    @Nullable
    @ColumnInfo(name = "instagram_id")
    String instagram_id;*/

    @SerializedName("get_all_info")
    @Nullable
    @ColumnInfo(name = "get_all_info")
    String get_all_info;

    @SerializedName("music_url")
    @Nullable
    @ColumnInfo(name = "music_url")
    public String music_url;

    @SerializedName("is_pause")
    @Nullable
    @ColumnInfo(name = "is_pause")
    public String is_pause;

    @SerializedName("is_rtsp_audio")
    @Nullable
    @ColumnInfo(name = "is_rtsp_audio")
    public String is_rtsp_audio;

    @SerializedName("is_video_audio")
    @Nullable
    @ColumnInfo(name = "is_video_audio")
    public String is_video_audio;

    @SerializedName("spreadsheets_url")
    @Nullable
    @ColumnInfo(name = "spreadsheets_url")
    String spreadsheets_url;

    @SerializedName("google_slides_url")
    @Nullable
    @ColumnInfo(name = "google_slides_url")
    String google_slides_url;

    public void setYoutube_channel_data(@Nullable String youtube_channel_data) {
        this.youtube_channel_data = youtube_channel_data;
    }

    public void setYoutube_video_ids(@Nullable String youtube_video_ids) {
        this.youtube_video_ids = youtube_video_ids;
    }

    public void setChannel_id(@Nullable String channel_id) {
        this.channel_id = channel_id;
    }

    /*public void setFacebook_data(@Nullable String facebook_data) {
        this.facebook_data = facebook_data;
    }

    public void setFacebook_id(@Nullable String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public void setInstagram_data(@Nullable String instagram_data) {
        this.instagram_data = instagram_data;
    }

    public void setInstagram_id(@Nullable String instagram_id) {
        this.instagram_id = instagram_id;
    }*/

    @Nullable
    public String getYoutube_channel_data() {
        return youtube_channel_data;
    }

    @Nullable
    public String getYoutube_video_ids() {
        return youtube_video_ids;
    }

    @Nullable
    public String getChannel_id() {
        return channel_id;
    }

    /*@Nullable
    public String getFacebook_data() {
        return facebook_data;
    }

    @Nullable
    public String getFacebook_id() {
        return facebook_id;
    }

    @Nullable
    public String getInstagram_data() {
        return instagram_data;
    }

    @Nullable
    public String getInstagram_id() {
        return instagram_id;
    }*/

    public void setMusicUrl(@Nullable String music_url) {
        this.music_url = music_url;
    }

    public void setIsPause(@Nullable String is_pause) {
        this.is_pause = is_pause;
    }

    public void setIsRtspAudio(@Nullable String is_rtsp_audio) {
        this.is_rtsp_audio = is_rtsp_audio;
    }

    public void setIsVideoAudio(@Nullable String is_video_audio) {
        this.is_video_audio = is_video_audio;
    }

    @Nullable
    public String getBGMusicUrl() {
        return music_url;
    }

    @Nullable
    public String isBGMusicPause() {
        return is_pause;
    }

    public void setTwitterProfileData(@Nullable String twitterProfileData) {
        this.twitterProfileData = twitterProfileData;
    }

    public void setTwitterFeedsList(@Nullable String twitterFeedsList) {
        this.twitterFeedsList = twitterFeedsList;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAssociated_schedule(String associated_schedule) {
        this.associated_schedule = associated_schedule;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public void setLayout_type(String layout_type) {
        this.layout_type = layout_type;
    }

    public void setComposition_id(String composition_id) {
        this.composition_id = composition_id;
    }

    public void setZone_type(String zone_type) {
        this.zone_type = zone_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setPlaytime_seconds(String playtime_seconds) {
        this.playtime_seconds = playtime_seconds;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public void setApp_cache(String app_cache) {
        this.app_cache = app_cache;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText_color(String text_color) {
        this.text_color = text_color;
    }

    public void setText_style(String text_style) {
        this.text_style = text_style;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public void setScroll_speed(String scroll_speed) {
        this.scroll_speed = scroll_speed;
    }

    public void setScroll_font_size(String scroll_font_size) {
        this.scroll_font_size = scroll_font_size;
    }

    public void setScroll_direction(String scroll_direction) {
        this.scroll_direction = scroll_direction;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWeather_location(String weather_location) {
        this.weather_location = weather_location;
    }

    public void setWeatherData(@Nullable String weatherData) {
        this.weatherData = weatherData;
    }

    public void setWeather_lat(String weather_lat) {
        this.weather_lat = weather_lat;
    }

    public void setWeather_lng(String weather_lng) {
        this.weather_lng = weather_lng;
    }

    public void setTemp_unit(String temp_unit) {
        this.temp_unit = temp_unit;
    }

    public void setForcast(String forcast) {
        this.forcast = forcast;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSearch_type(String search_type) {
        this.search_type = search_type;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setTweet_count_display(String tweet_count_display) {
        this.tweet_count_display = tweet_count_display;
    }

    public void setSlide_duration(String slide_duration) {
        this.slide_duration = slide_duration;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setModeration(String moderation) {
        this.moderation = moderation;
    }

    public void setAuthenticated_user(String authenticated_user) {
        this.authenticated_user = authenticated_user;
    }

    public void setTwitter_id(String twitter_id) {
        this.twitter_id = twitter_id;
    }

    public void setGet_all_info(String get_all_info) {
        this.get_all_info = get_all_info;
    }

    public void setMusic_url(@Nullable String music_url) {
        this.music_url = music_url;
    }

    public void setIs_pause(@Nullable String is_pause) {
        this.is_pause = is_pause;
    }

    public void setSpreadsheets_url(@Nullable String spreadsheets_url) {
        this.spreadsheets_url = spreadsheets_url;
    }

    public void setGoogle_slides_url(@Nullable String google_slides_url) {
        this.google_slides_url = google_slides_url;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public String getDuration() {
        return duration;
    }

    public String getAssociated_schedule() {
        return associated_schedule;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getMedia_id() {
        return media_id;
    }

    public String getLayout_type() {
        return layout_type;
    }

    public String getComposition_id() {
        return composition_id;
    }

    public String getZone_type() {
        return zone_type;
    }

    public String getApp_type() {
        return app_type;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getPlaytime_seconds() {
        return playtime_seconds;
    }

    public String getProperties() {
        return properties;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getApp_url() {
        return app_url;
    }

    public String getApp_cache() {
        return app_cache;
    }

    public String getText() {
        return text;
    }

    public String getText_color() {
        return text_color;
    }

    public String getText_style() {
        return text_style;
    }

    public String getBackground_color() {
        return background_color;
    }

    public String getScroll_speed() {
        return scroll_speed;
    }

    public String getScroll_font_size() {
        return scroll_font_size;
    }

    public String getScroll_direction() {
        return scroll_direction;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLocation() {
        return location;
    }

    public String getWeather_location() {
        return weather_location;
    }

    @Nullable
    public String getWeatherData() {
        return weatherData;
    }

    public String getWeather_lat() {
        return weather_lat;
    }

    public String getWeather_lng() {
        return weather_lng;
    }

    public String getTemp_unit() {
        return temp_unit;
    }

    public String getForcast() {
        return forcast;
    }

    public String getAnimation() {
        return animation;
    }

    public String getLanguage() {
        return language;
    }

    public String getSearch_type() {
        return search_type;
    }

    public String getTerm() {
        return term;
    }

    public String getTweet_count_display() {
        return tweet_count_display;
    }

    public String getSlide_duration() {
        return slide_duration;
    }

    public String getTheme() {
        return theme;
    }

    public String getModeration() {
        return moderation;
    }

    public String getAuthenticated_user() {
        return authenticated_user;
    }

    public String getTwitter_id() {
        return twitter_id;
    }

    public String getGet_all_info() {
        return get_all_info;
    }

    @Nullable
    public String getTwitterProfileData() {
        return twitterProfileData;
    }

    @Nullable
    public String getTwitterFeedsList() {
        return twitterFeedsList;
    }

    @Nullable
    public String getMusic_url() {
        return music_url;
    }

    @Nullable
    public String getIs_pause() {
        return is_pause;
    }

    @Nullable
    public String getRTSPAudio() {
        return is_rtsp_audio;
    }

    @Nullable
    public String getVideoAudio() {
        return is_video_audio;
    }

    @Nullable
    public String getSpreadsheets_url() {
        return spreadsheets_url;
    }

    @Nullable
    public String getGoogle_slides_url() {
        return google_slides_url;
    }


    // In CompositionLayoutDetail.java (add at bottom or near existing getters)

    @NonNull
    public String getIdSafe() {
        return id != null ? id : ""; // OR generate UUID
    }

    @NonNull
    public String getZoneTypeSafe() {
        return zone_type != null ? zone_type : "";
    }

    @NonNull
    public String getMediaIdSafe() {
        return media_id != null ? media_id : "";
    }

    @NonNull
    public String getDurationSafe() {
        return duration != null ? duration : "0";
    }

    // Optional: keep these as String but provide boolean-ish helpers
    public boolean isBgMusicPausedSafe() {
        // server returns "1"/"0" or null
        return "1".equals(is_pause);
    }

    @NonNull
    public String getBgMusicUrlSafe() {
        return music_url != null ? music_url : "";
    }

}
