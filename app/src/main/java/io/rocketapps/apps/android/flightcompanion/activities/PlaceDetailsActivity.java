package io.rocketapps.apps.android.flightcompanion.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import io.realm.RealmList;
import io.rocketapps.apps.android.flightcompanion.R;
import io.rocketapps.apps.android.flightcompanion.model.FlightPlacesModel;
import io.rocketapps.apps.android.flightcompanion.model.PlacesDetailsModel;
import io.rocketapps.apps.android.flightcompanion.model.PlacesImagesModel;
import io.rocketapps.apps.android.flightcompanion.model.RealmController;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PlaceDetailsActivity extends AppCompatActivity  implements OnMapReadyCallback {


    FlightPlacesModel mPlace;
    private long uniqueId;
    private GoogleMap mMap;
    private long parentId;
    private PlacesDetailsModel mDetails;
    private MarkerOptions mMarkerFrom;
    TextView txtGeneral, txtCulture, txtGeography, txtClimate, txtHistory;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;
    private RealmList<PlacesImagesModel> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
//            parentId = bundle.getLong("place_id");
            uniqueId = bundle.getLong("object_id");
            mPlace = RealmController.with(this).getSingleObjectPlace(uniqueId);
            mDetails = mPlace.getDetails().first();
            mImages = mPlace.getImages();


            setTitle(mPlace.getName());
            toolbar.setTitle(mPlace.getName());


        }


    }

    private void initView() {
        txtGeneral = (TextView) findViewById(R.id.txt_general);
        txtCulture = (TextView) findViewById(R.id.txt_culture);
        txtGeography = (TextView) findViewById(R.id.txt_geography);
        txtClimate = (TextView) findViewById(R.id.txt_climate);
        txtHistory = (TextView) findViewById(R.id.txt_history);



        mViewPager = (ViewPager) findViewById(R.id.pager);
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


    private void initData() {

        try {

            if (mMap == null)
                return;


            mMap.clear();


            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            LatLng mFrom = null;

            mFrom = new LatLng(mPlace.getLat(), mPlace.getLng());

            mMarkerFrom = new MarkerOptions().position(mFrom)
                    .title(mPlace.getName());
            mMap.addMarker(mMarkerFrom);
            mMap.setMaxZoomPreference(12F);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( mFrom, 15.0f));



            if(mDetails != null)
            {
                txtGeography.setText(mDetails.getGeography());
                txtCulture.setText(mDetails.getCulture());
                txtClimate.setText(mDetails.getClimate());
                txtHistory.setText(mDetails.getHistory());
                txtGeneral.setText(mDetails.getGeneral());

            }





            mCustomPagerAdapter = new CustomPagerAdapter(this);
            mViewPager.setAdapter(mCustomPagerAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(mPlace != null)
        {
            initData();
        }

    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Picasso.with(mContext).load(mImages.get(position).getLink()).into(imageView);
            TextView textView = (TextView) itemView.findViewById(R.id.photo_title);
            textView.setText(mImages.get(position).getTitle());
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
