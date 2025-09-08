package com.hem.cleartouchmedia.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.repository.TwitterApiDataRepository;
import com.hem.cleartouchmedia.repository.WeatherRepository;

public class TwitterApiDataViewModel extends AndroidViewModel {
    private TwitterApiDataRepository repository;
    public LiveData<TwitterApiData> getTwitterApiData;

    public TwitterApiDataViewModel(@NonNull Application application) {
        super(application);
        repository = new TwitterApiDataRepository(application);
        getTwitterApiData = repository.getTwitterApiData();
    }

    public void insert(TwitterApiData detail){
        repository.insert(detail);
    }

    public LiveData<TwitterApiData> getTwitterApiData()
    {
        return getTwitterApiData;
    }
}
