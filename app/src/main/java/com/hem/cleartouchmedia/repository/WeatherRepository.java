package com.hem.cleartouchmedia.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.WeatherDetail;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.persistance.CompositionDetailDao;
import com.hem.cleartouchmedia.persistance.WeatherDetailDao;

public class WeatherRepository {
    public WeatherDetailDao weatherDetailDao;
    public LiveData<WeatherDetail> weatherDetailLiveData;
    private CompositionDatabase database;

    public WeatherRepository(Application application){
        database= CompositionDatabase.getInstance(application);
        weatherDetailDao = database.weatherDetailDao();
        weatherDetailLiveData = weatherDetailDao.getWeatherDetail();

    }

    public void insert(WeatherDetail weatherDetail){
        new InsertAsyncTask(weatherDetailDao).execute(weatherDetail);
    }

    public LiveData<WeatherDetail> getWeatherDetail(){
        return weatherDetailLiveData;
    }
    private static class InsertAsyncTask extends AsyncTask<WeatherDetail,Void,Void>{
        private WeatherDetailDao weatherDetailDao;

        public InsertAsyncTask(WeatherDetailDao weatherDetailDao)
        {
            this.weatherDetailDao = weatherDetailDao;
        }
        @Override
        protected Void doInBackground(WeatherDetail... lists) {
            weatherDetailDao.insert(lists);
            return null;
        }
    }
}