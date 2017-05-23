package io.rocketapps.apps.android.flightcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.rocketapps.apps.android.flightcompanion.R;
import io.rocketapps.apps.android.flightcompanion.adapters.FlightRecyclerAdapter;
import io.rocketapps.apps.android.flightcompanion.adapters.OnListFragmentInteractionListener;
import io.rocketapps.apps.android.flightcompanion.model.FlightModel;
import io.rocketapps.apps.android.flightcompanion.model.FlightPlacesModel;
import io.rocketapps.apps.android.flightcompanion.model.PlacesDetailsModel;
import io.rocketapps.apps.android.flightcompanion.model.RealmController;
import io.rocketapps.apps.android.flightcompanion.model.RealmCustomObject;
import io.rocketapps.apps.android.flightcompanion.model.Utils;

public class FlightDetailsActivity extends AppCompatActivity implements OnListFragmentInteractionListener{

    private static final String TAG = "DETAILS";
    private long uniqueId;
    private Realm realm;
    private FlightModel mFlight;
    TextView txtFlightToHeader;
    TextView txtFlightFromHeader;

    TextView txtFlightAirportFrom;
    TextView txtFlightCityFrom;
    TextView txtFlightIataFrom;

    TextView txtFlightAirportTo;
    TextView txtFlightCityTo;
    TextView txtFlightIataTo;

    RecyclerView mRecyclerView;
    private FlightRecyclerAdapter mAdapter;

    OnListFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);


        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        RealmController.with(this).refresh();

        mListener = this;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FlightMapRouteActivity.class);
                i.putExtra("object_id", uniqueId);
                startActivity(i);
            }
        });

        initView();


        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            uniqueId = bundle.getLong("object_id");
            mFlight = RealmController.with(this).getSingleObject(uniqueId);

            initData();
        }

    }

    private void initData() {
        txtFlightFromHeader.setText(mFlight.getIata_from());
        txtFlightToHeader.setText(mFlight.getIata_to());

        txtFlightAirportFrom.setText(mFlight.getAirport_from());
        txtFlightCityFrom.setText(mFlight.getCity_from());
        txtFlightIataFrom.setText(mFlight.getCountry_from());

        txtFlightAirportTo.setText(mFlight.getAirport_to());
        txtFlightCityTo.setText(mFlight.getCity_to());
        txtFlightIataTo.setText(mFlight.getCountry_to());


        ArrayList<RealmCustomObject> mObjs = new ArrayList<>();
        for(FlightPlacesModel fl : mFlight.getmPlaces())
        {
            mObjs.add(new RealmCustomObject()
                    .setViewType(RealmCustomObject.PLACES_LIST)
                    .setObject(fl)
            );

        }

        mAdapter = new FlightRecyclerAdapter(mObjs, getApplicationContext(), mListener);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void initView() {

        txtFlightFromHeader = (TextView) findViewById(R.id.txt_from_iata_header);
        txtFlightToHeader = (TextView) findViewById(R.id.txt_to_iata_header);
        txtFlightAirportFrom = (TextView) findViewById(R.id.txt_airport_from);
        txtFlightCityFrom = (TextView) findViewById(R.id.txt_city_from);
        txtFlightIataFrom = (TextView) findViewById(R.id.txt_iata_from);
        txtFlightAirportTo = (TextView) findViewById(R.id.txt_airport_to);
        txtFlightCityTo = (TextView) findViewById(R.id.txt_city_to);
        txtFlightIataTo = (TextView) findViewById(R.id.txt_iata_to);



        mRecyclerView = (RecyclerView) findViewById(R.id.mRecycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setNestedScrollingEnabled(true);





    }

    @Override
    public void onListFragmentInteraction(RealmCustomObject item) {

        FlightPlacesModel flight = (FlightPlacesModel) item.getObject();
        Utils.Log(TAG, "item:"+flight.getId());
        Intent i = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
        i.putExtra("object_id", flight.getCreated_at());
        startActivity(i);

    }
}
