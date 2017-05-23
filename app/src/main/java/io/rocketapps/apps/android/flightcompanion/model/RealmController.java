package io.rocketapps.apps.android.flightcompanion.model;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmController {


    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm instance
    public void refresh() {

//        realm.refresh();
    }

    public RealmResults<FlightModel> getFlights() {
        return realm.where(FlightModel.class).findAllSorted("created_at", Sort.DESCENDING);
    }

    public FlightModel getSingleObject(long id) {
        return realm.where(FlightModel.class).equalTo("created_at", id).findFirst();
    }
    public FlightPlacesModel getSingleObjectPlace(long id) {
        return realm.where(FlightPlacesModel.class).equalTo("created_at", id).findFirst();
    }
    public PlacesDetailsModel getSingleObjectPlaceDetails(long id) {
        return realm.where(PlacesDetailsModel.class).equalTo("created_at", id).findFirst();
    }
}
