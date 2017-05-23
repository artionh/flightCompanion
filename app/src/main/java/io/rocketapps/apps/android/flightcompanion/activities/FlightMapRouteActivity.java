package io.rocketapps.apps.android.flightcompanion.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import io.rocketapps.apps.android.flightcompanion.R;
import io.rocketapps.apps.android.flightcompanion.model.FlightModel;
import io.rocketapps.apps.android.flightcompanion.model.FlightPlacesModel;
import io.rocketapps.apps.android.flightcompanion.model.RealmController;
import io.rocketapps.apps.android.flightcompanion.model.Utils;
import io.rocketapps.apps.android.flightcompanion.network.GPSTracker;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FlightMapRouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private long uniqueId;
    private FlightModel mFlight;
    private Polyline mRouteFlight;
    private MarkerOptions mMarkerFrom;
    private MarkerOptions mMarkerTo;
    private String TAG = "VIEWMAP";
    private Location myLocation;
    private GPSTracker mTracker;
    private MarkerOptions mCenter;
    private double mB = 90;
    private FlightPlacesModel mNearFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_map_route);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            uniqueId = bundle.getLong("object_id");
            mFlight = RealmController.with(this).getSingleObject(uniqueId);


            setTitle(mFlight.getIata_from() + " to " + mFlight.getIata_to());
            toolbar.setTitle(mFlight.getIata_from() + " to " + mFlight.getIata_to());


        }
    }

    private void initData() {

        try {

            if (mMap == null)
                return;


//            mMap.clear();

            LatLng mFrom = null;
            LatLng mTo = null;

            mFrom = new LatLng(mFlight.getLatFrom(), mFlight.getLngFrom());

            mMarkerFrom = new MarkerOptions().position(mFrom)
                    .title(mFlight.getAirport_from());
            mMap.addMarker(mMarkerFrom);
            mMap.setMaxZoomPreference(12F);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mFrom));

            mTo = new LatLng(mFlight.getLatTo(), mFlight.getLngTo());


            mMarkerTo = new MarkerOptions().position(mTo)
                    .title(mFlight.getAirport_to());
            mMap.addMarker(mMarkerTo);
            mMap.setMaxZoomPreference(12F);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(mTo));


            if (mRouteFlight != null) {
                mRouteFlight.remove();
            }
            LatLngBounds b = new LatLngBounds.Builder().include(mFrom).include(mTo).build();

            Utils.Log(TAG, b.getCenter().toString());


            if (myLocation != null) {

                mB = getBearing(mFrom, mTo);
                setMyPosition();


//                MarkerOptions ME = new MarkerOptions().position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
////                        .anchor(0.5f, 0.5f)
//                        .title("My Location")
////                        .rotation((float) getBearing(mFrom, mTo))
//                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_me));
//                mMap.addMarker(ME);
            }


            mRouteFlight = mMap.addPolyline(new PolylineOptions()
                    .add(mFrom, mTo)
                    .width(5)
                    .color(Color.RED));
            mRouteFlight.setJointType(JointType.ROUND);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMyPosition() {
        double lat = 48.125500;//myLocation.getLatitude(), myLocation.getLongitude()
        double lng = 11.576159;
        mCenter = new MarkerOptions().position(new LatLng(lat, lng))
                .anchor(0.5f, 0.5f)
                .rotation((float) mB)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_plane_v3));
        mMap.addMarker(mCenter);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCenter.getPosition(), 6.0f));

        checkHasDataForPlaceDownOfMe();
    }

    private void checkHasDataForPlaceDownOfMe() {
//        LatLng mm = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        LatLng mm = mCenter.getPosition();

        for (int i = 0; i < mFlight.getmPlaces().size(); i++) {
            FlightPlacesModel flightPlacesModel = mFlight.getmPlaces().get(i);

            LatLng d = new LatLng(flightPlacesModel.getLat(), flightPlacesModel.getLng());

            double distance = getDistanceFromLatLngInKm(d, mm);

            if (distance < 50) {
                mNearFlight = flightPlacesModel;
                break;
            }

        }

    }

    public static double getDistanceFromLatLngInKm(LatLng c1, LatLng c2) {
        int R = 6371; // Radius of the earth in km

        double lat1 = c1.latitude;
        double lat2 = c2.latitude;

        double lon1 = c1.longitude;
        double lon2 = c2.longitude;

        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);

        double a =
                sin(dLat / 2) * sin(dLat / 2) +
                        cos(deg2rad(lat1)) * cos(deg2rad(lat2)) *
                                sin(dLon / 2) * sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }


    private static double degreeToRadians(double latLong) {
        return (Math.PI * latLong / 180.0);
    }

    private static double radiansToDegree(double latLong) {
        return (latLong * 180.0 / Math.PI);
    }

    public static double getBearing(LatLng l1, LatLng l2) {

        double lat1 = l1.latitude;
        double lng1 = l1.longitude;

        double lat2 = l2.latitude;
        double lng2 = l2.longitude;

        double fLat = degreeToRadians(lat1);
        double fLong = degreeToRadians(lng1);
        double tLat = degreeToRadians(lat2);
        double tLong = degreeToRadians(lng2);

        double dLon = (tLong - fLong);

        double degree = radiansToDegree(Math.atan2(sin(dLon) * cos(tLat),
                cos(fLat) * sin(tLat) - sin(fLat) * cos(tLat) * cos(dLon)));

        if (degree >= 0) {
            return degree;
        } else {
            return 360 + degree;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        if (mTracker == null) {
            mTracker = new GPSTracker(getApplicationContext());
        }

        if (!isLocationEnabled(getApplicationContext())) {

//            mTracker.showSettingsAlert();
            return;
        }
        Location location;
        String latitude = "0";
        String longtitude = "0";
        if (mTracker != null) {
            myLocation = mTracker.getLocation();


        }

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                myLocation = location;
                setMyPosition();
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // TODO Auto-generated method stub
            }
        });


        if (mFlight != null) {
            initData();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.route_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_near)
        {
            if(mNearFlight != null)
            {
                Intent i = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
                i.putExtra("object_id", mNearFlight.getCreated_at());
                startActivity(i);
            }else{
                Toast.makeText(getApplicationContext(), "We can not find any place down there :(", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

}
