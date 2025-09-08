package com.hem.cleartouchmedia.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.Screen;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.persistance.CompositionDetailDao;
import com.hem.cleartouchmedia.persistance.ScreenDao;

public class CompositionRepository {
    public CompositionDetailDao compositionDetailDao;
    public LiveData<CompositionDetail> compositionDetailLiveData;
    private CompositionDatabase database;

    public CompositionRepository(Application application) {
        database= CompositionDatabase.getInstance(application);
        compositionDetailDao = database.compositionDao();
        compositionDetailLiveData = compositionDetailDao.getCompositionDetail();

    }

    public void insert(CompositionDetail compositionDetail) {
        new InsertAsyncTask(compositionDetailDao).execute(compositionDetail);
    }

    public LiveData<CompositionDetail> getCompositionDetail(){
        return compositionDetailLiveData;
    }
    private static class InsertAsyncTask extends AsyncTask<CompositionDetail,Void,Void>{
        private CompositionDetailDao compositionDetailDao;

        public InsertAsyncTask(CompositionDetailDao compositionDetailDao)
        {
            this.compositionDetailDao = compositionDetailDao;
        }
        @Override
        protected Void doInBackground(CompositionDetail... lists) {
            compositionDetailDao.insert(lists);
            return null;
        }
    }
}