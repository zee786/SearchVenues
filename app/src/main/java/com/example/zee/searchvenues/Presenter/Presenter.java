package com.example.zee.searchvenues.Presenter;

import android.content.Context;

import com.example.zee.searchvenues.model.LocationTracker;
import com.example.zee.searchvenues.model.VenueSearch;
import com.example.zee.searchvenues.utility.VenueModel;
import com.example.zee.searchvenues.view.MainController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zee on 8/16/2017.
 */

public class Presenter {

    private LocationTracker tracker;
    private VenueSearch searchService;
    private MainController mainView;
    private String gpsSettingTitle="GPS Settings";
    private String gpsSettingMessage="GPS is not enabled. Please enable it using settings menu";
    private final String apiUrl="";
    private final String clientID="YFRT0F0EKMLCUYYQO04YWQMUN15MXZH2KEXU5ZGN2D1QV0CF"; //client id
    private final String clientSecret="I2NQKHQPPL3DCCBTWU4PP4ZF2WJMGYZEOXIVXSHF0K330RJA";//client secret
    private final String baseQuery = "https://api.foursquare.com/v2/venues/search?"; //base query string from foursquare
    private final int apiVersion=20130815; // api version
    private double latitude=0.0; //latitude
    private double longitude=0.0;//longitude

    private String searchQuery;


    public Presenter(MainController view, VenueSearch searchService) {
        this.mainView = view;
        this.searchService = searchService;
        tracker = new LocationTracker(this.getSearchAppContext());

    }

    private Context getSearchAppContext() {
        return mainView.getSearchAppContext();
    }

    public void search(String query) {
        searchQuery = query;
        //check if GPS service is able to get location if false then show alert dialog to user
        if (!tracker.canGetLocation()) {

            showSettingsAlert(gpsSettingTitle, gpsSettingMessage);
        } else {
            // get latitude from GPS service
            latitude = tracker.getLatitude();
            // get longitude from GPS service
            longitude = tracker.getLongitude();
            // call search service and pass Presenter object to get response back and current geo positioning data
            searchService.search(this, buildQuery(searchQuery, latitude, longitude));
        }
    }

    // if GPS is disabled then show alert dialog to user
    private void showSettingsAlert(String title, String message) {
        mainView.showSettingsAlert(title, message);

    }

    //method to show error messages on activity display
    public void showResult(String errorMessage) {

        mainView.showResult(errorMessage);
    }

    public void showResponse(List<VenueModel> venueListDTOs) {
        // in case of request failure we will get only one obeject in Array list


        if (!venueListDTOs.isEmpty()) {
            if (venueListDTOs.get(0).isRequestFailed()) {
                //if due to any network issue request get failed then we will show general error message
                showResult("Connection with server failed, please try again later");
            } else {
                //populate list view on application view with venues received against user's query.
                mainView.populateListView((ArrayList<VenueModel>) venueListDTOs);

            }
        } else {
            // if foursqure api does not return anything in response to query then we will show this message to user
            showResult("No relevant venue found against your search string...");
            mainView.clearListView();

        }
        // search view is empty or user clear the search view, this method will clear Venue list on application view.
        if (searchQuery.isEmpty() || searchQuery.equals("")) {
            mainView.clearListView();
        }


    }

    // we will stop location manager on application kill, this method will be called at Activity.onDestory method
    public void stopLocationManager() {
        if (tracker != null) {
            // stop the gps data collection service.
            tracker.stopUsingLocation();
            tracker.stopSelf();

        }
    }


    //build the search query to get data from server.
    public String buildQuery(String query, double latitude, double longitude) {
        // create query string
        String searchQuery = baseQuery + "client_id=" + clientID
                + "&client_secret=" + clientSecret + "&v=" + apiVersion
                + "&ll=" + this.latitude + "," + this.longitude + "&query=" + query;

        return searchQuery;
    }
}
