package io.rocketapps.apps.android.flightcompanion.model;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
 
public class RealmHelper {

    private static RealmHelper instance;
    private final Realm realm;

    public RealmHelper(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmHelper with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmHelper(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmHelper with(Activity activity) {

        if (instance == null) {
            instance = new RealmHelper(activity.getApplication());
        }
        return instance;
    }

    public static RealmHelper with(Application application) {

        if (instance == null) {
            instance = new RealmHelper(application);
        }
        return instance;
    }

    public static RealmHelper getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }




}
