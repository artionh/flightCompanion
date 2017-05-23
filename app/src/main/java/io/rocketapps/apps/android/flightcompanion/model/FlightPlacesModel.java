package io.rocketapps.apps.android.flightcompanion.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;

public class FlightPlacesModel extends RealmObject {

    @Index
    private long id;
    private long server_id;
    double lat;
    double lng;
    String name;
    long created_at;

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    RealmList<PlacesDetailsModel> details;
    RealmList<PlacesImagesModel> images;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServer_id() {
        return server_id;
    }

    public void setServer_id(long server_id) {
        this.server_id = server_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<PlacesDetailsModel> getDetails() {
        return details;
    }

    public void setDetails(RealmList<PlacesDetailsModel> details) {
        this.details = details;
    }

    public RealmList<PlacesImagesModel> getImages() {
        return images;
    }

    public void setImages(RealmList<PlacesImagesModel> images) {
        this.images = images;
    }
}