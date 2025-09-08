package com.hem.cleartouchmedia.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.persistance.CompositionLayoutDetailDao;
import com.hem.cleartouchmedia.repository.CompositionLayoutRepository;
import com.hem.cleartouchmedia.repository.CompositionRepository;

import java.util.List;

public class CompositionLayoutDetailViewModel extends AndroidViewModel {
    private CompositionLayoutRepository repository;
    public LiveData<List<CompositionLayoutDetail>> getCompositionDetail;
//    public List<CompositionLayoutDetail> getCompositionList;
//    public LiveData<CompositionLayoutDetail> getCompositionDetailByMediaId;
    public CompositionLayoutDetailDao compositionLayoutDetailDao;

    public CompositionLayoutDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new CompositionLayoutRepository(application);
        getCompositionDetail = repository.getCompositionLayoutDetail();
//        getCompositionList = repository.getCompositionLayoutList();
//        getCompositionDetailByMediaId = repository.getCompositionLayoutDetailByMediaId(media_id);
        compositionLayoutDetailDao = CompositionDatabase.getInstance(application).compositionLayoutDao();
    }

    public void insert(CompositionLayoutDetail detail){
        repository.insert(detail);
    }

    public LiveData<List<CompositionLayoutDetail>> getCompositionLayoutDetail()
    {
        return getCompositionDetail;
    }

    /*public List<CompositionLayoutDetail> getCompositionLayoutList()
    {
        return getCompositionList;
    }*/

    /*public LiveData<CompositionLayoutDetail> getCompositionLayoutDetailByMediaId(String media_id)
    {
        return getCompositionDetailByMediaId;
    }*/

    public CompositionLayoutDetail searchSingleRecordByID( String media_id ) {
        return compositionLayoutDetailDao.getCompositionLayoutDetailByMediaId(media_id);
    }

    public void deleteAll() {repository.deleteAll();}
}
