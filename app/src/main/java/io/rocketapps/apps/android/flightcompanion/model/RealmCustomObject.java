package io.rocketapps.apps.android.flightcompanion.model;

import io.realm.RealmObject;

public class RealmCustomObject {

    public static final int SAVED_FLIGHT_LIST = 1;
    public static final int PLACES_LIST = 2;

    int viewType;
    RealmObject object;


    public int getViewType() {
        return viewType;
    }

    public RealmCustomObject setViewType(int viewType) {
        this.viewType = viewType;

        return this;
    }

    public RealmObject getObject() {
        return object;
    }

    public RealmCustomObject setObject(RealmObject object) {
        this.object = object;
        return this;

    }
}
