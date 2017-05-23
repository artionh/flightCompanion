package io.rocketapps.apps.android.flightcompanion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.rocketapps.apps.android.flightcompanion.R;
import io.rocketapps.apps.android.flightcompanion.model.RealmCustomObject;

public class FlightRecyclerAdapter extends RecyclerView.Adapter<FlightRecyclerViewHolder> {

    ArrayList<RealmCustomObject> mObjects;
    Context mContext;

    private final OnListFragmentInteractionListener mListener;


    public FlightRecyclerAdapter(ArrayList<RealmCustomObject> mObjects, Context mContext,  OnListFragmentInteractionListener mListener) {
        this.mObjects = mObjects;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public FlightRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case RealmCustomObject.SAVED_FLIGHT_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_saved_flight, parent, false);
                break;
            case RealmCustomObject.PLACES_LIST:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_flight_place, parent, false);
                break;
        }
        return new FlightRecyclerViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final FlightRecyclerViewHolder holder, int position) {

        holder.bind(mContext, mObjects.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return mObjects.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }
}
