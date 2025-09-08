package com.hem.cleartouchmedia.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.repository.CompositionRepository;
import com.hem.cleartouchmedia.repository.WeatherRepository;

public class WeatherDetailViewModel extends AndroidViewModel {
    private WeatherRepository repository;
    public LiveData<WeatherDetail> getWeather;

    public WeatherDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new WeatherRepository(application);
        getWeather = repository.getWeatherDetail();
    }

    public void insert(WeatherDetail detail){
        repository.insert(detail);
    }

    public LiveData<WeatherDetail> getWeatherDetail()
    {
        return getWeather;
    }
}
