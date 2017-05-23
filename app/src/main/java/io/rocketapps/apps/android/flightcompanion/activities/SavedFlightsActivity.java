package io.rocketapps.apps.android.flightcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.rocketapps.apps.android.flightcompanion.R;
import io.rocketapps.apps.android.flightcompanion.adapters.FlightRecyclerAdapter;
import io.rocketapps.apps.android.flightcompanion.adapters.OnListFragmentInteractionListener;
import io.rocketapps.apps.android.flightcompanion.model.FlightModel;
import io.rocketapps.apps.android.flightcompanion.model.RealmController;
import io.rocketapps.apps.android.flightcompanion.model.RealmCustomObject;
import io.rocketapps.apps.android.flightcompanion.model.Utils;

public class SavedFlightsActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private static final String TAG = "SAVED";
    private RecyclerView mRecyclerView;
    private Realm realm;
    private RealmResults<FlightModel> mFlights;
    private FlightRecyclerAdapter mAdapter;


    OnListFragmentInteractionListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_flights);


        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        RealmController.with(this).refresh();

        mListener = this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        setActions();
    }

    private void setActions() {

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mFlights = RealmController.with(this).getFlights();

        ArrayList<RealmCustomObject> mObjs = new ArrayList<>();
        for(FlightModel fl : mFlights)
        {

            mObjs.add(new RealmCustomObject()
                    .setViewType(RealmCustomObject.SAVED_FLIGHT_LIST)
                    .setObject(fl)
            );

        }

        mAdapter = new FlightRecyclerAdapter(mObjs, getApplicationContext(), mListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListFragmentInteraction(RealmCustomObject item) {
        Utils.Log(TAG, "itemclicked");
        FlightModel flight = (FlightModel) item.getObject();
        Utils.Log(TAG, "item:"+flight.getId());
        Intent i = new Intent(getApplicationContext(), FlightDetailsActivity.class);
        i.putExtra("object_id", flight.getCreated_at());
        startActivity(i);
    }





}
