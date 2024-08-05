package com.storelink;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

public class EventManager {

    private static EventManager instance;
    private final MutableLiveData<Map<String, Object>> eventLiveData = new MutableLiveData<>();

    private EventManager() {
    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void postEvent(Map<String, Object> event) {
        eventLiveData.postValue(event);
    }

    public LiveData<Map<String, Object>> getEventLiveData() {
        return eventLiveData;
    }
}
