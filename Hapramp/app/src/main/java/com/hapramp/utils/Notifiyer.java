package com.hapramp.utils;

import java.util.HashMap;

/**
 * Created by Ankit on 1/13/2018.
 */

public class Notifiyer {

    private static HashMap<String,NotifyObjectListener> targetMap;

    public static void init(){
        targetMap = new HashMap<>();
    }

    public void addNotifier(String backTrack,NotifyObjectListener object){
        targetMap.put(backTrack,object);
    }

    public void sendNotif(String path,NotifyObjectListener<?> changedObject){

    }

    interface NotifyObjectListener<T>{
        void onChange(Class<T> a);
    }

}
