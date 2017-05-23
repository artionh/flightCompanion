package io.rocketapps.apps.android.flightcompanion;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.rocketapps.apps.android.flightcompanion.model.RealmConfigs;

public class FlightApplicaiton extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(RealmConfigs.DB_NAME)
                .schemaVersion(RealmConfigs.SCHEMA)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
