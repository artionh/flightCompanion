package io.rocketapps.apps.android.flightcompanion.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.rocketapps.apps.android.flightcompanion.R;
import io.rocketapps.apps.android.flightcompanion.model.FlightModel;
import io.rocketapps.apps.android.flightcompanion.model.FlightPlacesModel;
import io.rocketapps.apps.android.flightcompanion.model.PlacesDetailsModel;
import io.rocketapps.apps.android.flightcompanion.model.PlacesImagesModel;
import io.rocketapps.apps.android.flightcompanion.model.RealmController;
import io.rocketapps.apps.android.flightcompanion.model.RequestVolley;
import io.rocketapps.apps.android.flightcompanion.model.SuccessError;
import io.rocketapps.apps.android.flightcompanion.model.Utils;
import io.rocketapps.apps.android.flightcompanion.network.CustomStringRequest;
import io.rocketapps.apps.android.flightcompanion.network.CustomVolleyRequestQueue;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SearchFlightActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "SEARCH";
    private static final Object REQUEST_TAG = "request_search";
    AutoCompleteTextView mFlightFrom, mFlightTo;
    private RequestQueue mQueue;
    private JSONArray mSearchedFlightsFrom;
    private JSONObject mSelectedFrom;
    private JSONArray mSearchedFlightsTo;
    private JSONObject mSelectedTo;
    private SupportMapFragment mapViews;
    private Button mbtnViewOnMap;
    private GoogleMap mMap;
    private Polyline mRouteFlight;
    private MarkerOptions mMarkerFrom, mMarkerTo;
    private Realm realm;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);

        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        RealmController.with(this).refresh();


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


        mFlightFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mSelectedFrom = mSearchedFlightsFrom.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mFlightFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                Utils.Log("SEARCHING", String.valueOf(s.toString()));

                if (s.toString().length() < 3) {
                    return;
                }

                makeRequest(new RequestVolley() {
                    @Override
                    public void onSuccess(String s) throws Exception {

                        Utils.Log(TAG, String.valueOf(s));

                        JSONObject obj = new JSONObject(s);
                        JSONArray mAirports = obj.getJSONArray("airports");
                        List<String> mAirs = new ArrayList<String>();
                        if (obj.getInt("max") > 0) {
                            for (int i = 0; i < mAirports.length(); i++) {
                                mAirs.add(mAirports.getJSONObject(i).getString("name"));
                            }

                            String[] ss = mAirs.toArray(new String[mAirs.size()]);


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getApplicationContext(), R.layout.simple_list_item_1, ss);
                            mFlightFrom.setAdapter(adapter);
                            mFlightFrom.showDropDown();
                            mSearchedFlightsFrom = mAirports;
                            mFlightFrom.notifyAll();
                        }


                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public String getUrl() {
                        return "http://openflights.org/php/apsearch.php";
                    }

                    @Override
                    public Map<String, String> getParams(Context applicationContext) {
                        Map<String, String> m = new HashMap<String, String>();
                        m.put("name", String.valueOf(s.toString()));

                        return m;
                    }
                });
            }
        });

        mFlightTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    mSelectedTo = mSearchedFlightsTo.getJSONObject(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mFlightTo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                Utils.Log("SEARCHING", String.valueOf(s.toString()));

                if (s.toString().length() < 3) {
                    return;
                }

                makeRequest(new RequestVolley() {
                    @Override
                    public void onSuccess(String s) throws Exception {

                        Utils.Log(TAG, String.valueOf(s));

                        JSONObject obj = new JSONObject(s);
                        JSONArray mAirports = obj.getJSONArray("airports");
                        List<String> mAirs = new ArrayList<String>();
                        if (obj.getInt("max") > 0) {
                            for (int i = 0; i < mAirports.length(); i++) {
                                mAirs.add(mAirports.getJSONObject(i).getString("name"));
                            }

                            String[] ss = mAirs.toArray(new String[mAirs.size()]);


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                    (getApplicationContext(), R.layout.simple_list_item_1, ss);
                            mFlightTo.setAdapter(adapter);
                            mFlightTo.showDropDown();
                            mSearchedFlightsTo = mAirports;
                        }


                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public String getUrl() {
                        return "http://openflights.org/php/apsearch.php";
                    }

                    @Override
                    public Map<String, String> getParams(Context applicationContext) {
                        Map<String, String> m = new HashMap<String, String>();
                        m.put("name", String.valueOf(s.toString()));

                        return m;
                    }
                });
            }
        });


        mbtnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (mMap == null)
                        return;


                    mMap.clear();

                    LatLng mFrom = null;
                    LatLng mTo = null;

                    if (mSelectedFrom != null) {
                        mFrom = new LatLng(mSelectedFrom.getDouble("y"), mSelectedFrom.getDouble("x"));

                        mMarkerFrom = new MarkerOptions().position(mFrom)
                                .title(mSelectedFrom.getString("name"));
                        mMap.addMarker(mMarkerFrom);
                        mMap.setMaxZoomPreference(12F);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(mFrom));
                    }
                    if (mSelectedTo != null) {
                        mTo = new LatLng(mSelectedTo.getDouble("y"), mSelectedTo.getDouble("x"));


                        mMarkerTo = new MarkerOptions().position(mTo)
                                .title(mSelectedTo.getString("name"));
                        mMap.addMarker(mMarkerTo);
                        mMap.setMaxZoomPreference(12F);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(mTo));
                    }


                    if (mFrom != null && mTo != null) {

                        if (mRouteFlight != null) {
                            mRouteFlight.remove();
                        }
                        LatLngBounds b = new LatLngBounds.Builder().include(mFrom).include(mTo).build();

                        Utils.Log(TAG, b.getCenter().toString());

                        MarkerOptions mCenter = new MarkerOptions().position(b.getCenter())
                                .anchor(0.5f, 0.5f)
                                .rotation((float) getBearing(mFrom, mTo))
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_plane_v3));
                        mMap.addMarker(mCenter);

//                        mMap.animateCamera(CameraUpdateFactory.newLatLng(b.getCenter()));

                        mRouteFlight = mMap.addPolyline(new PolylineOptions()
                                .add(mFrom, mTo)
                                .width(5)
//                                .geodesic(true)
                                .color(Color.RED));
                        mRouteFlight.setJointType(JointType.ROUND);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    public void makeRequest(final RequestVolley r) {

        Utils.Log("PARAMS", r.getParams(getApplicationContext()).toString());

        CustomStringRequest jsonRequest = new CustomStringRequest(Request.Method.POST,
                r.getUrl(), null,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {
                        Utils.Log(TAG, String.valueOf(result));
                        try {

                            r.onSuccess(result);

                        } catch (Exception e) {
                            r.onError();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.Log(TAG, error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;

            }
        }, r.getParams(getApplicationContext()));
        jsonRequest.setTag(REQUEST_TAG);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonRequest);
    }

    private void initView() {


        mFlightFrom = (AutoCompleteTextView) findViewById(R.id.flight_from);
        mFlightTo = (AutoCompleteTextView) findViewById(R.id.flight_to);
        mbtnViewOnMap = (Button) findViewById(R.id.btn_view_on_map);

        String[] ss = new String[]{"1", "3", "test"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, ss);
        mFlightFrom.setAdapter(adapter);


        mapViews = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapViews.getMapAsync(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_search) {
            saveSearch();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveSearch() {

        try {

            progressDialog = new ProgressDialog(SearchFlightActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Saving Flight");
            progressDialog.setMessage("Please wait while flight is saved locally :)");

            if(!progressDialog.isShowing())
            {
                progressDialog.show();
            }
            String file = Utils.getJsonFile(getApplicationContext(), "places.json");

            JSONObject mObj = new JSONObject(file);
            JSONArray mArray = mObj.getJSONArray("cities");

            PolylineOptions mPolygon = new PolylineOptions();


            if (mSelectedFrom != null && mSelectedTo != null) {

                LatLng mFrom = new LatLng(mSelectedFrom.getDouble("y"), mSelectedFrom.getDouble("x"));
                mPolygon.add(mFrom);

                LatLng mTo = new LatLng(mSelectedTo.getDouble("y"), mSelectedTo.getDouble("x"));


                int dist = (int) getDistanceFromLatLngInKm(mFrom, mTo);
                int from = 0;
                double radiusInKM = 10.0;
                double bearing = getBearing(mFrom, mTo);

                Utils.Log(TAG, String.valueOf(bearing));

                while (from < dist) {
                    LatLng nextPos = getDestinationPoint(mFrom, bearing, radiusInKM + from);
                    mPolygon.add(nextPos);
                    Utils.Log(TAG,"NextPos:"+nextPos.toString());
                    from += 10;
                }



                mPolygon.add(mTo);
//                mMap.clear();
//                mMap.addPolygon(mPolygon);
//                return;


//                LatLng destinationPoint = getDestinationPoint(new LatLng(25.48, -71.26), bearing, radiusInKM);


                double distance = 0;
                Utils.Log(TAG, "Started");

                //region save flight
                realm.beginTransaction();
                FlightModel flightModel = realm.createObject(FlightModel.class);
                flightModel.setAirport_from(mSelectedFrom.getString("name"));
                flightModel.setLatFrom(mSelectedFrom.getDouble("y"));
                flightModel.setLngFrom(mSelectedFrom.getDouble("x"));
                flightModel.setCity_from(mSelectedFrom.getString("city"));
                flightModel.setCountry_from(mSelectedFrom.getString("country"));
                flightModel.setIata_from(mSelectedFrom.getString("iata"));

                flightModel.setAirport_to(mSelectedTo.getString("name"));
                flightModel.setLatTo(mSelectedTo.getDouble("y"));
                flightModel.setLngTo(mSelectedTo.getDouble("x"));
                flightModel.setCity_to(mSelectedTo.getString("city"));
                flightModel.setCountry_to(mSelectedTo.getString("country"));
                flightModel.setIata_to(mSelectedTo.getString("iata"));

                flightModel.setCreated_at(Calendar.getInstance().getTimeInMillis());

                RealmList<FlightPlacesModel> placesModels = new RealmList<>();


                for (int i = 0; i < mArray.length(); i++) {

                    for (int j = 0; j < mPolygon.getPoints().size(); j++) {

                        LatLng l1 = new LatLng(mArray.getJSONObject(i).getDouble("latitude"), mArray.getJSONObject(i).getDouble("longtitude"));


                        distance = getDistanceFromLatLngInKm(mPolygon.getPoints().get(j), l1);

                        if (distance <= 50) {
                            Utils.Log(TAG, String.valueOf(mArray.get(i).toString()));

                            JSONObject place = mArray.getJSONObject(i);
                            FlightPlacesModel flightPlacesModel = realm.createObject(FlightPlacesModel.class);
                            flightPlacesModel.setLat(place.getDouble("latitude"));
                            flightPlacesModel.setLng(place.getDouble("longtitude"));
                            flightPlacesModel.setName(place.getString("cityName"));
                            flightPlacesModel.setCreated_at(Calendar.getInstance().getTimeInMillis());


                            if(place.has("description")) {


                                JSONArray details = place.getJSONArray("description");

                                RealmList<PlacesDetailsModel> placesDetials = new RealmList<>();

                                for (int k = 0; k < details.length(); k++) {
                                    JSONObject obj = details.getJSONObject(k);
                                    if (!obj.has("general"))
                                        continue;
                                    PlacesDetailsModel placesDetailsModel = realm.createObject(PlacesDetailsModel.class);
                                    placesDetailsModel.setGeneral(obj.getString("general"));
                                    placesDetailsModel.setCulture(obj.getString("culture"));
                                    placesDetailsModel.setClimate(obj.getString("climate"));
                                    placesDetailsModel.setGeography(obj.getString("geography"));
                                    placesDetailsModel.setHistory(obj.getString("history"));
                                    placesDetailsModel.setCreated_at(Calendar.getInstance().getTimeInMillis());
                                    placesDetials.add(placesDetailsModel);
                                }

                                flightPlacesModel.setDetails(placesDetials);
                            }

                            if(place.has("photoLinks")) {

                                RealmList<PlacesImagesModel> placesImage = new RealmList<>();

                                JSONArray images = place.getJSONArray("photoLinks");


                                for (int k = 0; k < images.length(); k++) {
                                    JSONObject obj = images.getJSONObject(k);
                                    if (!obj.has("link"))
                                        continue;
                                    final PlacesImagesModel imagesModel = realm.createObject(PlacesImagesModel.class);

                                    imagesModel.setLink(obj.getString("link"));
//                                    saveImage(obj.getString("link"), new SuccessError() {
//                                        @Override
//                                        public void onSuccess(String path) {
//                                            imagesModel.setLink(path);
//                                        }
//
//                                        @Override
//                                        public void onError() {
//
//                                        }
//                                    });

                                    imagesModel.setTitle(obj.getString("title"));
                                    imagesModel.setCreated_at(Calendar.getInstance().getTimeInMillis());
                                    placesImage.add(imagesModel);
                                }
                                flightPlacesModel.setImages(placesImage);
                            }



                            placesModels.add(flightPlacesModel);



                            break;

                        }
                    }

                }

                flightModel.setmPlaces(placesModels);
                realm.commitTransaction();


                Utils.Log(TAG, "END");

                Toast.makeText(getApplicationContext(), "Flight is Saved successfully :).", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }


        } catch (Exception e) {

            realm.close();


            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), "Something went wrong while trying to save flight :(", Toast.LENGTH_SHORT).show();




            e.printStackTrace();

        }
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

        double lat2 =l2.latitude;
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


    private LatLng getDestinationPoint(LatLng source, double brng, double dist) {
        dist = dist / 6371;
        brng = Math.toRadians(brng);

        double lat1 = Math.toRadians(source.latitude), lon1 = Math.toRadians(source.longitude);
        double lat2 = Math.asin(sin(lat1) * cos(dist) +
                cos(lat1) * sin(dist) * cos(brng));
        double lon2 = lon1 + Math.atan2(sin(brng) * sin(dist) *
                        cos(lat1),
                cos(dist) - sin(lat1) *
                        sin(lat2));
        if (Double.isNaN(lat2) || Double.isNaN(lon2)) {
            return null;
        }
        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
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

    @Override
    protected void onStart() {
        super.onStart();


        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Log.d(TAG, "onMapReady()");

    }


    public void saveImage(String url, final SuccessError successError)
    {
        final String fileName = Calendar.getInstance().getTimeInMillis() + ".jpg";
        File file = null;


        File directory = getApplicationContext().getDir("imagesDire", Context.MODE_PRIVATE);


        file = new File(directory,  fileName);
        final File finalFile = file;
        final File finalFile1 = file;
        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {
                return;
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {

                try {


                    finalFile.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(finalFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                    ostream.close();
                    successError.onSuccess(finalFile1.getAbsolutePath());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                return;
            }
        };

        Picasso.with(getApplicationContext())
                .load(url)
                .into(target);

    }
}
