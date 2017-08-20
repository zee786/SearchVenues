

package com.example.zee.searchvenues.view;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.zee.searchvenues.Presenter.Presenter;
import com.example.zee.searchvenues.R;
import com.example.zee.searchvenues.model.VenueSearch;
import com.example.zee.searchvenues.utility.VenueModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainController{
    private RecyclerView.Adapter venuesRecyclerAdapter;
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<VenueModel> venueListDTOs;
    private Presenter presenter;
    private SearchView searchView;
    private TextView textViewErrorReport;
    private ListView listViewShowVenues;
    private String searchQuery;
    private ArrayAdapter<VenueModel> adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stop location manager
        stopLocationManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewErrorReport = (TextView) findViewById(R.id.item_tvErrorReport);
        // disable error report text view.
        textViewErrorReport.setVisibility(View.GONE);
        listViewShowVenues = (ListView) findViewById(R.id.item_listView);
        searchView = (SearchView) findViewById(R.id.item_searchVenue);
        // register listener to listen every character entered
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // call search method on click on submit button
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            // call search method on every key pressed
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

        presenter = new Presenter(this, new VenueSearch());
    }


    private void search(String query) {
        // instantiate Presenter object

        this.searchQuery = query;
        // pass the search query to presenter
        presenter.search(searchQuery);

    }


    @Override
    public void showSettingsAlert(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

       //Dialog title
        alertDialog.setTitle(title);

        //  Dialog Message
        alertDialog.setMessage(message);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // show intent to user to enable gps settings
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    // show if any error occurred while getting results from server.
    @Override
    public void showResult(String result) {
        // make text view visible
        textViewErrorReport.setVisibility(View.VISIBLE);
        // show error message
        textViewErrorReport.setText(result);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationManager();
    }

    // at the end give signal to presenter to stop location manager.
    @Override
    public void stopLocationManager() {
        if (presenter != null) {
            presenter.stopLocationManager();
        }

    }

    @Override
    public Context getSearchAppContext() {
        return this.getBaseContext();
    }

    // populate listview with the data received from service call.
    @Override
    public void populateListView(ArrayList<VenueModel> venueListDTOs) {
        // hide textview which we created for displaying the error message
        textViewErrorReport.setVisibility(View.GONE);

        this.venueListDTOs = venueListDTOs;
        // instantiate adapter
        adapter = new MyListAdapter();
        // attach listview to the adapter
        listViewShowVenues.setAdapter(adapter);

    }

    // clear list view
    @Override
    public void clearListView() {

        if (venueListDTOs != null) {
            // clear DTO
            venueListDTOs.clear();
            // set adapter to null
            listViewShowVenues.setAdapter(null);
            // notify the adapter about change
            adapter.notifyDataSetChanged();
        }

    }


    //Adapter is a part of View. It does not have any relation to Presenter at all. A presenter should not know how view will display returned data.
    private class MyListAdapter extends ArrayAdapter<VenueModel> {

        public MyListAdapter() {
            super(MainActivity.this, R.layout.venue_list_view, venueListDTOs);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemsListView = convertView;
            //To make sure we have a itemsListView because convertView can be null
            if (itemsListView == null)
                itemsListView = getLayoutInflater().inflate(R.layout.venue_list_view, parent, false);

            TextView name = (TextView) itemsListView.findViewById(R.id.item_TvName);
            TextView distance = (TextView) itemsListView.findViewById(R.id.item_TvDistance);
            TextView address = (TextView) itemsListView.findViewById(R.id.item_TvAddress);

            // get venueListDTO object from current index position of ArrayList
            VenueModel currentItem = venueListDTOs.get(position);


//            if (currentItem.getName() != null) {
            name.setText(currentItem.getName());

//            }
            distance.setText(currentItem.getDistance().toString());

//            if (currentItem.getAddress() != null)
            address.setText(currentItem.getAddress());

            return itemsListView;
        }

    }

}
