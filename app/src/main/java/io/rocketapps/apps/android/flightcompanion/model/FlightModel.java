package io.rocketapps.apps.android.flightcompanion.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class FlightModel extends RealmObject {

    @Index
    private long id;
    double latFrom;
    double lngFrom;
    double latTo;
    double lngTo;
    String airport_from;
    String airport_to;
    long created_at;
    String city_from;
    String country_from;
    String iata_from;
    String city_to;
    String country_to;
    String iata_to;

    RealmList<FlightPlacesModel> mPlaces;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatFrom() {
        return latFrom;
    }

    public void setLatFrom(double latFrom) {
        this.latFrom = latFrom;
    }

    public double getLngFrom() {
        return lngFrom;
    }

    public void setLngFrom(double lngFrom) {
        this.lngFrom = lngFrom;
    }

    public double getLatTo() {
        return latTo;
    }

    public void setLatTo(double latTo) {
        this.latTo = latTo;
    }

    public double getLngTo() {
        return lngTo;
    }

    public void setLngTo(double lngTo) {
        this.lngTo = lngTo;
    }

    public String getAirport_from() {
        return airport_from;
    }

    public void setAirport_from(String airport_from) {
        this.airport_from = airport_from;
    }

    public String getAirport_to() {
        return airport_to;
    }

    public void setAirport_to(String airport_to) {
        this.airport_to = airport_to;
    }

    public RealmList<FlightPlacesModel> getmPlaces() {
        return mPlaces;
    }

    public void setmPlaces(RealmList<FlightPlacesModel> mPlaces) {
        this.mPlaces = mPlaces;
    }


    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }


    public String getCity_from() {
        return city_from;
    }

    public void setCity_from(String city_from) {
        this.city_from = city_from;
    }

    public String getCountry_from() {
        return country_from;
    }

    public void setCountry_from(String country_from) {
        this.country_from = country_from;
    }

    public String getIata_from() {
        return iata_from;
    }

    public void setIata_from(String iata_from) {
        this.iata_from = iata_from;
    }

    public String getCity_to() {
        return city_to;
    }

    public void setCity_to(String city_to) {
        this.city_to = city_to;
    }

    public String getCountry_to() {
        return country_to;
    }

    public void setCountry_to(String country_to) {
        this.country_to = country_to;
    }

    public String getIata_to() {
        return iata_to;
    }

    public void setIata_to(String iata_to) {
        this.iata_to = iata_to;
    }
}
