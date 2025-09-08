package com.hem.cleartouchmedia.persistance;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutDetail;
import com.hem.cleartouchmedia.model.Screen;
import com.hem.cleartouchmedia.model.TwitterApiData;
import com.hem.cleartouchmedia.model.WeatherDetail;

@Database(entities = {Screen.class,
        CompositionDetail.class,
        CompositionLayoutDetail.class,
        WeatherDetail.class,
        TwitterApiData.class},
        version = 25)
public abstract class CompositionDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "Composition_db";
    public abstract ScreenDao screenDao();
    public abstract WeatherDetailDao weatherDetailDao();
    public abstract TwitterApiDataDao twitterApiDataDao();
    public abstract CompositionDetailDao compositionDao();
    public abstract CompositionLayoutDetailDao compositionLayoutDao();
    public static volatile CompositionDatabase INSTANCE = null;

    public static CompositionDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CompositionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, CompositionDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyn(INSTANCE);
        }
    };

    static  class  PopulateDbAsyn extends AsyncTask<Void,Void,Void>{
        private ScreenDao screenDao;
        private CompositionDetailDao compositionDetailDao;
        private WeatherDetailDao weatherDetailDao;
        private TwitterApiDataDao twitterApiDataDao;
        private CompositionLayoutDetailDao compositionLayoutDetailDao;
        public PopulateDbAsyn(CompositionDatabase compositionDatabase)
        {
            screenDao = compositionDatabase.screenDao();
            compositionDetailDao = compositionDatabase.compositionDao();
            twitterApiDataDao = compositionDatabase.twitterApiDataDao();
            weatherDetailDao = compositionDatabase.weatherDetailDao();
            compositionLayoutDetailDao = compositionDatabase.compositionLayoutDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            screenDao.deleteAll();
            compositionDetailDao.deleteAll();
            weatherDetailDao.deleteAll();
            twitterApiDataDao.deleteAll();
            compositionLayoutDetailDao.deleteAll();
            return null;
        }
    }
}