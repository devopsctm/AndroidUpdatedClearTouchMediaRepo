package com.hem.cleartouchmedia.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.repository.CompositionRepository;

public class CompositionDetailViewModel extends AndroidViewModel {
    private CompositionRepository repository;
    public LiveData<CompositionDetail> getComposition;

    public CompositionDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new CompositionRepository(application);
        getComposition = repository.getCompositionDetail();
    }

    public void insert(CompositionDetail detail){
        repository.insert(detail);
    }

    public LiveData<CompositionDetail> getCompositionDetail()
    {
        return getComposition;
    }

}
