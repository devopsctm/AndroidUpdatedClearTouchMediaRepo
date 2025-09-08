package com.hem.cleartouchmedia.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.model.TwitterApiData;
import com.hem.cleartouchmedia.model.WeatherDetail;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.persistance.TwitterApiDataDao;
import com.hem.cleartouchmedia.persistance.WeatherDetailDao;

public class TwitterApiDataRepository {
    public TwitterApiDataDao twitterApiDataDao;
    public LiveData<TwitterApiData> twitterApiDataLiveData;
    private CompositionDatabase database;

    public TwitterApiDataRepository(Application application){
        database= CompositionDatabase.getInstance(application);
        twitterApiDataDao = database.twitterApiDataDao();
        twitterApiDataLiveData = twitterApiDataDao.getTwitterApiData();

    }

    public void insert(TwitterApiData twitterApiData){
        new InsertAsyncTask(twitterApiDataDao).execute(twitterApiData);
    }

    public LiveData<TwitterApiData> getTwitterApiData(){
        return twitterApiDataLiveData;
    }
    private static class InsertAsyncTask extends AsyncTask<TwitterApiData,Void,Void>{
        private TwitterApiDataDao twitterApiDataDao;

        public InsertAsyncTask(TwitterApiDataDao twitterApiDataDao)
        {
            this.twitterApiDataDao = twitterApiDataDao;
        }
        @Override
        protected Void doInBackground(TwitterApiData... lists) {
            twitterApiDataDao.insert(lists);
            return null;
        }
    }

}