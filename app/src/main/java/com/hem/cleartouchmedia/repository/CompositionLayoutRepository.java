package com.hem.cleartouchmedia.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.model.CompositionLayoutDetail;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.persistance.CompositionLayoutDetailDao;

import java.util.List;

public class CompositionLayoutRepository {
    public CompositionLayoutDetailDao compositionLayoutDetailDao;
    public LiveData<List<CompositionLayoutDetail>> compositionLayoutDetailLiveData;
    private CompositionDatabase database;

    public CompositionLayoutRepository(Application application){
        database= CompositionDatabase.getInstance(application);
        compositionLayoutDetailDao = database.compositionLayoutDao();
        compositionLayoutDetailLiveData = compositionLayoutDetailDao.getCompositionLayoutDetail();
    }

    public void insert(CompositionLayoutDetail compositionLayoutDetail){
        new InsertAsyncTask(compositionLayoutDetailDao).execute(compositionLayoutDetail);
    }

    public void deleteAll()  {
        new deleteAllCompositionAsyncTask(compositionLayoutDetailDao).execute();
    }

    public LiveData<List<CompositionLayoutDetail>> getCompositionLayoutDetail(){
        return compositionLayoutDetailLiveData;
    }

    private static class InsertAsyncTask extends AsyncTask<CompositionLayoutDetail,Void,Void>{
        private CompositionLayoutDetailDao compositionLayoutDetailDao;

        public InsertAsyncTask(CompositionLayoutDetailDao compositionLayoutDetailDao)
        {
            this.compositionLayoutDetailDao = compositionLayoutDetailDao;
        }
        @Override
        protected Void doInBackground(CompositionLayoutDetail... lists) {
            compositionLayoutDetailDao.insert(lists);
            return null;
        }
    }

    private static class deleteAllCompositionAsyncTask extends AsyncTask<Void, Void, Void> {
        private CompositionLayoutDetailDao compositionLayoutDetailDao;

        deleteAllCompositionAsyncTask(CompositionLayoutDetailDao dao) {
            compositionLayoutDetailDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            compositionLayoutDetailDao.deleteAll();
            return null;
        }
    }
}