package io.rocketapps.apps.android.flightcompanion.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class PlacesImagesModel extends RealmObject {

    @Index
    long id;
    String link;
    String title;
    long created_at;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
