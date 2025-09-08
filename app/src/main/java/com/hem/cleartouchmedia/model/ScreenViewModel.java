package com.hem.cleartouchmedia.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.repository.Repository;

import java.util.List;

public class ScreenViewModel extends AndroidViewModel {
    private Repository repository;
    public LiveData<Screen> getScreen;

    public ScreenViewModel(@NonNull Application application) {
        super(application);
        repository=new Repository(application);
        getScreen=repository.getAllCats();
    }

    public void insert(Screen cats){
        repository.insert(cats);
    }

    public LiveData<Screen> getAllCats()
    {
        return getScreen;
    }

    public void deleteAll() {repository.deleteAll();}
}
