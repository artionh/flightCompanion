package io.rocketapps.apps.android.flightcompanion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.rocketapps.apps.android.flightcompanion.activities.SavedFlightsActivity;
import io.rocketapps.apps.android.flightcompanion.activities.SearchFlightActivity;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    private Button btnView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        setActions();

    }

    private void setActions() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchFlightActivity.class);
                startActivity(i);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SavedFlightsActivity.class);
                startActivity(i);
            }
        });
    }

    private void initView() {

        mToolbar  =  (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        btnSearch = (Button) findViewById(R.id.search_flight);
        btnView = (Button) findViewById(R.id.view_flight);
    }
}
