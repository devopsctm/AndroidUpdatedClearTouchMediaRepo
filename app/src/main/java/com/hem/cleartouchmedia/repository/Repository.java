package com.hem.cleartouchmedia.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.model.Screen;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.persistance.CompositionLayoutDetailDao;
import com.hem.cleartouchmedia.persistance.ScreenDao;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public ScreenDao catimgDao;
    public LiveData<Screen> getAllCats;
    private CompositionDatabase database;

    public Repository(Application application) {
        database= CompositionDatabase.getInstance(application);
        catimgDao=database.screenDao();
        getAllCats=catimgDao.getScreen();
    }

    public void insert(Screen cats){
        new InsertAsyncTask(catimgDao).execute(cats);
    }

    public void deleteAll()  {
        new  deleteAllScreneAsyncTask(catimgDao).execute();
    }

    public LiveData<Screen> getAllCats(){
        return getAllCats;
    }
    private static class InsertAsyncTask extends AsyncTask<Screen,Void,Void>{
        private ScreenDao catimgDao;

        public InsertAsyncTask(ScreenDao catDao)
        {
            this.catimgDao=catDao;
        }
        @Override
        protected Void doInBackground(Screen... screens) {
            if (screens != null && screens.length > 0) {
                // Optional: Filter out nulls to avoid crash
                List<Screen> validScreens = new ArrayList<>();
                for (Screen screen : screens) {
                    if (screen != null) validScreens.add(screen);
                }

                if (!validScreens.isEmpty()) {
                    catimgDao.insert(validScreens.toArray(new Screen[0]));
                }
            }
            return null;
        }
    }

    private static class deleteAllScreneAsyncTask extends AsyncTask<Void, Void, Void> {
        private ScreenDao catimgDao;

        deleteAllScreneAsyncTask(ScreenDao dao) {
            catimgDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            catimgDao.deleteAll();
            return null;
        }
    }
}